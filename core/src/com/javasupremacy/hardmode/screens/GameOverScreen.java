package com.javasupremacy.hardmode.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javasupremacy.hardmode.MainGame;
import com.javasupremacy.hardmode.utils.Constant;

public class GameOverScreen implements Screen {
    private final MainGame game;
    private final Texture background;
    private final Stage stage;
    private final Skin skin;
    private final BitmapFont font0;
    private boolean showScore;
    private int score = 0;

    public GameOverScreen(MainGame game, boolean isWin, int score) {
        this.game = game;
        if(isWin) {
            background = new Texture("game_win.jpg");
            showScore = true;
        }
        else {
            background = new Texture("GameOver.jpg");
            showScore = false;
        }
        //winBackground = new Texture("game_win.jpg");
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
        Gdx.input.setInputProcessor(stage);
        this.font0 = new BitmapFont();
        this.font0.setColor(0, 0, 0, 1);
        this.font0.getData().setScale(4f);
        this.score = score;

        loadButtons();
    }

    private void loadButtons() {
        int sizeUnit = 60;
        final Screen self = this;

        final TextButton button3 = new TextButton("Return to the menu", skin, "small");
        button3.setSize(sizeUnit * 4, sizeUnit);
        button3.setPosition((Gdx.graphics.getWidth() - button3.getWidth()) / 2,150);
        button3.getLabel().setFontScale(1.2f, 1.2f);
        button3.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                backToMenu();
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

 //       stage.addActor(button1);
 //       stage.addActor(button2);
        stage.addActor(button3);
    }


    private void backToMenu() {
        this.dispose();
        stage.dispose();
        //Gdx.graphics.setWindowedMode(Constant.WINDOW_WIDTH,Constant.WINDOW_HEIGHT);
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background, 0, 0, Constant.EXT_WINDOW_WIDTH, Constant.EXT_WINDOW_HEIGHT);
        if(showScore)
            font0.draw(game.batch, "Score: "+String.format("%d", score), 250, 60);
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

