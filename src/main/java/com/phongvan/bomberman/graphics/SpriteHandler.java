package com.phongvan.bomberman.graphics;

public class SpriteHandler {
    private static SpriteHandler instance;
    private final SpriteSheet basicSheet = new SpriteSheet("spritesheet.png");
    private final int[] basicPixels;

    public final Sprite[] bombermanUp = new Sprite[3];
    public final Sprite[] bombermanRight = new Sprite[3];
    public final Sprite[] bombermanDown = new Sprite[3];
    public final Sprite[] bombermanLeft = new Sprite[3];
    public final Sprite[] bombermanDead = new Sprite[3];

    public final Sprite[] balloomUpLeft = new Sprite[3];
    public final Sprite[] balloomDownRight = new Sprite[3];
    public final Sprite[] balloomDead = new Sprite[4];

    public final Sprite[] onealUpLeft = new Sprite[3];
    public final Sprite[] onealDownRight = new Sprite[3];
    public final Sprite[] onealDead = new Sprite[4];

    public final Sprite[] dollUpLeft = new Sprite[3];
    public final Sprite[] dollDownRight = new Sprite[3];
    public final Sprite[] dollDead = new Sprite[4];

    public final Sprite[] minvoUpLeft = new Sprite[3];
    public final Sprite[] minvoDownRight = new Sprite[3];
    public final Sprite[] minvoDead = new Sprite[4];

    public final Sprite[] kondoriaUpLeft = new Sprite[3];
    public final Sprite[] kondoriaDownRight = new Sprite[3];
    public final Sprite[] kondoriaDead = new Sprite[4];

    public final Sprite[] ovapiUpLeft = new Sprite[3];
    public final Sprite[] ovapiDownRight = new Sprite[3];
    public final Sprite[] ovapiDead = new Sprite[4];

    public final Sprite[] passUpLeft = new Sprite[3];
    public final Sprite[] passDownRight = new Sprite[3];
    public final Sprite[] passDead = new Sprite[4];

    public final Sprite[] pontanUpLeft = new Sprite[3];
    public final Sprite[] pontanDownRight = new Sprite[3];
    public final Sprite[] pontanDead = new Sprite[4];

    public final Sprite bombsPU = new Sprite(10, 0, 16, 16);
    public final Sprite flames = new Sprite(10, 1, 16, 16);
    public final Sprite speed = new Sprite(10, 2, 16, 16);
    public final Sprite wallPass = new Sprite(10, 3, 16, 16);
    public final Sprite detonator = new Sprite(10, 4, 16, 16);
    public final Sprite bombPass = new Sprite(10, 5, 16, 16);
    public final Sprite flamePass = new Sprite(10, 6, 16, 16);
    public final Sprite mystery = new Sprite(10, 7, 16, 16);

    public final Sprite[] bomb = new Sprite[3];
    public final Sprite[] explodeCenter = new Sprite[3];
    public final Sprite[] explodeHeadLeft = new Sprite[3];
    public final Sprite[] explodeHeadRight = new Sprite[3];
    public final Sprite[] explodeHeadUp = new Sprite[3];
    public final Sprite[] explodeHeadDown = new Sprite[3];
    public final Sprite[] explodeMiddleVertical = new Sprite[3];
    public final Sprite[] explodeMiddleHorizontal = new Sprite[3];

    public final Sprite portal = new Sprite(0, 4, 16, 16);
    public final Sprite wall = new Sprite(0, 5, 16, 16);
    public final Sprite grass = new Sprite(0, 6, 16, 16);
    public final Sprite[] bricks = new Sprite[4];

    private SpriteHandler() {
        basicPixels = basicSheet.getPixels();
    }

