package com.javasupremacy.hardmode.strategy;

import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.movement.Movement;
import com.javasupremacy.hardmode.objects.EnemyLaser;
import com.javasupremacy.hardmode.utils.Constant;
//import com.sun.org.apache.bcel.internal.Const;

import java.util.List;

public class StringLaserStrategy implements LaserStrategy{
    // laser information
    float laserWidth, laserHeight;
    float timeBetweenShots;
    float timeSinceLastShot;
    float laserMovementSpeed;
    float timeSwitch;
    boolean fireSwitch;
    String filename;
    String movementClass;

    public StringLaserStrategy() {
        laserWidth = 15f;
        laserHeight = 15f;
        timeBetweenShots = 0.03f;
        timeSinceLastShot = 0;
        laserMovementSpeed = 200f;
        fireSwitch = true;
    }

    @Override
    public void setLaserMovement(String movement) {
        this.movementClass = movement;
    }

    @Override
    public void setTexture(String filename) {
        this.filename = filename;
    }

    private boolean canFire() {
        if (timeSwitch >= 3) {
            fireSwitch = !fireSwitch;
            timeSwitch = 0;
        }
        return fireSwitch && timeSinceLastShot >= timeBetweenShots;
    }

    @Override
    public void fire(float deltaTime, Rectangle hitbox, List<EnemyLaser> list, List<EnemyLaser> heavyList) {
        timeSinceLastShot += deltaTime;
        timeSwitch += deltaTime;
        try {
            if (canFire()) {
                timeSinceLastShot = 0;
                float playerX = Constant.playerShip.hitbox.x + Constant.playerShip.hitbox.width / 2;
                float playerY = Constant.playerShip.hitbox.y + Constant.playerShip.hitbox.height / 2;
                float enemyX = hitbox.x + hitbox.width / 2;
                float enemyY = hitbox.y + hitbox.height / 2;
                float deltaX = playerX - enemyX;
                float deltaY = playerY - enemyY;
                double angle = Math.atan2(deltaY, deltaX) ;

                Class cls = Class.forName("com.javasupremacy.hardmode.movement." + movementClass);
                Movement movement = (Movement) cls.getConstructor().newInstance();
                movement.setDirection((float)Math.cos(angle), (float)Math.sin(angle));
                movement.setSpeed(100);
                movement.setAcceleration(1);
                list.add(new EnemyLaser(filename,
                        new Rectangle(hitbox.x + (hitbox.width / 2) ,
                                hitbox.y + (hitbox.height / 2 ),
                                laserWidth,
                                laserHeight),
                        movement));
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
}
