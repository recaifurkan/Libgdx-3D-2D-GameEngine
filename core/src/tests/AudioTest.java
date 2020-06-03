package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;

import java.nio.ByteBuffer;

public class AudioTest extends ApplicationAdapter {
    int SIZE = 1024 * 4;
    short[] samples = new short[SIZE];

    AudioDevice device;
    AudioRecorder recorder;

    @Override
    public void create() {
        device = Gdx.audio.newAudioDevice(44100, true);
        recorder = Gdx.audio.newAudioRecorder(44100, true);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    recorder.read(samples, 0, samples.length);

                    device.writeSamples(samples, 0, samples.length);
                }
            }
        });


        t.setDaemon(true);
        t.start();

    }

    @Override
    public void resume() {
        device = Gdx.audio.newAudioDevice(44100, true);
        recorder = Gdx.audio.newAudioRecorder(44100, true);
    }

    public byte[] shortArrayToByte(short[] buf) {
        ByteBuffer buffer = ByteBuffer.allocate(buf.length * 2);

        for (int i = 0; i < buf.length; i++) {
            buffer.putShort(buf[i]);
        }
        return buffer.array();


    }

    public short[] byteArrayToShort(byte[] buf) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(buf.length).put(buf);
        short[] shorts = new short[buf.length / 2];

        for (int i = 0; i < shorts.length; i++) {
            shorts[i] = byteBuffer.getShort(i);
        }
        return shorts;


    }
}
