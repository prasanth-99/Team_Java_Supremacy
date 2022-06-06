package com.javasupremacy.hardmode.strategy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.movement.Movement;
import com.javasupremacy.hardmode.objects.EnemyLaser;

import java.util.List;

public class CircleLaserStrategy implements LaserStrategy{
    // laser information
    float laserWidth, laserHeight;
    float timeBetweenShots;
    float timeSinceLastShot;
    float laserMovementSpeed;
    String filename;
    String movementClass;

    public CircleLaserStrategy() {
        laserWidth = 10f;
        laserHeight = 10f;
        timeBetweenShots = 0.3f;
        timeSinceLastShot = 0;
        laserMovementSpeed = 70f;
    }

    private boolean canFire() {
        return timeSinceLastShot >= timeBetweenShots;
    }

    @Override
    public void setLaserMovement(String movement) {
        this.movementClass = movement;
    }

    @Override
    public void setTexture(String filename) {
        this.filename = filename;
    }

    @Override
    public void fire(float deltaTime, Rectangle hitbox, List<EnemyLaser> list, List<EnemyLaser> heavyList) {
        timeSinceLastShot += deltaTime;
        try {
            if (canFire()) {
                for (int angle = 0; angle < 360; angle += 5) {
                    timeSinceLastShot = 0;
                    Class cls = Class.forName("com.javasupremacy.hardmode.movement." + movementClass);
                    Movement movement = (Movement) cls.getConstructor().newInstance();
                    movement.setDirection((float)Math.cos(Math.toRadians(angle)), (float)Math.sin(Math.toRadians(angle)));
                    movement.setSpeed(laserMovementSpeed);
                    movement.setAcceleration(1);
                    list.add(new EnemyLaser(filename,
                            new Rectangle(hitbox.x + (hitbox.width / 2) + hitbox.width * 0.5f * (float)Math.cos(Math.toRadians(angle)),
                                    hitbox.y + (hitbox.height / 2 ) + hitbox.height * 0.5f * (float)Math.sin(Math.toRadians(angle)),
                                    laserWidth,
                                    laserHeight),
                            movement));
                }
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
}
