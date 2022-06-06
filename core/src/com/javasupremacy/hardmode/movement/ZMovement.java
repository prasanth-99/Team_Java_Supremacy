package com.javasupremacy.hardmode.movement;

import com.badlogic.gdx.math.Rectangle;

public class ZMovement implements Movement{
    public float timestamp;
    public float timeToTurn;
    public float speed;

    public ZMovement() {
        timestamp = 0;
        timeToTurn = 2;
        speed = 150;
    }

    @Override
    public void move(float deltaTime, Rectangle hitbox) {
        timestamp += deltaTime;
        if (timestamp < (timeToTurn*0.5))
            hitbox.y -= speed * deltaTime;
        else if (timestamp>=(timeToTurn*0.5) && timestamp<(timeToTurn*2))
            hitbox.x += speed * deltaTime;
        else if (timestamp>=(timeToTurn*2) && timestamp<(timeToTurn*2.5))
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
