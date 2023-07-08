

package com.rfbsoft.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Scaling;

import static com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;

public class AnimatedImageButton extends Button {

    private static final float ANIMATION_SPEED = 0.2f;
    private final Image image;
    private ImageButton.ImageButtonStyle style;

    private Animation<TextureRegion> downWalk;
    private Animation<TextureRegion> leftWalk;
    private Animation<TextureRegion> rightWalk;
    private Animation<TextureRegion> upWalk;

    private Animation<TextureRegion> selectedAnimation;

    private TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();

    private float stateTime = 0;

    public AnimatedImageButton(Skin skin) {
        this(skin.get(ImageButtonStyle.class));
        setSkin(skin);
    }

    public AnimatedImageButton(Skin skin, String styleName) {
        this(skin.get(styleName, ImageButtonStyle.class));
        setSkin(skin);
    }

    public AnimatedImageButton(ImageButtonStyle style) {
        super(style);
        image = newImage();
        add(image);
        setStyle(style);
        setSize(getPrefWidth(), getPrefHeight());
        Texture texture = new Texture(Gdx.files.internal("data/animation.png"));
        TextureRegion[][] regions = TextureRegion.split(texture, 32, 48);
        TextureRegion[] downWalkReg = regions[0];
        TextureRegion[] leftWalkReg = regions[1];
        TextureRegion[] rightWalkReg = regions[2];
        TextureRegion[] upWalkReg = regions[3];
        downWalk = new Animation<TextureRegion>(ANIMATION_SPEED, downWalkReg);
        leftWalk = new Animation<TextureRegion>(ANIMATION_SPEED, leftWalkReg);
        rightWalk = new Animation<TextureRegion>(ANIMATION_SPEED, rightWalkReg);
        upWalk = new Animation<TextureRegion>(ANIMATION_SPEED, upWalkReg);
    }

    public AnimatedImageButton(@Null Drawable imageUp) {
        this(new ImageButtonStyle(null, null, null, imageUp, null, null));
    }

    public AnimatedImageButton(@Null Drawable imageUp, @Null Drawable imageDown) {
        this(new ImageButtonStyle(null, null, null, imageUp, imageDown, null));
    }

    public AnimatedImageButton(@Null Drawable imageUp, @Null Drawable imageDown, @Null Drawable imageChecked) {
        this(new ImageButtonStyle(null, null, null, imageUp, imageDown, imageChecked));
    }

    protected Image newImage() {
        return new Image((Drawable) null, Scaling.fit);
    }

    public void setStyle(ButtonStyle style) {
        if (!(style instanceof ImageButtonStyle))
            throw new IllegalArgumentException("style must be an ImageButtonStyle.");
        this.style = (ImageButtonStyle) style;
        super.setStyle(style);

        if (image != null) updateImage();
    }

    public ImageButtonStyle getStyle() {
        return style;
    }

    /**
     * Returns the appropriate image drawable from the style based on the current button state.
     */
    protected @Null Drawable getImageDrawable() {
        return style.imageUp;
    }

    protected @Null Animation<TextureRegion> getImageAnimation() {
        if (isPressed()) {
            return rightWalk;
        }
        return downWalk;
    }

    /**
     * Sets the image drawable based on the current button state. The default implementation sets the image drawable using
     * {@link #getImageDrawable()}.
     */
    protected void updateImage() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        stateTime += deltaTime;
        Animation<TextureRegion> selectedAnimation = getImageAnimation();
        Drawable drawable = getImageDrawable();
        if (selectedAnimation != null) {
            TextureRegion frame = selectedAnimation.getKeyFrame(stateTime, true);
            textureRegionDrawable.setRegion(frame);
            drawable = textureRegionDrawable;
        }
        image.setDrawable(drawable);
    }

    public void draw(Batch batch, float parentAlpha) {
        updateImage();
        super.draw(batch, parentAlpha);
    }

    public Image getImage() {
        return image;
    }

    public String toString() {
        String name = getName();
        if (name != null) return name;
        String className = getClass().getName();
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex != -1) className = className.substring(dotIndex + 1);
        return (className.indexOf('$') != -1 ? "ImageButton " : "") + className + ": " + image.getDrawable();
    }
}
