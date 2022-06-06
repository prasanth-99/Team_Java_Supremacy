package com.javasupremacy.hardmode.movement;

import com.badlogic.gdx.math.Rectangle;

public interface Movement {
    public void move(float deltaTime, Rectangle hitbox);
    public void setDirection(float xDirection, float yDirection);
    public void setSpeed(float speed);
    public void setAcceleration(float acceleration);
}
