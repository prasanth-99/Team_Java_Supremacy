package com.javasupremacy.hardmode.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class PowerUp {
    public Rectangle hitbox;
    private int type;
    private Texture textureReg;

    private float movementSpeed;
    private float xDirection;
    private float yDirection;
    private float acceleration;
    /*1--+1 life
     * 2--+1 power
     * 3--+2 power
     */
    public PowerUp(Builder builder) {
        this.hitbox = builder.hitbox;
        this.type=builder.type;
        this.xDirection = builder.xDirection;
        this.yDirection = builder.yDirection;
        this.acceleration = builder.acceleration;
        this.movementSpeed = builder.movementSpeed;

        if(type==1)
            textureReg=new Texture("health.png");//health
        //else if(type ==2||type==3)
        else
            textureReg=new Texture("power-up.png");//powerup
    }

    public void move(float deltaTime) {
        hitbox.x += movementSpeed * xDirection * deltaTime;
        hitbox.y -= movementSpeed * yDirection * deltaTime;
        movementSpeed = movementSpeed * acceleration;
    }

    public void draw(Batch batch) {
        batch.draw(textureReg,  hitbox.x ,  hitbox.y , 30, 30);
    }

    public int getType() {
        return type;
    }

    public static class Builder {
        private Rectangle hitbox;
        //private Texture textureReg;
        private int type;

        private float movementSpeed;
        private float xDirection;
        private float yDirection;
        private float acceleration;

        public Builder(int type) {
            this.type = type;
            xDirection = 0;
            yDirection = 1;
            acceleration = 1;
            movementSpeed = 200f;

        }

        public PowerUp.Builder hitbox(Rectangle hitbox) {
            this.hitbox = hitbox;
            return this;
        }
        public PowerUp.Builder hitbox(float x, float y, float width, float height) {
            this.hitbox = new Rectangle(x, y, width, height);
            return this;
        }
        public PowerUp.Builder direction(float x, float y) {
            this.xDirection = x;
            this.yDirection = y;
            return this;
        }
        public PowerUp.Builder speed(float speed) {
            this.movementSpeed = speed;
            return this;
        }
        public PowerUp build() {
            return new PowerUp(this);
        }

    }

}
