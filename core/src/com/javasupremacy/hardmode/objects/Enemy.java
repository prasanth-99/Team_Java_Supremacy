package com.javasupremacy.hardmode.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.movement.Movement;
import com.javasupremacy.hardmode.systems.ScoreSystem;
import com.javasupremacy.hardmode.utils.Constant;
import com.javasupremacy.hardmode.wrap.LaserWrapper;

import java.util.List;

abstract public class Enemy {
    public Rectangle hitbox;
    public Movement movement;
    public LaserWrapper laserWrapper;
    public int hp; // Need this later
    public int score;
    public boolean isFinalBoss;

    // Graphics
    Texture shipTexture;

    public Enemy() {
        isFinalBoss = false;
    }

    /**
     * Move current position based on Track, then render
     * @param batch
     * @param deltaTime
     */
    public void draw(Batch batch, float deltaTime){
        movement.move(deltaTime, this.hitbox);
        batch.draw(shipTexture, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public void fire(float deltaTime, List<EnemyLaser> lasers, List<EnemyLaser> heavys) {
        laserWrapper.fire(deltaTime, this.hitbox, lasers, heavys);
    }

    /**
     * Check if current object is out of screen
     * @return
     */
    public boolean isOutOfBounds() {
        if (this.hitbox.x + hitbox.width < 0 || this.hitbox.x > Constant.WINDOW_WIDTH || this.hitbox.y + hitbox.height < 0 || this.hitbox.y > Constant.WINDOW_HEIGHT) {
            return true;
        }
        return false;
    }

    public boolean overlaps(Rectangle other) {
        return this.hitbox.overlaps(other);
    }

    public abstract void die(ScoreSystem ss);
}

