package com.rfbsoft.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.rfbsoft.Settings;
import com.rfbsoft.ads.AdsProcessor;
import com.rfbsoft.ads.AdsProcessorAdaptor;
import com.rfbsoft.assets.Assets;
import com.rfbsoft.game.ClientGameInitializer;
import com.rfbsoft.game.ClientGameInput;
import com.rfbsoft.game.GameFields;
import com.rfbsoft.game.ui.AnimatedImageButton;

public class GameScreen implements Screen {
    private static final String TAG = "com.rfbsoft.factories.EntityFactory";

    private final AdsProcessor adsProcessor;

    public GameScreen(AdsProcessor adsProcessor) {
        this.adsProcessor = adsProcessor == null ? new AdsProcessorAdaptor() : adsProcessor;
    }

    private UITest uiTest;


    ClientGameInitializer systems;

    @Override
    public void show() {

        Bullet.init();
        Settings.load();
        Assets.load();


        systems = new ClientGameInitializer();


        ClientGameInput gameInput = new ClientGameInput();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(gameInput);
        inputMultiplexer.addProcessor(GameFields.stage);
        if (GameFields.systemType == GameFields.GameType.G3D && GameFields.camController != null)
            inputMultiplexer.addProcessor(GameFields.camController);
        Gdx.input.setInputProcessor(inputMultiplexer);
        GameFields.inputMultiplexer = inputMultiplexer;

        uiTest = new UITest();
        uiTest.create();


    }

