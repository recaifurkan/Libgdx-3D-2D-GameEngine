package com.rfbsoft.utils.g3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.rfbsoft.game.GameFields;
import org.recast4j.detour.*;

import java.util.List;

public class DetourDebugRender {

    public static final int DRAWNAVMESH_OFFMESHCONS = 0x01;
    public static final int DRAWNAVMESH_CLOSEDLIST = 0x02;
    public static final int DRAWNAVMESH_COLOR_TILES = 0x04;
    private static final int NUM_ARC_PTS = 8;
    private static final float PAD = 0.05f;
    private static final float ARC_PTS_SCALE = (1.0f - PAD * 2) / NUM_ARC_PTS;
    public static ImmediateModeRenderer20 renderer = new ImmediateModeRenderer20(false, true, 0);
    static Color color = new Color();

    public static void renderNavMesh(NavMesh mesh, Color color) {
        for (int i = 0; i < mesh.getMaxTiles(); ++i) {
            MeshTile tile = mesh.getTile(i);
            if (tile == null || tile.data == null || tile.data.header == null) {
                continue;
            }
            long base = mesh.getPolyRefBase(tile);

            for (int j = 0; j < tile.data.header.polyCount; ++j) {
                Poly p = tile.data.polys[j];
                if ((p.flags) == 0) {
                    continue;
                }
                debugDrawNavMeshPoly(mesh, base | j, color);
            }
        }
    }

    public static Color duIntToCol(int i, int a) {
        int r = bit(i, 1) + bit(i, 3) * 2 + 1;
        int g = bit(i, 2) + bit(i, 4) * 2 + 1;
        int b = bit(i, 0) + bit(i, 5) * 2 + 1;
        return duRGBA(r * 63, g * 63, b * 63, a);
    }

    public static int bit(int a, int b) {
        return (a & (1 << b)) >>> b;
    }

    public static int duTransCol(int c, int a) {
        return (a << 24) | (c & 0x00ffffff);
    }

    public static Color areaToCol(int area) {
        if (area == 0) {
            return duRGBA(0, 192, 255, 255);
        } else {
            return duIntToCol(area, 255);
        }
    }

    private static Color duRGBA(int i, int i1, int i2, int i3) {

        return color.set(i, i1, i2, i3);
    }

    public static void debugDrawNavMeshPoly(NavMesh mesh, long ref, Color col) {
        if (ref == 0) {
            return;
        }
        Result<Tupple2<MeshTile, Poly>> tileAndPolyResult = mesh.getTileAndPolyByRef(ref);
        if (tileAndPolyResult.failed()) {
            return;
        }
        Tupple2<MeshTile, Poly> tileAndPoly = tileAndPolyResult.result;
        MeshTile tile = tileAndPoly.first;
        Poly poly = tileAndPoly.second;


        int ip = poly.index;

        if (poly.getType() == Poly.DT_POLYTYPE_OFFMESH_CONNECTION) {
            OffMeshConnection con = tile.data.offMeshCons[ip - tile.data.header.offMeshBase];

            renderer.begin(GameFields.cam.combined, GL30.GL_LINES);
            renderer.color(col);
            // Connection arc.
            appendArc(con.pos[0], con.pos[1], con.pos[2], con.pos[3], con.pos[4], con.pos[5], 0.25f,
                    (con.flags & 1) != 0 ? 0.6f : 0.0f, 0.6f);

            renderer.end();
        } else {
            PolyDetail pd = tile.data.detailMeshes[ip];
            renderer.begin(GameFields.cam.combined, GL30.GL_LINE_STRIP);

            for (int i = 0; i < pd.triCount; ++i) {
                int t = (pd.triBase + i) * 4;
                for (int j = 0; j < 3; ++j) {
                    if (tile.data.detailTris[t + j] < poly.vertCount) {
                        renderer.color(col);
                        renderer.vertex(tile.data.verts[poly.verts[tile.data.detailTris[t + j]] * 3],
                                tile.data.verts[poly.verts[tile.data.detailTris[t + j]] * 3 + 1],
                                tile.data.verts[poly.verts[tile.data.detailTris[t + j]] * 3 + 2]);
                    } else {
                        renderer.color(col);
                        renderer.vertex(tile.data.detailVerts[(pd.vertBase + tile.data.detailTris[t + j] - poly.vertCount) * 3],
                                tile.data.detailVerts[(pd.vertBase + tile.data.detailTris[t + j] - poly.vertCount) * 3
                                        + 1],
                                tile.data.detailVerts[(pd.vertBase + tile.data.detailTris[t + j] - poly.vertCount) * 3
                                        + 2]);
                    }
                }
            }
            renderer.end();
        }

    }

