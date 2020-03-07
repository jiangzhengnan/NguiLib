package com.ng.ui.test;

import android.graphics.Color;

import java.util.Random;

/**
 * 描述:  粒子属性
 *
 * @author Jzn
 * @date 2020-03-06
 */
public class Lizi {
    //自身属性
    //生命-可刷新次数
    public int HP = 100;
    //大小 1-2
    public int size;
    //color
    public int color;
    //透明度
    public int alpha;
    //速度
    public float speed;

    //其他属性
    //出生方向 0左 1右
    public int side;
    //当前位置
    public int x;
    public int y;
    //上升节点  0~width
    public int upState;
    //下降节点  0~height
    public int downState;
    public float maxW;
    public float maxH;

    public int state;
    public static final int STATE_MOVE = 1;
    public static final int STATE_UP = 2;
    public static final int STATE_DOWN = 3;


    public Lizi(float width, float height) {

        maxW = width;
        maxH = height;
        Random random = new Random();
        size = nextInt(random, 1, 3);
        color = Color.BLUE;
        alpha = nextInt(random, 20, 150);
        speed = nextInt(random, 2, 5);

        side = random.nextBoolean() ? 1 : 0;
        if (side == 0) { //left
            x = 0;
            y = (int) maxH;
        } else {//right
            x = (int) maxW;
            y = (int) maxH;
        }
        upState = nextInt(random, 1, (int) maxW / 30) * 30;
        downState = nextInt(random, 10, (int) maxW);

        HP = (int) ((2 * downState * (1 - Math.abs(upState - maxW / 2) / maxW)) / speed);

    }

    public void refresh() {


        switch (getState()) {
            case STATE_MOVE:
                x += side == 0 ? speed : -speed;
                break;
            case STATE_UP:
                y -= speed;
                break;
            case STATE_DOWN:
                y += speed;
                break;
        }

        //扣血逻辑
        if (getState() != STATE_MOVE) {
            Random random = new Random();
            if (random.nextBoolean()) {
                HP -= speed;
            }
            if (HP <= 0) {
                isDead = true;
            }
        }

        if (y > maxH) {
            isDead = true;
        }
    }

    public int getState() {
        int state = STATE_MOVE;
        if (side == 0 && x >= upState) { //left -> right
            state = STATE_UP;
        } else if (side == 1 && x <= upState) {//right -> left
            state = STATE_UP;
        }

        // 1018 1018
        if (y <= downState) {
            canDown = true;
        }

        if (canDown) {
            state = STATE_DOWN;
        }

        return state;
    }

    private boolean canDown = false;

    private float nextFloat(Random random, float minValue, float maxValue) {
        if (minValue > maxValue) {
            return 0;
        }
        float num = maxValue - minValue;
        return minValue + (float) (random.nextDouble() * num);
    }

    private int nextInt(Random random, int minValue, int maxValue) {
        if (minValue > maxValue) {
            return 0;
        }
        int num = maxValue - minValue;
        if (num==0) {
            return maxValue;
        }
        int result = minValue + random.nextInt(num);
        return result;
    }

    //hp=0或者溢出边界则消亡
    public boolean isDead = false;


    @Override
    public String toString() {
        return "Lizi{" +
                "HP=" + HP +
                ", size=" + size +
                ", color=" + color +
                ", alpha=" + alpha +
                ", speed=" + speed +
                ", side=" + side +
                ", x=" + x +
                ", y=" + y +
                ", upState=" + upState +
                ", downState=" + downState +
                ", maxW=" + maxW +
                ", maxH=" + maxH +
                ", state=" + state +
                ", isDead=" + isDead +
                '}';
    }
}
