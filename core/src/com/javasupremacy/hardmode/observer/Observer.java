package com.javasupremacy.hardmode.observer;

import com.javasupremacy.hardmode.systems.ScoreSystem;

public abstract class Observer {
    protected ScoreSystem subject;
    public abstract void updateScore();
    public abstract void updateLives();
    public abstract void updateBombs();
}