    public static void appendArc(float x0, float y0, float z0, float x1, float y1, float z1, float h, float as0, float as1) {
        float dx = x1 - x0;
        float dy = y1 - y0;
        float dz = z1 - z0;
        float len = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        float[] prev = new float[3];
        evalArc(x0, y0, z0, dx, dy, dz, len * h, PAD, prev);
        for (int i = 1; i <= NUM_ARC_PTS; ++i) {
            float u = PAD + i * ARC_PTS_SCALE;
            float[] pt = new float[3];
            evalArc(x0, y0, z0, dx, dy, dz, len * h, u, pt);
            renderer.vertex(prev[0], prev[1], prev[2]);
            renderer.vertex(pt[0], pt[1], pt[2]);
            prev[0] = pt[0];
            prev[1] = pt[1];
            prev[2] = pt[2];
        }

        // End arrows
        if (as0 > 0.001f) {
            float[] p = new float[3], q = new float[3];
            evalArc(x0, y0, z0, dx, dy, dz, len * h, PAD, p);
            evalArc(x0, y0, z0, dx, dy, dz, len * h, PAD + 0.05f, q);
            appendArrowHead(p, q, as0);
        }

        if (as1 > 0.001f) {
            float[] p = new float[3], q = new float[3];
            evalArc(x0, y0, z0, dx, dy, dz, len * h, 1 - PAD, p);
            evalArc(x0, y0, z0, dx, dy, dz, len * h, 1 - (PAD + 0.05f), q);
            appendArrowHead(p, q, as1);
        }
    }

    private static void evalArc(float x0, float y0, float z0, float dx, float dy, float dz, float h, float u, float[] res) {
        res[0] = x0 + dx * u;
        res[1] = y0 + dy * u + h * (1 - (u * 2 - 1) * (u * 2 - 1));
        res[2] = z0 + dz * u;
    }

    static void appendArrowHead(float[] p, float[] q, float s) {
        float eps = 0.001f;
        if (vdistSqr(p, q) < eps * eps) {
            return;
        }
        float[] ax = new float[3], ay = {0, 1, 0}, az = new float[3];
        vsub(az, q, p);
        vnormalize(az);
        vcross(ax, ay, az);
        vcross(ay, az, ax);
        vnormalize(ay);

        renderer.vertex(p[0], p[1], p[2]);
        // vertex(p[0]+az[0]*s+ay[0]*s/2, p[1]+az[1]*s+ay[1]*s/2, p[2]+az[2]*s+ay[2]*s/2, col);
        renderer.vertex(p[0] + az[0] * s + ax[0] * s / 3, p[1] + az[1] * s + ax[1] * s / 3, p[2] + az[2] * s + ax[2] * s / 3);

        renderer.vertex(p[0], p[1], p[2]);
        // vertex(p[0]+az[0]*s-ay[0]*s/2, p[1]+az[1]*s-ay[1]*s/2, p[2]+az[2]*s-ay[2]*s/2, col);
        renderer.vertex(p[0] + az[0] * s - ax[0] * s / 3, p[1] + az[1] * s - ax[1] * s / 3, p[2] + az[2] * s - ax[2] * s / 3);

    }

