package com.phongvan.bomberman.player;

import com.phongvan.bomberman.Core;
import com.phongvan.bomberman.gui.InGameController;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class ControlHandler {
    private static ControlHandler instance = null;
    private final Set<KeyCode> eventSet = new HashSet<>();

    public static final int KEY_UP         = 0, /// W, UP Arrow    : move up
                            KEY_RIGHT      = 1, /// D, RIGHT Arrow : move right
                            KEY_DOWN       = 2, /// S, DOWN Arrow  : move down
                            KEY_LEFT       = 3, /// A, LEFT Arrow  : move left
                            KEY_PLANT_BOMB = 4, /// SPACE BAR      : plant the bomb
                            KEY_DETONATE   = 6, /// J              : detonate the oldest bomb
                            KEY_PAUSE      = 7, /// ESC, P         : pause
                            KEY_AI_MODE    = 8, /// L              : AI mode
                            KEY_UNKNOWN    =-1; ///

    private ControlHandler() {}

    public static ControlHandler getInstance() {
        if (instance == null) {
            instance = new ControlHandler();
        }

        return instance;
    }

    public void reset() {
        eventSet.clear();
    }

    public void add(KeyEvent event) {
        eventSet.add(event.getCode());
    }

    public void remove(KeyEvent event) {
        eventSet.remove(event.getCode());
    }

    private boolean isMovingUp() {
        return eventSet.contains(KeyCode.W) || eventSet.contains(KeyCode.UP);
    }

    private boolean isMovingRight() {
        return eventSet.contains(KeyCode.D) || eventSet.contains(KeyCode.RIGHT);
    }

    private boolean isMovingDown() {
        return eventSet.contains(KeyCode.S) || eventSet.contains(KeyCode.DOWN);
    }

    private boolean isMovingLeft() {
        return eventSet.contains(KeyCode.A) || eventSet.contains(KeyCode.LEFT);
    }

    private boolean isPaused() {
        return eventSet.contains(KeyCode.P) || eventSet.contains(KeyCode.ESCAPE);
    }

    public void handleInput() {
        Core.getInstance().getPlayer().resetVelocity();
        if (isMovingUp()) {
            Core.getInstance().getPlayer().changeVelY(false);
        }
        if (isMovingRight()) {
            Core.getInstance().getPlayer().changeVelX(true);
        }
        if (isMovingDown()) {
            Core.getInstance().getPlayer().changeVelY(true);
        }
        if (isMovingLeft()) {
            Core.getInstance().getPlayer().changeVelX(false);
        }
        if (eventSet.contains(KeyCode.SPACE)) {
            Core.getInstance().getPlayer().setPlantBomb(true);
        }
        if (eventSet.contains(KeyCode.J)) {
            Core.getInstance().getPlayer().detonateOldestBomb();
        }
        if (eventSet.contains(KeyCode.ESCAPE) || eventSet.contains(KeyCode.P)) {
            System.out.println(1);
            Core.getInstance().setPause(true);
            InGameController.getInstance().openPausePane();
        }
        if (eventSet.contains(KeyCode.L)) {
            Core.getInstance().toggleAIMode();
        }
    }

    public boolean isNavigateInput(int code) {
        return code <= 4 && code != -1;
    }
}
