package com.javasupremacy.hardmode.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.movement.Movement;
import com.javasupremacy.hardmode.systems.ScoreSystem;
import com.javasupremacy.hardmode.wrap.LaserWrapper;

public class EnemyShip extends Enemy {

    public EnemyShip(Builder builder) {
        super();
        this.hp = builder.hp;
        this.hitbox = builder.hitbox;
        this.score = builder.score;
        this.shipTexture = builder.texture;
        this.movement = builder.movement;
        this.laserWrapper = builder.laserWrapper;
        this.isFinalBoss = builder.isFinalBoss;
    }

    public void die(ScoreSystem ss) {
        ss.updateScore(this.score);
        // Final boss die ends game
        if (this.isFinalBoss) {
            ss.updateEnd(true);
        }
    }

    public static class Builder {
        private int hp;
        private int score;
        private Texture texture;
        private Rectangle hitbox;
        private Movement movement;
        private LaserWrapper laserWrapper;
        private boolean isFinalBoss;

        public Builder() {

        }

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder texture(String filename) {
            this.texture = new Texture(filename);
            return this;
        }

        public Builder hitbox(int x, int y, int width, int height) {
            this.hitbox = new Rectangle(x, y, width, height);
            return this;
        }

        public Builder movement(String movement) {
            try {
                Class cls = Class.forName("com.javasupremacy.hardmode.movement." + movement);
                this.movement = (Movement) cls.getConstructor().newInstance();
            } catch (Throwable e) {
                System.err.println(e);
            } finally {
                return this;
            }
        }

        public Builder laserWrapper(LaserWrapper laserWrapper) {
            this.laserWrapper = laserWrapper;
            return this;
        }

        public Builder isFinalBoss(boolean isFinalBoss) {
            this.isFinalBoss = isFinalBoss;
            return this;
        }

        public EnemyShip build() {
            return new EnemyShip(this);
        }
    }
}