    static void vcross(float[] dest, float[] v1, float[] v2) {
        dest[0] = v1[1] * v2[2] - v1[2] * v2[1];
        dest[1] = v1[2] * v2[0] - v1[0] * v2[2];
        dest[2] = v1[0] * v2[1] - v1[1] * v2[0];
    }

    static void vnormalize(float[] v) {
        float d = (float) (1.0f / Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]));
        v[0] *= d;
        v[1] *= d;
        v[2] *= d;
    }

    static void vsub(float[] dest, float[] v1, float[] v2) {
        dest[0] = v1[0] - v2[0];
        dest[1] = v1[1] - v2[1];
        dest[2] = v1[2] - v2[2];
    }

    static float vdistSqr(float[] v1, float[] v2) {
        float x = v1[0] - v2[0];
        float y = v1[1] - v2[1];
        float z = v1[2] - v2[2];
        return x * x + y * y + z * z;
    }

    public static Color duLerpCol(Color ca, Color cb, int u) {
        int ra = (int) ca.r & 0xff;
        int ga = (int) ca.g >> 8 & 0xff;
        int ba = (int) ca.b >> 16 & 0xff;
        int aa = (int) ca.a >> 24 & 0xff;
        int rb = (int) cb.r & 0xff;
        int gb = (int) cb.g >> 8 & 0xff;
        int bb = (int) cb.b >> 16 & 0xff;
        int ab = (int) cb.a >> 24 & 0xff;

        int r = (ra * (255 - u) + rb * u) / 255;
        int g = (ga * (255 - u) + gb * u) / 255;
        int b = (ba * (255 - u) + bb * u) / 255;
        int a = (aa * (255 - u) + ab * u) / 255;
        return duRGBA(r, g, b, a);
    }

    public void debugDrawTriMeshSlope(float[] verts, int[] tris, float[] normals, float walkableSlopeAngle,
                                      float texScale) {

        float walkableThr = (float) Math.cos(walkableSlopeAngle / 180.0f * Math.PI);

        float[] uva = new float[2];
        float[] uvb = new float[2];
        float[] uvc = new float[2];

//        texture(true);

        Color unwalkable = duRGBA(192, 128, 0, 255);
        begin(GL30.GL_TRIANGLE_STRIP);
        for (int i = 0; i < tris.length; i += 3) {
            float[] norm = new float[]{normals[i], normals[i + 1], normals[i + 2]};

            Color color;
            int a = (int) (220 * (2 + norm[0] + norm[1]) / 4);
            if (norm[1] < walkableThr) {
                color = duLerpCol(duRGBA(a, a, a, 255), unwalkable, 64);
            } else {
                color = duRGBA(a, a, a, 255);
            }

            float[] va = new float[]{verts[tris[i] * 3], verts[tris[i] * 3 + 1], verts[tris[i] * 3 + 2]};
            float[] vb = new float[]{verts[tris[i + 1] * 3], verts[tris[i + 1] * 3 + 1], verts[tris[i + 1] * 3 + 2]};
            float[] vc = new float[]{verts[tris[i + 2] * 3], verts[tris[i + 2] * 3 + 1], verts[tris[i + 2] * 3 + 2]};

            int ax = 0, ay = 0;
            if (Math.abs(norm[1]) > Math.abs(norm[ax])) {
                ax = 1;
            }
            if (Math.abs(norm[2]) > Math.abs(norm[ax])) {
                ax = 2;
            }
            ax = (1 << ax) & 3; // +1 mod 3
            ay = (1 << ax) & 3; // +1 mod 3

            uva[0] = va[ax] * texScale;
            uva[1] = va[ay] * texScale;
            uvb[0] = vb[ax] * texScale;
            uvb[1] = vb[ay] * texScale;
            uvc[0] = vc[ax] * texScale;
            uvc[1] = vc[ay] * texScale;

            vertex(va, color, uva);
            vertex(vb, color, uvb);
            vertex(vc, color, uvc);
        }
        end();

//        texture(false);
    }

    void vertex(float[] pos, Color color, float[] uv) {
        renderer.texCoord(uv[0], uv[1]);
        renderer.color(color);
        renderer.vertex(pos[0], pos[1], pos[2]);
    }

    private void end() {
        renderer.end();
    }

    private void begin(int glTriangleStrip) {
        renderer.begin(GameFields.cam.combined, glTriangleStrip);
    }

    public void debugDrawNavMeshNodes(NavMeshQuery query, Color color) {
        NodePool pool = query.getNodePool();
        if (pool != null) {
            float off = 0.5f;
            renderer.begin(GameFields.cam.combined, GL30.GL_POINTS);

            for (List<Node> nodes : pool.getNodeMap().values()) {

                for (Node node : nodes) {
                    if (node == null) {
                        continue;
                    }
                    vertex(node.pos[0], node.pos[1] + off, node.pos[2], duRGBA(255, 192, 0, 255));

                }
            }
            renderer.end();

            renderer.begin(GameFields.cam.combined, GL30.GL_LINES);
//            begin(DebugDrawPrimitives.LINES, 2.0f);
            for (List<Node> nodes : pool.getNodeMap().values()) {

                for (Node node : nodes) {
                    if (node == null) {
                        continue;
                    }
                    if (node.pidx == 0) {
                        continue;
                    }
                    Node parent = pool.getNodeAtIdx(node.pidx);
                    if (parent == null) {
                        continue;
                    }
                    renderer.color(255, 192, 0, 128);
                    renderer.vertex(node.pos[0], node.pos[1] + off, node.pos[2]);
                    vertex(parent.pos[0], parent.pos[1] + off, parent.pos[2], duRGBA(255, 192, 0, 128));
                }
            }
            renderer.end();
        }
    }

    private void vertex(float po, float v, float po1, Color duRGBA) {
        renderer.color(duRGBA);
        renderer.vertex(po, v, po1);
    }

