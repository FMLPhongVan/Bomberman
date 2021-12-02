package com.phongvan.bomberman.player;

import com.phongvan.bomberman.Core;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class ControlHandler {
    private final Set<KeyCode> eventSet = new HashSet<>();

    public static final int KEY_UP         = 0, /// W, UP Arrow    : move up
                            KEY_RIGHT      = 1, /// D, RIGHT Arrow : move right
                            KEY_DOWN       = 2, /// S, DOWN Arrow  : move down
                            KEY_LEFT       = 3, /// A, LEFT Arrow  : move left
                            KEY_PLANT_BOMB = 4, /// SPACE BAR   : plant the bomb
                            KEY_PAUSE      = 5, /// ESC, P         : pause
                            KEY_UNKNOWN    =-1; ///

    public ControlHandler() {}

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
    }

    public boolean isNavigateInput(int code) {
        return code <= 4 && code != -1;
    }
}
