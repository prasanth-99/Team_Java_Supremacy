package com.javasupremacy.hardmode.systems;

import com.javasupremacy.hardmode.observer.Observer;
import com.javasupremacy.hardmode.utils.Constant;
import org.json.simple.JSONObject;

public class ScoreSystem{

    private int score;
    private int lives;
    private int bombs;
    private boolean win;
    private Observer backScreen;

    public ScoreSystem(JSONObject object) {
        score = 0;
        lives = ((Long) object.get("lives")).intValue();
        bombs = ((Long) object.get("bombs")).intValue();
        win = false;
    }

    public void attachBackScreen(Observer backScreen){
        this.backScreen = backScreen;
    }

    public void updateScore(int change) {
        score += change;
        this.backScreen.updateScore();
    }

    public void updateLives(int change) {
        lives += change;
        this.backScreen.updateLives();
    }

    public void updateBombs(int num) {
        bombs -= num;
        this.backScreen.updateBombs();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getBombs() {
        return bombs;
    }

    public void updateEnd(boolean win) {
        this.win = win;
    }

    public boolean canEnd() {
        return this.win || lives <= 0;
    }

    public boolean isWin() {
        return this.win;
    }
}
