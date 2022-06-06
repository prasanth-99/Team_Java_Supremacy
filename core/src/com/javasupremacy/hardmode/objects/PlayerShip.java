
package com.javasupremacy.hardmode.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.javasupremacy.hardmode.utils.Constant;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PlayerShip implements Controllable{

    public float movementSpeed;

    private boolean isSlow, isthrow;

    private Texture spaceship, manmode, ghostmode;
    public Rectangle hitbox, manbox, ghostbox;

    private float shootInterval = 0.2f;
    private float bombInterval = 2.5f;
    private float shootTimestamp = 0;
    private float levelTimestep = 0;

    private float slowMultiplier=0.5f;

    private List<PlayerBullet> bulletList;
    private List<PlayerSpecialBomb> bomblist;
    private Sound fireSound;
    private Sound bombSound;
    private int powerLevel;
    private List<PowerUp> powerUps;
    private double type1UpperBound, type1LowerBound;


    public PlayerShip(List<PlayerBullet> bulletList, List<PlayerSpecialBomb> bomblist, List<PowerUp> powerUps, Object object) {
        fireSound= Gdx.audio.newSound(Gdx.files.internal("arcade.ogg"));
        bombSound= Gdx.audio.newSound(Gdx.files.internal("bomb.ogg"));

        this.movementSpeed = 4f;
        float width = 20;
        float height = 40;
        float x = (Constant.WINDOW_WIDTH - width) / 2;
        float y = 50;
        this.hitbox = new Rectangle(x, y, width, height);
        this.manbox = this.hitbox;
        this.ghostbox = new Rectangle(hitbox.x, hitbox.y, 40, 60);
        this.manmode = new Texture("man.png");
        this.ghostmode = new Texture("ghost.png");
        this.spaceship = this.manmode;
        this.bulletList = bulletList;
        this.bomblist = bomblist;
        this.isthrow = false;
        this.powerLevel = 0;
        this.powerUps = powerUps;
        this.type1UpperBound = 0.8 * ((double)object);
        this.type1LowerBound = 0.5 * ((double)object);
    }

    /**
     *
     * @return true if playership is firing
     */

    public void update(float deltaTime) {
        shootTimestamp += deltaTime;
    }

    @Override
    public void increasePower(int i)
    {
        levelTimestep = 0;
        if(powerLevel < 3)
            powerLevel++;
    }

    /**
     * switch slow mode
     * @param isSlow
     */
    @Override
    public void slowMode(boolean isSlow) {
        this.isSlow = isSlow;
    }

    @Override
    public void moveUp() {
        hitbox.y = Math.min(hitbox.y + this.getSpeed(), Constant.WINDOW_HEIGHT- hitbox.height);
        manbox.y = hitbox.y;
        ghostbox.y = hitbox.y;
    }

    @Override
    public void moveDown() {
        hitbox.y = Math.max(hitbox.y - this.getSpeed(), 0);
        manbox.y = hitbox.y;
        ghostbox.y = hitbox.y;
    }

    @Override
    public void moveLeft() {
        hitbox.x = Math.max(hitbox.x - this.getSpeed(), 0);
        manbox.x = hitbox.x;
        ghostbox.x = hitbox.x;
    }

    @Override
    public void moveRight() {
        hitbox.x = Math.min(hitbox.x + this.getSpeed(), Constant.WINDOW_WIDTH - hitbox.width);
        manbox.x = hitbox.x;
        ghostbox.x = hitbox.x;
    }

    @Override
    public void fire() {
        if(powerLevel<1) {
            if (shootTimestamp >= shootInterval) {
                shootTimestamp = 0;
                fireSound.play(0.05f);
                this.bulletList.add(new PlayerBullet.Builder(new Texture("bulletBeige.png"))
                        .hitbox(new Rectangle((hitbox.x + (hitbox.width / 2)) - 5, (hitbox.y + hitbox.height) - 5, 12, 26))
                        .speed(400)
                        .direction(0, 1)
                        .build());

            }
        }
        else if(powerLevel<2 && powerLevel>=1) {
            //2 bullets
            if (shootTimestamp >= shootInterval) {
                levelTimestep += shootTimestamp;
                shootTimestamp = 0;
                fireSound.play(0.05f);
                this.bulletList.add(new PlayerBullet.Builder(new Texture("bulletBeige.png"))
                        .hitbox(new Rectangle((hitbox.x + (hitbox.width / 2)) - 15, (hitbox.y + hitbox.height) - 5, 12, 26))
                        .speed(400)
                        .direction(0, 1)
                        .build());
                this.bulletList.add(new PlayerBullet.Builder(new Texture("bulletBeige.png"))
                        .hitbox(new Rectangle((hitbox.x + (hitbox.width / 2)) + 15, (hitbox.y + hitbox.height) - 5, 12, 26))
                        .speed(400)
                        .direction(0, 1)
                        .build());
            }
            if (levelTimestep >= 10f){
                powerLevel--;
                levelTimestep = 0;
            }
        }
        else
        {//for 3 bullets
            if (shootTimestamp >= shootInterval) {
                levelTimestep += shootTimestamp;
                shootTimestamp = 0;
                fireSound.play(0.05f);
                this.bulletList.add(new PlayerBullet.Builder(new Texture("bulletBeige.png"))
                        .hitbox(new Rectangle((hitbox.x + (hitbox.width / 2)) , (hitbox.y + hitbox.height) , 12, 26))
                        .speed(400)
                        .direction(0, 1)
                        .build());
                this.bulletList.add(new PlayerBullet.Builder(new Texture("bulletBeige.png"))
                        .hitbox(new Rectangle((hitbox.x + (hitbox.width / 2)) - 15, (hitbox.y + hitbox.height) + 15, 12, 26))
                        .speed(400)
                        .direction(0, 1)
                        .build());
                this.bulletList.add(new PlayerBullet.Builder(new Texture("bulletBeige.png"))
                        .hitbox(new Rectangle((hitbox.x + (hitbox.width / 2)) + 15, (hitbox.y + hitbox.height) + 15, 12, 26))
                        .speed(400)
                        .direction(0, 1)
                        .build());
                if (levelTimestep >= 5f){
                    powerLevel--;
                    levelTimestep = 0;
                }
            }
        }
    }

    @Override
    public void getPowerUp(Rectangle hitbox) {
        double rand=Math.random();
        if(rand<type1UpperBound && rand>=type1LowerBound) {
            // 1-reward
            // 2--powerup
            // 3--nothing
            powerUps.add(new PowerUp.Builder(1).hitbox(hitbox.x,
                            hitbox.y,
                            30,
                            30)
                    .build());
        }
        else if(rand<type1LowerBound && rand>=0.2) {
            powerUps.add(new PowerUp.Builder(2).hitbox(hitbox.x,
                            hitbox.y,
                            30,
                            30)
                    .build());
        }
    }

    @Override
    public void throwBomb() {
        if (shootTimestamp >= bombInterval) {
            shootTimestamp = 0;
            isthrow = true;
            bombSound.play(0.05f);
            this.bomblist.add(new PlayerSpecialBomb.Builder(new Texture("specialBomb.png"))
                    .hitbox(new Rectangle(hitbox.x - (hitbox.width/3), hitbox.y + hitbox.height, 30, 30))
                    .speed(250)
                    .direction(0, 1)
                    .build());
        }
    }

    public boolean getIsThrow(){ return this.isthrow;}

    public void setIsThrow(boolean isthrow) {this.isthrow=isthrow;}

    private float getSpeed() {
        float speed = this.movementSpeed;
        if (isSlow) {
            speed *= this.slowMultiplier;
        }
        return speed;
    }

    public void draw(Batch batch, float deltaTime) {
        update(deltaTime);
        batch.draw(spaceship, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public boolean overlaps(Rectangle other) {
        return this.hitbox.overlaps(other);
    }

    public void changeMode(boolean isCheating){
        if(isCheating){
            this.hitbox = this.ghostbox;
            this.spaceship = this.ghostmode;
        }
        else{
            this.hitbox = this.manbox;
            this.spaceship = this.manmode;
        }
    }
}