//    void drawPolyBoundaries(MeshTile tile, int col, float linew, boolean inner) {
//        float thr = 0.01f * 0.01f;
//
//        begin(GL30.GL_LINES);
//
//        for (int i = 0; i < tile.data.header.polyCount; ++i) {
//            Poly p = tile.data.polys[i];
//
//            if (p.getType() == Poly.DT_POLYTYPE_OFFMESH_CONNECTION) {
//                continue;
//            }
//
//            PolyDetail pd = tile.data.detailMeshes[i];
//
//            for (int j = 0, nj = p.vertCount; j < nj; ++j) {
//                int c = col;
//                if (inner) {
//                    if (p.neis[j] == 0) {
//                        continue;
//                    }
//                    if ((p.neis[j] & NavMesh.DT_EXT_LINK) != 0) {
//                        boolean con = false;
//                        for (int k = p.firstLink; k != NavMesh.DT_NULL_LINK; k = tile.links.get(k).next) {
//                            if (tile.links.get(k).edge == j) {
//                                con = true;
//                                break;
//                            }
//                        }
//                        if (con) {
//                            c = duRGBA(255, 255, 255, 48);
//                        } else {
//                            c = duRGBA(0, 0, 0, 48);
//                        }
//                    } else {
//                        c = duRGBA(0, 48, 64, 32);
//                    }
//                } else {
//                    if (p.neis[j] != 0) {
//                        continue;
//                    }
//                }
//
//                float[] v0 = new float[] { tile.data.verts[p.verts[j] * 3], tile.data.verts[p.verts[j] * 3 + 1],
//                        tile.data.verts[p.verts[j] * 3 + 2] };
//                float[] v1 = new float[] { tile.data.verts[p.verts[(j + 1) % nj] * 3],
//                        tile.data.verts[p.verts[(j + 1) % nj] * 3 + 1],
//                        tile.data.verts[p.verts[(j + 1) % nj] * 3 + 2] };
//
//                // Draw detail mesh edges which align with the actual poly edge.
//                // This is really slow.
//                for (int k = 0; k < pd.triCount; ++k) {
//                    int t = (pd.triBase + k) * 4;
//                    float[][] tv = new float[3][];
//                    for (int m = 0; m < 3; ++m) {
//                        int v = tile.data.detailTris[t + m];
//                        if (v < p.vertCount) {
//                            tv[m] = new float[] { tile.data.verts[p.verts[v] * 3], tile.data.verts[p.verts[v] * 3 + 1],
//                                    tile.data.verts[p.verts[v] * 3 + 2] };
//                        } else {
//                            tv[m] = new float[] { tile.data.detailVerts[(pd.vertBase + (v - p.vertCount)) * 3],
//                                    tile.data.detailVerts[(pd.vertBase + (v - p.vertCount)) * 3 + 1],
//                                    tile.data.detailVerts[(pd.vertBase + (v - p.vertCount)) * 3 + 2] };
//                        }
//                    }
//                    for (int m = 0, n = 2; m < 3; n = m++) {
//                        if ((NavMesh.getDetailTriEdgeFlags(tile.data.detailTris[t + 3], n) & NavMesh.DT_DETAIL_EDGE_BOUNDARY) == 0)
//                            continue;
//
//                        if (((tile.data.detailTris[t + 3] >> (n * 2)) & 0x3) == 0) {
//                            continue; // Skip inner detail edges.
//                        }
//                        if (distancePtLine2d(tv[n], v0, v1) < thr && distancePtLine2d(tv[m], v0, v1) < thr) {
//                            vertex(tv[n], c);
//                            vertex(tv[m], c);
//                        }
//                    }
//                }
//            }
//        }
//        end();
//    }
//
//    private void drawMeshTile(NavMesh mesh, NavMeshQuery query, MeshTile tile, int flags) {
//        long base = mesh.getPolyRefBase(tile);
//
//        int tileNum = NavMesh.decodePolyIdTile(base);
//        Color tileColor = duIntToCol(tileNum, 128);
////        depthMask(false);
//        begin(GL30.GL_TRIANGLE_STRIP);
//        for (int i = 0; i < tile.data.header.polyCount; ++i) {
//            Poly p = tile.data.polys[i];
//            if (p.getType() == Poly.DT_POLYTYPE_OFFMESH_CONNECTION) {
//                continue;
//            }
//
//            PolyDetail pd = tile.data.detailMeshes[i];
//
//            Color col;
//            if (query != null && query.isInClosedList(base | i)) {
//                col = duRGBA(255, 196, 0, 64);
//            } else {
//                if ((flags & DRAWNAVMESH_COLOR_TILES) != 0) {
//                    col = tileColor;
//                } else {
//                    col = duTransCol(areaToCol(p.getArea()), 64);
//                }
//            }
//
//            for (int j = 0; j < pd.triCount; ++j) {
//                int t = (pd.triBase + j) * 4;
//                for (int k = 0; k < 3; ++k) {
//                    int v = tile.data.detailTris[t + k];
//                    if (v < p.vertCount) {
//                        vertex(tile.data.verts[p.verts[v] * 3], tile.data.verts[p.verts[v] * 3 + 1],
//                                tile.data.verts[p.verts[v] * 3 + 2], col);
//                    } else {
//                        vertex(tile.data.detailVerts[(pd.vertBase + v - p.vertCount) * 3],
//                                tile.data.detailVerts[(pd.vertBase + v - p.vertCount) * 3 + 1],
//                                tile.data.detailVerts[(pd.vertBase + v - p.vertCount) * 3 + 2], col);
//                    }
//                }
//            }
//
//        }
//        end();
//
//        // Draw inter poly boundaries
//        drawPolyBoundaries(tile, duRGBA(0, 48, 64, 32), 1.5f, true);
//
//        // Draw outer poly boundaries
//        drawPolyBoundaries(tile, duRGBA(0, 48, 64, 220), 2.5f, false);
//
//        if ((flags & DRAWNAVMESH_OFFMESHCONS) != 0) {
//            begin(DebugDrawPrimitives.LINES, 2.0f);
//            for (int i = 0; i < tile.data.header.polyCount; ++i) {
//                Poly p = tile.data.polys[i];
//
//                if (p.getType() != Poly.DT_POLYTYPE_OFFMESH_CONNECTION) {
//                    continue;
//                }
//
//                int col, col2;
//                if (query != null && query.isInClosedList(base | i)) {
//                    col = duRGBA(255, 196, 0, 220);
//                } else {
//                    col = duDarkenCol(duTransCol(areaToCol(p.getArea()), 220));
//                }
//
//                OffMeshConnection con = tile.data.offMeshCons[i - tile.data.header.offMeshBase];
//                float[] va = new float[] { tile.data.verts[p.verts[0] * 3], tile.data.verts[p.verts[0] * 3 + 1],
//                        tile.data.verts[p.verts[0] * 3 + 2] };
//                float[] vb = new float[] { tile.data.verts[p.verts[1] * 3], tile.data.verts[p.verts[1] * 3 + 1],
//                        tile.data.verts[p.verts[1] * 3 + 2] };
//
//                // Check to see if start and end end-points have links.
//                boolean startSet = false;
//                boolean endSet = false;
//                for (int k = p.firstLink; k != NavMesh.DT_NULL_LINK; k = tile.links.get(k).next) {
//                    if (tile.links.get(k).edge == 0) {
//                        startSet = true;
//                    }
//                    if (tile.links.get(k).edge == 1) {
//                        endSet = true;
//                    }
//                }
//
//                // End points and their on-mesh locations.
//                vertex(va[0], va[1], va[2], col);
//                vertex(con.pos[0], con.pos[1], con.pos[2], col);
//                col2 = startSet ? col : duRGBA(220, 32, 16, 196);
//                appendCircle(con.pos[0], con.pos[1] + 0.1f, con.pos[2], con.rad, col2);
//
//                vertex(vb[0], vb[1], vb[2], col);
//                vertex(con.pos[3], con.pos[4], con.pos[5], col);
//                col2 = endSet ? col : duRGBA(220, 32, 16, 196);
//                appendCircle(con.pos[3], con.pos[4] + 0.1f, con.pos[5], con.rad, col2);
//
//                // End point vertices.
//                vertex(con.pos[0], con.pos[1], con.pos[2], duRGBA(0, 48, 64, 196));
//                vertex(con.pos[0], con.pos[1] + 0.2f, con.pos[2], duRGBA(0, 48, 64, 196));
//
//                vertex(con.pos[3], con.pos[4], con.pos[5], duRGBA(0, 48, 64, 196));
//                vertex(con.pos[3], con.pos[4] + 0.2f, con.pos[5], duRGBA(0, 48, 64, 196));
//
//                // Connection arc.
//                appendArc(con.pos[0], con.pos[1], con.pos[2], con.pos[3], con.pos[4], con.pos[5], 0.25f,
//                        (con.flags & 1) != 0 ? 0.6f : 0, 0.6f, col);
//
//            }
//
//            end();
//        }
//
//        int vcol = duRGBA(0, 0, 0, 196);
//        begin(DebugDrawPrimitives.POINTS, 3.0f);
//        for (int i = 0; i < tile.data.header.vertCount; i += 3) {
//            vertex(tile.data.verts[i], tile.data.verts[i + 1], tile.data.verts[i + 2], vcol);
//        }
//        end();
//
//        depthMask(true);
//    }

//    public void debugDrawNavMeshWithClosedList(NavMesh mesh, NavMeshQuery query, int flags) {
//        NavMeshQuery q = (flags & DRAWNAVMESH_CLOSEDLIST) != 0 ? query : null;
//        for (int i = 0; i < mesh.getMaxTiles(); ++i) {
//            MeshTile tile = mesh.getTile(i);
//            if (tile != null && tile.data != null) {
//                drawMeshTile(mesh, q, tile, flags);
//            }
//        }
//    }
}
