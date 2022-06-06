package com.javasupremacy.hardmode.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.javasupremacy.hardmode.factories.EnemyFactory;
import com.javasupremacy.hardmode.factories.EnemyShipFactory;
import com.javasupremacy.hardmode.objects.*;
import com.javasupremacy.hardmode.observer.CheatingObserver;
import com.javasupremacy.hardmode.screens.BackgroundScreen;
import com.javasupremacy.hardmode.utils.Constant;
import com.javasupremacy.hardmode.utils.JsonReader;
import com.javasupremacy.hardmode.utils.PlayerCommand;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameSystem extends CheatingObserver {
    private float timestamp;
    // Game Objects
    PlayerShip playerShip;
    private List<PlayerBullet> bullets;
    private List<PlayerSpecialBomb> specialBombs;
    private List<Enemy> enemyShipList;
    private List<EnemyLaser> enemyLaserList;
    private List<EnemyLaser> heavyLaserList;
    private List<PowerUp> powerUps;

    // Input command
    private PlayerCommand command;

    // Factories
    EnemyFactory enemyFactory;
    private Queue<Float> releaseTime;
    private Queue<JSONObject> toBeReleased;

    private ScoreSystem scoreSystem;
    private boolean isCheating;

    private int awaredCount;

    private boolean end = false;

    public GameSystem(BackgroundScreen subject) {
        this.subject = subject;
        this.subject.attachCheatingObserver(this);
        init();
    }

    public void init() {
        JsonReader config = Constant.config;
        timestamp = 0;
        awaredCount = 0;

        bullets = new ArrayList<>();
        specialBombs = new ArrayList<>();
        powerUps= new ArrayList<>();
        playerShip = new PlayerShip(bullets, specialBombs, powerUps, config.getPlayerAttribute().get("award-prob"));
        Constant.playerShip = playerShip; // Hack for tracking lasers
        enemyLaserList = new ArrayList<>();
        heavyLaserList = new ArrayList<>();
        enemyShipList = new ArrayList<>();

        command = new PlayerCommand();
        command.add(playerShip);

        enemyFactory = new EnemyShipFactory();
        releaseTime = new LinkedList<>();
        toBeReleased = new LinkedList<>();

        loadEnemyShips(config);
    }

    public void loadEnemyShips(JsonReader config) {

        JSONArray enemyConfigs = config.getEnemies();
        for (Object obj : enemyConfigs) {
            JSONObject enemyObj = (JSONObject) obj;
            float timestamp = ((Long)enemyObj.get("spawnTime")).floatValue();
            int count = ((Long)enemyObj.get("count")).intValue();
            for (int i = 0; i < count; i++) {
                toBeReleased.offer(enemyObj);
                releaseTime.offer(timestamp);
                timestamp += ((Long)enemyObj.get("interval")).floatValue();
            }
        }
    }

    public void setScoreSystem(ScoreSystem ss) {
        this.scoreSystem = ss;
        this.command.setCheckBombs(ss);
    }

    public void render(SpriteBatch sbatch, float deltaTime) {
        updateGame(deltaTime);
        renderEnemy(sbatch, deltaTime);
        renderEnemyLasers(sbatch, deltaTime);
        renderShip(sbatch, deltaTime);
        renderShipBullet(sbatch, deltaTime);
        renderShipBomb(sbatch, deltaTime);
        renderPowerUps(sbatch,deltaTime);
    }

    private void updateGame(float deltaTime) {
        timestamp += deltaTime;
        spawnEnemy();
        command.run();
        heavyLaserCollesion();
        detectCollesion();
        powerUpCollsion();
    }

    private void spawnEnemy() {
        if (releaseTime.size() > 0 && timestamp > releaseTime.peek()) {
            releaseTime.poll();
            Enemy enemy = enemyFactory.produce(toBeReleased.poll());
            enemyShipList.add(enemy);
        }
    }

    private void heavyLaserCollesion() {
        for (EnemyLaser heavy : heavyLaserList) {
            for (EnemyLaser regular : enemyLaserList) {
                if (heavy.overlaps(regular.hitbox)) {
                    regular.movement = heavy.movement;
                }
            }
        }
    }

    private void detectCollesion() {
        if(!isCheating) {playerCollision();}
        enemyCollision();
    }

    private void playerCollision() {
        List<EnemyLaser> removeList = new ArrayList<>();
        List<EnemyLaser> removeHeavyList = new ArrayList<>();
        for (EnemyLaser laser : enemyLaserList) {
            if (playerShip.overlaps(laser.hitbox)) {
                removeList.add(laser);
                scoreSystem.updateLives(-1);
            }
        }
        for (EnemyLaser heavy : heavyLaserList) {
            if (playerShip.overlaps(heavy.hitbox)){
                removeHeavyList.add(heavy);
                scoreSystem.updateLives(-2);
            }
        }
        enemyLaserList.removeAll(removeList);
        heavyLaserList.removeAll(removeHeavyList);
    }

    private void powerUpCollsion() {
        List<PowerUp> remPow = new ArrayList<>();
        for (PowerUp powerUp : powerUps) {
            if (playerShip.overlaps(powerUp.hitbox)) {
                remPow.add(powerUp);
                if(powerUp.getType()==1)
                    scoreSystem.updateLives(1);
                else if(powerUp.getType()==2)
                    playerShip.increasePower(1);
            }
        }
        powerUps.removeAll(remPow);
    }


    private void enemyCollision() {
        List<PlayerBullet> removeBulletList = new ArrayList<>();
        List<Enemy> removeEnemyList = new ArrayList<>();
        for (PlayerBullet bullet : bullets) {
            for (Enemy enemy : enemyShipList) {
                if (enemy.overlaps(bullet.hitbox)) {
                    awaredCount++;
                    enemy.hp -= 1;
                    removeBulletList.add(bullet);
                    if(awaredCount >= 5) {
                        playerShip.getPowerUp(enemy.hitbox);
                        awaredCount = 0;
                    }
                    if (enemy.hp <= 0) {
                        awaredCount = 0;
                        playerShip.getPowerUp(enemy.hitbox);
                        removeEnemyList.add(enemy);
                        enemy.die(scoreSystem);
                    }
                }
            }
        }
        bullets.removeAll(removeBulletList);
        enemyShipList.removeAll(removeEnemyList);
    }
    /**
     * Iterate through list and draw enemy
     * Also add lasers if can fire
     * Remove out of screen enemies
     * @param deltaTime
     */
    private void renderEnemy(SpriteBatch sbatch, float deltaTime) {
        List<Enemy> removeList = new ArrayList<>();
        for (Enemy enemy : enemyShipList) {
            enemy.draw(sbatch, deltaTime);
            enemy.fire(deltaTime, enemyLaserList, heavyLaserList);
            if (enemy.isOutOfBounds()) {
                if (enemy.isFinalBoss) {
                    this.end = true;
                }
                removeList.add(enemy);
            }
        }
        enemyShipList.removeAll(removeList);
    }

    /**
     * Render all lasers
     * Remove out of screen lasers
     * @param deltaTime
     */
    private void renderEnemyLasers(SpriteBatch sbatch, float deltaTime) {
        List<EnemyLaser> removeList1 = new ArrayList<>();
        for (EnemyLaser enemyLaser : enemyLaserList) {
            enemyLaser.move(deltaTime);
            enemyLaser.draw(sbatch);
            if (enemyLaser.canRemove()) {
                removeList1.add(enemyLaser);
            }
        }
        enemyLaserList.removeAll(removeList1);

        List<EnemyLaser> removeList2 = new ArrayList<>();
        for (EnemyLaser enemyLaser : heavyLaserList) {
            enemyLaser.move(deltaTime);
            enemyLaser.draw(sbatch);
            if (enemyLaser.canRemove()) {
                removeList2.add(enemyLaser);
            }
        }
        enemyLaserList.removeAll(removeList2);
    }

    /**
     * Render player's ship
     * @param deltaTime
     */
    private void renderShip(SpriteBatch sbatch, float deltaTime) {
        playerShip.draw(sbatch, deltaTime);
    }

    /**
     * Render player's bullet
     * @param deltaTime
     */
    private void renderShipBullet(SpriteBatch sbatch, float deltaTime) {
        List<PlayerBullet> removeList = new ArrayList<>();
        for (PlayerBullet bullet : bullets) {
            bullet.move(deltaTime);
            bullet.draw(sbatch);
            if (bullet.canRemove()) {
                removeList.add(bullet);
            }
        }
        bullets.removeAll(removeList);
    }

    private void renderPowerUps(SpriteBatch sbatch, float deltatime)
    {
        for(PowerUp powerUp: powerUps)
        {
            powerUp.move(deltatime);
            powerUp.draw(sbatch);
        }
    }


    private void renderShipBomb(SpriteBatch sbatch, float deltaTime) {
        List<PlayerSpecialBomb> removeList = new ArrayList<>();
        List<Enemy> removeEnemyList = new ArrayList<>();
        for (PlayerSpecialBomb bomb : specialBombs) {
            bomb.move(deltaTime);
            bomb.draw(sbatch);
            if (bomb.canRemove()) {
                removeList.add(bomb);
                enemyLaserList.clear();
                heavyLaserList.clear();
                for (Enemy enemy : enemyShipList){
                    enemy.hp -= 5;
                    if (enemy.hp <= 0) {
                        removeEnemyList.add(enemy);
                        enemy.die(scoreSystem);
                    }
                }
                enemyShipList.removeAll(removeEnemyList);
            }
        }
        specialBombs.removeAll(removeList);
    }

    public boolean canEnd() {
        return timestamp > Constant.GAME_LENGTH || scoreSystem.canEnd() || this.end;
    }

    @Override
    public void updateCheating() {
        this.isCheating = subject.getIsCheating();
        this.playerShip.changeMode(this.isCheating);
    }
}
