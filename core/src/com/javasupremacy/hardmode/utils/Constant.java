package com.javasupremacy.hardmode.utils;

import com.badlogic.gdx.Input;
import com.javasupremacy.hardmode.objects.PlayerShip;

public class Constant {
    public static final int EXT_WINDOW_WIDTH = 836;
    public static final int EXT_WINDOW_HEIGHT  = 820;
    public static final int WINDOW_WIDTH = 536;
    public static final int WINDOW_HEIGHT = 800;
    public static final int GAME_LENGTH = 150; // 2:30

    //public static JsonReader reader = new JsonReader();
    //reader.initialContent();
    public static int UP = Input.Keys.UP;
    public static int DOWN = Input.Keys.DOWN;
    public static int LEFT = Input.Keys.LEFT;
    public static int RIGHT = Input.Keys.RIGHT;
    public static int W = Input.Keys.W;
    public static int A = Input.Keys.A;
    public static int S = Input.Keys.S;
    public static int D = Input.Keys.D;
    public static int FIRE = Input.Keys.SPACE;
    public static int BOMB = Input.Keys.B;
    public static int SLOW_MODE = Input.Keys.SHIFT_LEFT;
    public static int NUM_LIVES = 13;
    public static int NUM_BOMB = 5;
    public static double AWARD_PROB = 1.0;
    public static JsonReader config;

    public static PlayerShip playerShip;
}