    public void loadAllSprite() {
        for (int i = 0; i < 3; ++i) {
            bombermanUp[i] = new Sprite(i, 0, 16, 16);
            bombermanRight[i] = new Sprite(i, 1, 16, 16);
            bombermanDown[i] = new Sprite(i, 2, 16, 16);
            bombermanLeft[i] = new Sprite(i, 3, 16, 16);
            bombermanDead[i] = new Sprite(2, i + 4, 16, 16);

            balloomUpLeft[i] = new Sprite(i, 9, 16, 16);
            balloomDownRight[i] = new Sprite(i, 10, 16, 16);
            balloomDead[0] = new Sprite(3, 9, 16, 16);
            balloomDead[i + 1] = new Sprite(i, 15, 16, 16);

            onealUpLeft[i] = new Sprite(i, 11, 16, 16);
            onealDownRight[i] = new Sprite(i, 12, 16, 16);
            onealDead[0] = new Sprite(3, 11, 16, 16);
            onealDead[i + 1] = new Sprite(i, 15, 16, 16);

            dollUpLeft[i] = new Sprite(i, 13, 16, 16);
            dollDownRight[i] = new Sprite(i, 14, 16, 16);
            dollDead[0] = new Sprite(3, 13, 16, 16);
            dollDead[i + 1] = new Sprite(i, 15, 16, 16);

            minvoUpLeft[i] = new Sprite(i + 5, 8, 16, 16);
            minvoDownRight[i] = new Sprite(i + 5, 9, 16, 16);
            minvoDead[0] = new Sprite(8, 8, 16, 16);
            minvoDead[i + 1] = new Sprite(i, 15, 16, 16);

            kondoriaUpLeft[i] = new Sprite(i + 5, 10, 16, 16);
            kondoriaDownRight[i] = new Sprite(i + 5, 11, 16, 16);
            kondoriaDead[0] = new Sprite(8, 10, 16, 16);
            kondoriaDead[i + 1] = new Sprite(i, 15, 16, 16);

            ovapiUpLeft[i] = new Sprite(i + 5, 6, 16, 16);
            ovapiDownRight[i] = new Sprite(i + 5, 7, 16, 16);
            ovapiDead[0] = new Sprite(8, 6, 16, 16);
            ovapiDead[i + 1] = new Sprite(i, 15, 16, 16);

            passUpLeft[i] = new Sprite(i + 5, 4, 16, 16);
            passDownRight[i] = new Sprite(i + 5, 5, 16, 16);
            passDead[0] = new Sprite(8, 4, 16, 16);
            passDead[i + 1] = new Sprite(i, 15, 16, 16);

            pontanUpLeft[i] = new Sprite(i + 5, 12, 16, 16);
            pontanDownRight[i] = new Sprite(i + 5, 13, 16, 16);
            pontanDead[0] = new Sprite(8, 12, 16, 16);
            pontanDead[i + 1] = new Sprite(i, 15, 16, 16);

            bomb[i] = new Sprite(3, i, 16, 16);
            explodeCenter[i] = new Sprite(i + 4, 0, 16, 16);
            explodeHeadUp[i] = new Sprite(4, i + 1, 16, 16);
            explodeHeadDown[i] = new Sprite(6, i + 1, 16, 16);
            explodeHeadLeft[i] = new Sprite(i + 7, 0, 16, 16);
            explodeHeadRight[i] = new Sprite(i + 7, 2, 16, 16);
            explodeMiddleVertical[i] = new Sprite(5, i + 1, 16, 16);
            explodeMiddleHorizontal[i] = new Sprite(i + 7, 1, 16, 16);

            bricks[0] = new Sprite(0, 7, 16, 16);
            bricks[i + 1] = new Sprite(i + 1, 7, 16, 16);
        }

        loadAllSpritePixels();
    }

    private void loadAllSpritePixels() {
        for (int i = 0; i < 3; ++i) {
            bombermanUp[i].loadPixels(basicPixels);
            bombermanRight[i].loadPixels(basicPixels);
            bombermanDown[i].loadPixels(basicPixels);
            bombermanLeft[i].loadPixels(basicPixels);
            bombermanDead[i].loadPixels(basicPixels);

            balloomUpLeft[i].loadPixels(basicPixels);
            balloomDownRight[i].loadPixels(basicPixels);
            balloomDead[0].loadPixels(basicPixels);
            balloomDead[i + 1].loadPixels(basicPixels);

            onealUpLeft[i].loadPixels(basicPixels);
            onealDownRight[i].loadPixels(basicPixels);
            onealDead[0].loadPixels(basicPixels);
            onealDead[i + 1].loadPixels(basicPixels);

            dollUpLeft[i].loadPixels(basicPixels);
            dollDownRight[i].loadPixels(basicPixels);
            dollDead[0].loadPixels(basicPixels);
            dollDead[i + 1].loadPixels(basicPixels);

            minvoUpLeft[i].loadPixels(basicPixels);
            minvoDownRight[i].loadPixels(basicPixels);
            minvoDead[0].loadPixels(basicPixels);
            minvoDead[i + 1].loadPixels(basicPixels);

            kondoriaUpLeft[i].loadPixels(basicPixels);
            kondoriaDownRight[i].loadPixels(basicPixels);
            kondoriaDead[0].loadPixels(basicPixels);
            kondoriaDead[i + 1].loadPixels(basicPixels);

            ovapiUpLeft[i].loadPixels(basicPixels);
            ovapiDownRight[i].loadPixels(basicPixels);
            ovapiDead[0].loadPixels(basicPixels);
            ovapiDead[i + 1].loadPixels(basicPixels);

            passUpLeft[i].loadPixels(basicPixels);
            passDownRight[i].loadPixels(basicPixels);
            passDead[0].loadPixels(basicPixels);
            passDead[i + 1].loadPixels(basicPixels);

            pontanUpLeft[i].loadPixels(basicPixels);
            pontanDownRight[i].loadPixels(basicPixels);
            pontanDead[0].loadPixels(basicPixels);
            pontanDead[i + 1].loadPixels(basicPixels);

            bomb[i].loadPixels(basicPixels);
            explodeCenter[i].loadPixels(basicPixels);
            explodeHeadUp[i].loadPixels(basicPixels);
            explodeHeadDown[i].loadPixels(basicPixels);
            explodeHeadLeft[i].loadPixels(basicPixels);
            explodeHeadRight[i].loadPixels(basicPixels);
            explodeMiddleVertical[i].loadPixels(basicPixels);
            explodeMiddleHorizontal[i].loadPixels(basicPixels);
            bricks[0].loadPixels(basicPixels);
            bricks[i + 1].loadPixels(basicPixels);
        }

        bombsPU.loadPixels(basicPixels);
        flames.loadPixels(basicPixels);
        speed.loadPixels(basicPixels);
        wallPass.loadPixels(basicPixels);
        detonator.loadPixels(basicPixels);
        bombPass.loadPixels(basicPixels);
        flamePass.loadPixels(basicPixels);
        mystery .loadPixels(basicPixels);

        portal.loadPixels(basicPixels);
        wall.loadPixels(basicPixels);
        grass.loadPixels(basicPixels);
    }

    public static SpriteHandler getInstance() {
        if (instance == null) {
            instance = new SpriteHandler();
        }

        return instance;
    }
}
