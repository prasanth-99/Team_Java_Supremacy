package com.javasupremacy.hardmode.utils;

import com.badlogic.gdx.Gdx;
import com.javasupremacy.hardmode.objects.Controllable;
import com.javasupremacy.hardmode.objects.PlayerShip;
import com.javasupremacy.hardmode.systems.ScoreSystem;
import com.javasupremacy.hardmode.screens.OptionScreen;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand {
    List<Controllable> subscribers;
    private ScoreSystem checkBombs;
    public int inputType = OptionScreen.keyBind;

    public PlayerCommand() {
        subscribers = new ArrayList<>();
    }

    public void add(Controllable subscriber) {
        subscribers.add(subscriber);
    }

    public void setCheckBombs(ScoreSystem checkBombs){this.checkBombs = checkBombs;}

    public void remove(Controllable subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Listen for input command and make corresponding calls
     */
    public void run() {
        for (Controllable sub : subscribers) {
            // slow mode by hold
            sub.slowMode(Gdx.input.isKeyPressed(Constant.SLOW_MODE));
            if(inputType==0) {
                // Movements
                if (Gdx.input.isKeyPressed(Constant.UP)) {
                    sub.moveUp();
                }
                if (Gdx.input.isKeyPressed(Constant.DOWN)) {
                    sub.moveDown();
                }
                if (Gdx.input.isKeyPressed(Constant.LEFT)) {
                    sub.moveLeft();
                }
                if (Gdx.input.isKeyPressed(Constant.RIGHT)) {
                    sub.moveRight();
                }
            }
            else {
                if (Gdx.input.isKeyPressed(Constant.W)) {
                    sub.moveUp();
                }
                if (Gdx.input.isKeyPressed(Constant.S)) {
                    sub.moveDown();
                }
                if (Gdx.input.isKeyPressed(Constant.A)) {
                    sub.moveLeft();
                }
                if (Gdx.input.isKeyPressed(Constant.D)) {
                    sub.moveRight();
                }
            }
            // Fire
            if (Gdx.input.isKeyPressed(Constant.FIRE)) {
                sub.fire();
            }
            //Bomb
            if (Gdx.input.isKeyPressed(Constant.BOMB) && checkBombs.getBombs()>0) {
                sub.throwBomb();
                if(sub.getIsThrow()) {
                    checkBombs.updateBombs(1);
                    sub.setIsThrow(false);
                }
            }
        }
    }
}
