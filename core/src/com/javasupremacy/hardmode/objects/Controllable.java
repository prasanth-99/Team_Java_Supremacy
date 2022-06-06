package com.javasupremacy.hardmode.objects;

public interface Controllable {
    public void moveUp();
    public void moveDown();
    public void moveLeft();
    public void moveRight();
    public void fire();
    public void slowMode(boolean slow);
    public void throwBomb();
    public boolean getIsThrow();
    public void setIsThrow(boolean isThrow);
    public void increasePower(int i);
    public void getPowerUp(com.badlogic.gdx.math.Rectangle hitbox);
}