    public final Color backgroundColor = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1f);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        GameFields.gameEngine.update(delta);
        uiTest.render();


    }

    @Override
    public void resize(int width, int height) {
        uiTest.resize(width, height);

    }

    @Override
    public void pause() {
        uiTest.pause();

    }

    @Override
    public void resume() {
        uiTest.resume();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Assets.dispose();
        Settings.save();
        systems.dispose();
        uiTest.dispose();


    }

    public class UITest extends ApplicationAdapter {
        Object[] listEntries = {"This is a list entry1", "And another one1", "The meaning of life1", "Is hard to come by1",
                "This is a list entry2", "And another one2", "The meaning of life2", "Is hard to come by2", "This is a list entry3",
                "And another one3", "The meaning of life3", "Is hard to come by3", "This is a list entry4", "And another one4",
                "The meaning of life4", "Is hard to come by4", "This is a list entry5", "And another one5", "The meaning of life5",
                "Is hard to come by5"};

        Skin skin;
        Stage stage;
        Texture texture1;
        Texture texture2;
        Label fpsLabel;

        @Override
        public void create() {
            skin = new Skin(Gdx.files.internal("data/uiskin.json"));
            texture1 = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
            texture2 = new Texture(Gdx.files.internal("data/badlogic.jpg"));
            TextureRegion image = new TextureRegion(texture1);
            TextureRegion imageFlipped = new TextureRegion(image);
            imageFlipped.flip(true, true);
            TextureRegion image2 = new TextureRegion(texture2);
            // stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, new PolygonSpriteBatch());

            stage = GameFields.stage;

            // stage.setDebugAll(true);

            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.get(Button.ButtonStyle.class));
            style.imageUp = new TextureRegionDrawable(image);
            style.imageDown = new TextureRegionDrawable(imageFlipped);
            AnimatedImageButton iconButton = new AnimatedImageButton(style);

            Button buttonMulti = new TextButton("Multi\nLine\nToggle", skin, "toggle");
            Button imgButton = new Button(new Image(image), skin);
            Button imgToggleButton = new Button(new Image(image), skin, "toggle");

            TextButton adsButton1 = new TextButton("1", skin);
            adsButton1.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    adsProcessor.loadRewardedInterstitialAd();
                }
            });
            TextButton adsButton2 = new TextButton("2", skin);
            adsButton2.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    adsProcessor.loadRewardedAd();
                }
            });
            TextButton adsButton3 = new TextButton("3", skin);
            adsButton3.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    adsProcessor.loadInterstitialAd();
                }
            });
            TextButton adsButton4 = new TextButton("4", skin);
            adsButton4.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    adsProcessor.loadBannerView();
                }
            });
            TextButton adsButton5 = new TextButton("5", skin);
            adsButton5.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    adsProcessor.ins5();
                }
            });

            Label myLabel = new Label("this is some text.", skin);
            myLabel.setWrap(true);

            Table t = new Table();
            t.row();
            t.add(myLabel);

            t.layout();

            final CheckBox checkBox = new CheckBox(" Continuous rendering", skin);
            checkBox.setChecked(true);
            final Slider slider = new Slider(0, 10, 1, false, skin);
            slider.setAnimateDuration(0.3f);
            TextField textfield = new TextField("", skin);
            textfield.setMessageText("Click here!");
            textfield.setAlignment(Align.center);
            final SelectBox selectBox = new SelectBox(skin);
            selectBox.setAlignment(Align.right);
            selectBox.getList().setAlignment(Align.right);
            selectBox.getStyle().listStyle.selection.setRightWidth(10);
            selectBox.getStyle().listStyle.selection.setLeftWidth(20);
            selectBox.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println(selectBox.getSelected());
                }
            });
            selectBox.setItems("Android1", "Windows1 long text in item", "Linux1", "OSX1", "Android2", "Windows2", "Linux2", "OSX2",
                    "Android3", "Windows3", "Linux3", "OSX3", "Android4", "Windows4", "Linux4", "OSX4", "Android5", "Windows5", "Linux5",
                    "OSX5", "Android6", "Windows6", "Linux6", "OSX6", "Android7", "Windows7", "Linux7", "OSX7");
            selectBox.setSelected("Linux6");
            Image imageActor = new Image(image2);
            ScrollPane scrollPane = new ScrollPane(imageActor);
            List list = new List(skin);
            list.setItems(listEntries);
            list.getSelection().setMultiple(true);
            list.getSelection().setRequired(false);
            // list.getSelection().setToggle(true);
            ScrollPane scrollPane2 = new ScrollPane(list, skin);
            scrollPane2.setFlickScroll(false);
            Label minSizeLabel = new Label("minWidth cell", skin); // demos SplitPane respecting widget's minWidth
            Table rightSideTable = new Table(skin);
            rightSideTable.add(minSizeLabel).growX().row();
            rightSideTable.add(scrollPane2).grow();
            SplitPane splitPane = new SplitPane(scrollPane, rightSideTable, false, skin, "default-horizontal");
            fpsLabel = new Label("fps:", skin);

            // configures an example of a TextField in password mode.
            final Label passwordLabel = new Label("Textfield in password mode: ", skin);
            final TextField passwordTextField = new TextField("", skin);
            passwordTextField.setMessageText("password");
            passwordTextField.setPasswordCharacter('*');
            passwordTextField.setPasswordMode(true);

            buttonMulti.addListener(new TextTooltip(
                    "This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip!",
                    skin));
            Table tooltipTable = new Table(skin);
            tooltipTable.pad(10).background("default-round");
            tooltipTable.add(new TextButton("Fancy tooltip!", skin));
            imgButton.addListener(new Tooltip(tooltipTable));

            // window.debug();
            Window window = new Window("Dialog", skin);
            window.getTitleTable().add(new TextButton("X", skin)).height(window.getPadTop());
            window.setPosition(0, 0);
            window.defaults().spaceBottom(10);
            window.row().fill().expandX();
            window.add(iconButton);
            window.add(buttonMulti);
            window.add(imgButton);
            window.add(imgToggleButton);
            window.row();
            window.add(adsButton1);
            window.add(adsButton2);
            window.add(adsButton3);
            window.add(adsButton4);
            window.add(adsButton5);
            window.row();
            window.add(checkBox);
            window.add(slider).minWidth(100).fillX().colspan(3);
            window.row();
            window.add(selectBox).maxWidth(100);
            window.add(textfield).minWidth(100).expandX().fillX().colspan(3);
            window.row();
            window.add(splitPane).fill().expand().colspan(4).maxHeight(200);
            window.row();
            window.add(passwordLabel).colspan(2);
            window.add(passwordTextField).minWidth(100).expandX().fillX().colspan(2);
            window.row();
            window.add(fpsLabel).colspan(4);
            window.pack();

            // stage.addActor(new Button("Behind Window", skin));
            stage.addActor(window);

            textfield.setTextFieldListener(new TextField.TextFieldListener() {
                public void keyTyped(TextField textField, char key) {
                    if (key == '\n') textField.getOnscreenKeyboard().show(false);
                }
            });

            slider.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.log("UITest", "slider: " + slider.getValue());
                }
            });

            iconButton.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    new Dialog("Some Dialog", skin, "dialog") {
                        protected void result(Object object) {
                            System.out.println("Chosen: " + object);
                        }
                    }.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Input.Keys.ENTER, true)
                            .key(Input.Keys.ESCAPE, false).show(stage);
                }
            });

            checkBox.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.graphics.setContinuousRendering(checkBox.isChecked());
                }
            });
        }

        @Override
        public void render() {

            fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }

        @Override
        public void resize(int width, int height) {
            stage.getViewport().update(width, height, true);
        }

        @Override
        public void dispose() {
            stage.dispose();
            skin.dispose();
            texture1.dispose();
            texture2.dispose();
        }
    }

}


