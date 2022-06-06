package com.javasupremacy.hardmode.screens;

//This is my class where I wrote code
//This class consists of two execution one is scrolling back and character included

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.javasupremacy.hardmode.MainGame;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.javasupremacy.hardmode.systems.GameSystem;
import com.javasupremacy.hardmode.systems.ScoreSystem;
import com.javasupremacy.hardmode.utils.Constant;
import com.javasupremacy.hardmode.utils.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class GameScreen implements Screen {
    private BackgroundScreen backScreen;
    private final Camera cameraForeground;
    private final Viewport viewportForeground;
    private MainGame game;
    private int foregroundOffset;
    //ASTEROID
    private Texture asteroid;
    private int asteroidX, asteroidY;
    private int asteroidX2, asteroidY2;

    private Texture foreground;
    private SpriteBatch sbatch;

    private GameSystem gameSystem;
    private ScoreSystem scoreSystem;
    private Music bgm;

    public GameScreen(MainGame game) {
        JsonReader config = Constant.config;
        JSONObject playerConfigs = config.getPlayerAttribute();
        scoreSystem = new ScoreSystem(playerConfigs);
        sbatch = new SpriteBatch();
        this.backScreen = new BackgroundScreen(scoreSystem);
        this.cameraForeground = new OrthographicCamera();
        ((OrthographicCamera) cameraForeground).setToOrtho(false, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        this.viewportForeground = new FitViewport(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT,cameraForeground);

        foregroundOffset = 0;
        foreground = new Texture("back.jpg");

        //LOAD ASTEROID
        asteroid = new Texture("asteroid.png");
        setAsteroidPosition();

        //LOAD BGM AUDIO FILE AND INITIALIZE OBJECT
        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgm.ogg"));
        bgm.setVolume(0.05f);
        bgm.setLooping(true);
        bgm.play();

        gameSystem = new GameSystem(this.backScreen);
        //scoreSystem = new ScoreSystem();
        gameSystem.setScoreSystem(scoreSystem);
        //reader = new JsonReader("JSONFile.json");
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        this.backScreen.renderBackground();
        sbatch.setProjectionMatrix(cameraForeground.combined);
        Gdx.gl.glViewport(10,10, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);

        sbatch.begin();
        rollingForeground();
        rollAsteroid();
        gameSystem.render(sbatch, deltaTime);
        sbatch.end();

        if (gameSystem.canEnd()) {
            this.gameEnd();
        }
    }

    private void rollingForeground() {
        foregroundOffset++;
        if (foregroundOffset % Constant.WINDOW_HEIGHT == 0) {
            foregroundOffset = 0;
        }
        sbatch.draw(foreground, 0, -foregroundOffset, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        sbatch.draw(foreground, 0, -foregroundOffset + Constant.WINDOW_HEIGHT, Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
    }
    //ROLL ASTEROID
    private void rollAsteroid() {
        asteroidX--;
        asteroidY--;
        if (asteroidX < -100 || asteroidY < -100) {
            setAsteroidPosition();
        }
        sbatch.draw(asteroid, asteroidX, asteroidY, 100, 100);

        asteroidX2 -= 2;
        asteroidY2 -= 2;
        if (asteroidX2 < -50 || asteroidY2 < -50) {
            setAsteroid2Position();
        }
        sbatch.draw(asteroid, asteroidX2, asteroidY2, 50, 50);
    }

    //SET RANDOM ENTRY POINT FOR ASTEROID
    private void setAsteroidPosition() {
        asteroidX = Constant.WINDOW_WIDTH;
        asteroidY = MathUtils.random(Constant.EXT_WINDOW_WIDTH / 2, Constant.EXT_WINDOW_HEIGHT);
    }


    private void setAsteroid2Position() {
        asteroidX2 = Constant.WINDOW_WIDTH;
        asteroidY2 = MathUtils.random(Constant.EXT_WINDOW_WIDTH / 2, Constant.EXT_WINDOW_HEIGHT);
    }


    private void gameEnd() {
        this.dispose();
        game.setScreen(new GameOverScreen(game, scoreSystem.isWin(), scoreSystem.getScore()));
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
        bgm.stop();
        bgm.dispose();

    }
}
