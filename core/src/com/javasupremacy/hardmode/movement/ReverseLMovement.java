package com.javasupremacy.hardmode.movement;

import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.utils.Constant;

public class ReverseLMovement implements Movement{
    public float timestamp;
    public float timeToTurn;
    public float speed;

    public ReverseLMovement() {
        timestamp = 0;
        timeToTurn = 2;
        speed = 100;
    }

    @Override
    public void move(float deltaTime, Rectangle hitbox) {
        timestamp += deltaTime;
        if (timestamp < timeToTurn)
            hitbox.y -= speed * deltaTime;
        else
            hitbox.x -= speed * deltaTime;
    }

    @Override
    public void setDirection(float xDirection, float yDirection) {
        // DO NOTHING
    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void setAcceleration(float acceleration) {
        // DO NOTHING
    }
}
