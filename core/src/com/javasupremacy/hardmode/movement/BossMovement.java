package com.javasupremacy.hardmode.movement;

import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.tracks.Track;
import com.javasupremacy.hardmode.utils.Constant;

public class BossMovement implements Movement {
    public float timestamp;
    public float timeToTurn;
    public float switchTime;
    public float ttl;
    public float speed;
    public boolean ltr;

    public BossMovement() {
        timestamp = 0;
        timeToTurn = 5;
        switchTime = 15;
        ttl = 60;
        speed = 50;
        ltr = true;
    }

    @Override
    public void move(float deltaTime, Rectangle hitbox) {
        timestamp += deltaTime;
        //System.out.println(timestamp);
        if (timestamp > ttl) {
            speed = 500;
            hitbox.x += speed * deltaTime;
        } else {
            if (timestamp < timeToTurn) {
                hitbox.y -= speed * deltaTime;
            }
            else {
                if (timestamp > switchTime) {
                    this.speed = 300;
                }
                if (ltr) {
                    hitbox.x += speed * deltaTime;
                    if (timestamp > switchTime)
                        hitbox.y -= speed * deltaTime;
                } else {
                    hitbox.x -= speed * deltaTime;
                    if (timestamp > switchTime)
                        hitbox.y += speed * deltaTime;
                }

                if (hitbox.x >= Constant.WINDOW_WIDTH - hitbox.width) {
                    ltr = false;
                }
                if (hitbox.x <= 0) {
                    ltr = true;
                }
            }
        }
    }

    @Override
    public void setDirection(float xDirection, float yDirection) {

    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void setAcceleration(float acceleration) {

    }
}
