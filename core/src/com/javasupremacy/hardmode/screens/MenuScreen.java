package com.javasupremacy.hardmode.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javasupremacy.hardmode.MainGame;
import com.javasupremacy.hardmode.utils.Constant;

public class MenuScreen implements Screen {
    private final MainGame game;
    private final Texture background;
    private final Stage stage;
    private final Skin skin;

    public MenuScreen(MainGame game) {
        this.game = game;
        background = new Texture("menu2.jpg");
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
        Gdx.input.setInputProcessor(stage);
        loadButtons();
    }

    private void loadButtons() {
        int sizeUnit = 60;
        //final Screen self = this;
        // start button,
        final TextButton button1 = new TextButton("Start", skin, "small");
        button1.setSize(sizeUnit * 4, sizeUnit);
        button1.setPosition((Gdx.graphics.getWidth() - button1.getWidth()) / 2,350);
        button1.getLabel().setFontScale(1.2f, 1.2f);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //new Constant(2);
                Gdx.graphics.setWindowedMode(Constant.EXT_WINDOW_WIDTH,Constant.EXT_WINDOW_HEIGHT);
                ToLevelSelect();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button1.getLabel().setFontScale(1.5f, 1.5f);
            }
            //DOne with the changes here beed to focus
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button1.getLabel().setFontScale(1.2f, 1.2f);
            }
        });

        // options button
        final TextButton button2 = new TextButton("Options", skin, "small");
        button2.setSize(sizeUnit * 4, sizeUnit);
        button2.setPosition((Gdx.graphics.getWidth() - button2.getWidth()) / 2,250);
        button2.getLabel().setFontScale(1.2f, 1.2f);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                ToOption();
                //button2.setText("Options");
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {return true;}
            //button2.setText("In Progress");
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button2.getLabel().setFontScale(1.5f, 1.5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button2.getLabel().setFontScale(1.2f, 1.2f);
            }
        });

        // exit button
        final TextButton button3 = new TextButton("Exit", skin, "small");
        button3.setSize(sizeUnit * 4, sizeUnit);
        button3.setPosition((Gdx.graphics.getWidth() - button3.getWidth()) / 2,150);
        button3.getLabel().setFontScale(1.2f, 1.2f);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button3.getLabel().setFontScale(1.5f, 1.5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button3.getLabel().setFontScale(1.2f, 1.2f);
            }
        });

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
    }



    private void ToOption() {
        //System.out.println("go to option");
        this.dispose();
        stage.dispose();
        //System.out.println("go to option");
        game.setScreen(new OptionScreen(game));
    }
    private void ToLevelSelect(){
        //System.out.println("go to option");
        this.dispose();
        stage.dispose();
        //System.out.println("go to option");
        game.setScreen(new LevelScreen(game));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Constant.EXT_WINDOW_WIDTH, Constant.EXT_WINDOW_HEIGHT);
        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
