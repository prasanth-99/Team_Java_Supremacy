package com.javasupremacy.hardmode.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.javasupremacy.hardmode.observer.Observer;
import com.javasupremacy.hardmode.systems.GameSystem;
import com.javasupremacy.hardmode.systems.ScoreSystem;
import com.javasupremacy.hardmode.utils.Constant;


public class BackgroundScreen extends Observer {
    private final Camera cameraBackground;

    private final Viewport viewportBackground;
    private final Texture background;
    private final Texture heart, infinity;
    private final Texture bomb;
    private final BitmapFont font0, font1;
    private SpriteBatch sbatch;
    private String mode;
    private int hearCount, score, bombCount;
    private boolean isCheating;
    private GameSystem observer;
    private final TextButton cheatingButton;
    private final Skin skin;
    private final Stage stage;
    private int hpYposition;

    public BackgroundScreen(ScoreSystem subject) {
        this.subject = subject;
        this.cameraBackground = new OrthographicCamera();
        ((OrthographicCamera) cameraBackground).setToOrtho(false, Constant.EXT_WINDOW_WIDTH, Constant.EXT_WINDOW_HEIGHT);
        this.viewportBackground = new StretchViewport(Constant.EXT_WINDOW_WIDTH, Constant.EXT_WINDOW_HEIGHT,cameraBackground);
        this.background = new Texture("mainScreen.jpg");
        this.heart = new Texture("heart.png");
        this.infinity = new Texture("infinity-symbol.png");
        this.bomb = new Texture("specialBomb.png");
        this.font0 = new BitmapFont();
        this.font1 = new BitmapFont();
        this.font0.setColor(0, 0, 0, 1);
        this.font0.getData().setScale(2f);
        this.font1.setColor(1,1,1,1);
        this.font1.getData().setScale(2f);
        this.mode = "Normal speed";
        this.subject.attachBackScreen(this);
        this.bombCount = this.subject.getBombs();
        this.hearCount = this.subject.getLives();
        this.score = this.subject.getScore();
        this.hpYposition = Constant.WINDOW_HEIGHT-(140+50*((this.bombCount-1)/6))-50;
        this.isCheating = false;
        this.skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
        this.stage = new Stage(this.viewportBackground);
        Gdx.input.setInputProcessor(stage);
        this.cheatingButton = new TextButton("Start Cheating", skin, "small");
        this.loadButtons();
        sbatch = new SpriteBatch();

    }

    public void attachCheatingObserver(GameSystem observer){
        this.observer = observer;
    }

    public void renderBackground(){
        sbatch.setProjectionMatrix(cameraBackground.combined);
        Gdx.gl.glViewport(0,0, Constant.EXT_WINDOW_WIDTH, Constant.EXT_WINDOW_HEIGHT);
        sbatch.begin();
        sbatch.draw(this.background, 0, 0, Constant.EXT_WINDOW_WIDTH, Constant.EXT_WINDOW_HEIGHT);
        this.checkMode();
        font0.draw(sbatch, mode, Constant.WINDOW_WIDTH+55, Constant.WINDOW_HEIGHT-20);
        //font1.draw(sbatch, "HiScore: "+String.format("%08d", HiScore), Constant.WINDOW_WIDTH+15, Constant.WINDOW_HEIGHT-60);
        font1.draw(sbatch, "Score: "+String.format("%08d", this.score), Constant.WINDOW_WIDTH+15, Constant.WINDOW_HEIGHT-60);
        font0.draw(sbatch, "HP: ", Constant.WINDOW_WIDTH+15, this.hpYposition);
        this.showBombs();
        this.showLives();
        sbatch.end();
        stage.act();
        stage.draw();
    }

    private void showBombs(){
        for(int i=0; i<this.bombCount; i++)
            sbatch.draw(bomb, Constant.WINDOW_WIDTH+15+((i%6)*50), Constant.WINDOW_HEIGHT-(140+50*(i/6)), 40,40);
    }

    private void showLives(){
        if(!isCheating)
            for(int i=0; i<this.hearCount; i++)
                sbatch.draw(heart, Constant.WINDOW_WIDTH+15+((i%6)*50), this.hpYposition-(70+50*(i/6)), 40,40);
        else
            sbatch.draw(infinity, Constant.WINDOW_WIDTH+100, this.hpYposition-35, 60,40);
    }

    private void checkMode(){
        if (Gdx.input.isKeyPressed(Constant.SLOW_MODE))
            this.mode = "Slow speed";
        else
            this.mode = "Normal speed";
    }

    private void loadButtons() {
        int sizeUnit = 50;
        cheatingButton.setSize(sizeUnit * 5, sizeUnit);
        cheatingButton.setPosition(Constant.WINDOW_WIDTH+35,100);
        cheatingButton.getLabel().setFontScale(1.2f, 1.2f);
        cheatingButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(!isCheating) {
                    isCheating = true;
                    observer.updateCheating();
                }
                else {
                    isCheating = false;
                    observer.updateCheating();
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!isCheating)
                    cheatingButton.setText("Stop Cheating");
                else
                    cheatingButton.setText("Start Cheating");
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                cheatingButton.getLabel().setFontScale(1.5f, 1.5f);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                cheatingButton.getLabel().setFontScale(1.2f, 1.2f);
            }
        });

        this.stage.addActor(cheatingButton);
    }



    @Override
    public void updateScore() {
        this.score = subject.getScore();
    }

    @Override
    public void updateLives() {
        this.hearCount = subject.getLives();
    }

    @Override
    public void updateBombs() { this.bombCount = subject.getBombs(); }

    public boolean getIsCheating(){ return this.isCheating; }
}
