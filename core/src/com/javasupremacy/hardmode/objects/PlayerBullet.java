package com.javasupremacy.hardmode.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.utils.Constant;

public class PlayerBullet {
    // position and dimensions
    //float xPosition, yPosition, width, height;
    public Rectangle hitbox;

    // laser physical characteristics
    private float movementSpeed;
    // direction between 0 - 1
    private float xDirection; // right positive, left negative
    private float yDirection; // up positive, down negative

    private float acceleration;

    // graphics
    private Texture textureReg;

    public PlayerBullet(Builder builder) {
        this.hitbox = builder.hitbox;
        this.movementSpeed = builder.movementSpeed;
        this.xDirection = builder.xDirection;
        this.yDirection = builder.yDirection;
        this.acceleration = builder.acceleration;
        this.textureReg = builder.textureReg;
    }

    public boolean canRemove() {
        return hitbox.x < 0 || hitbox.x > Constant.WINDOW_WIDTH
                || hitbox.y > Constant.WINDOW_HEIGHT || hitbox.y < 0;
    }

    public void move(float deltaTime) {
        hitbox.x += movementSpeed * xDirection * deltaTime;
        hitbox.y += movementSpeed * yDirection * deltaTime;
        movementSpeed = movementSpeed * acceleration;
    }

    public void draw(Batch batch) {
        batch.draw(textureReg, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }


    public static class Builder {
        private Rectangle hitbox;
        private float movementSpeed;
        private float xDirection;
        private float yDirection;
        private float acceleration;
        private Texture textureReg;

        public Builder(Texture texture) {
            this.textureReg = texture;
            xDirection = 0;
            yDirection = 1;
            acceleration = 1;
        }

        public Builder hitbox(Rectangle hitbox) {
            this.hitbox = hitbox;
            return this;
        }

        public Builder hitbox(float x, float y, float width, float height) {
            this.hitbox = new Rectangle(x, y, width, height);
            return this;
        }

        public Builder direction(float x, float y) {
            this.xDirection = x;
            this.yDirection = y;
            return this;
        }

        public Builder speed(float speed) {
            this.movementSpeed = speed;
            return this;
        }

        public Builder acceleration(float acc) {
            this.acceleration = acc;
            return this;
        }

        public PlayerBullet build() {
            return new PlayerBullet(this);
        }
    }
}
