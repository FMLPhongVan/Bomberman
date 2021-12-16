package com.phongvan.bomberman;

import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.entities.mobs.Mob;
import com.phongvan.bomberman.gui.MenuController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SoundHandler {
    private static SoundHandler instance;
    private final List<MediaPlayer> playingList = new ArrayList<>();
    private MediaPlayer moveUp;
    private MediaPlayer moveRight;
    private MediaPlayer moveDown;
    private MediaPlayer moveLeft;

    public static final String EFFECT_BOMB_PLANT = "effects/bomb_plant.wav",
                                EFFECT_DEATH = "effects/death.wav",
                                EFFECT_DOWN = "effects/down.wav",
                                EFFECT_EXPLOSION = "effects/explosion.wav",
                                EFFECT_EXPLOSION2 = "effects/explosion2.wav",
                                EFFECT_KICK = "effects/kick.wav",
                                EFFECT_LEFT = "effects/left.wav",
                                EFFECT_PAUSE = "effects/pause.wav",
                                EFFECT_POWER_UP = "effects/power_up.wav",
                                EFFECT_RIGHT = "effects/right2.wav",
                                EFFECT_UP = "effects/up.wav";
    public static final String STAGE_FIND_THE_PORTAL = "stages/find_the_portal.wav",
                                STAGE_BONUS_STAGE = "stages/bonus_stage.wav",
                                STAGE_GAME_OVER = "stages/game_over.wav",
                                STAGE_INVINCIBILITY_THEME = "stages/invincibility_theme.wav",
                                STAGE_JUST_DIED = "stages/just_died.wav",
                                STAGE_LEVEL_COMPLETE = "stages/level_complete.wav",
                                STAGE_LEVEL_START = "stages/level_start.wav",
                                STAGE_PRESENTATION = "stages/presentation.wav",
                                STAGE_STAGE = "stages/stage.wav",
                                STAGE_STAGE_THEME = "stages/stage_theme.wav",
                                STAGE_TITLE_SCREEN = "stages/title_screen.wav";


    private SoundHandler() {
        moveUp = new MediaPlayer(new Media(Bomberman.class.getResource("sounds/" + EFFECT_UP).toString()));
        moveRight = new MediaPlayer(new Media(Bomberman.class.getResource("sounds/" + EFFECT_RIGHT).toString()));
        moveDown = new MediaPlayer(new Media(Bomberman.class.getResource("sounds/" + EFFECT_DOWN).toString()));
        moveLeft = new MediaPlayer(new Media(Bomberman.class.getResource("sounds/" + EFFECT_LEFT).toString()));

        //moveUp.play();
        //moveRight.play();
        //moveDown.play();
       // moveLeft.play();
    }

    public static SoundHandler getInstance() {
        if (instance == null) {
            instance = new SoundHandler();
        }

        return instance;
    }

    public void addMedia(final String name, boolean repeat) {
        try {
            Media media = new Media(Bomberman.class.getResource("sounds/" + name).toString());
            MediaPlayer mp = new MediaPlayer(media);
            mp.setVolume(MenuController.getInstance().getVolumeSlider().getValue() / 100);
            mp.play();
            playingList.add(mp);

            if (repeat) {
                mp.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        mp.seek(Duration.ZERO);
                    }
                });
            }
        } catch (Exception e) {
            Logger.log(Logger.WARN, "SoundHandler", e.getMessage());
        }
    }

    public void play() {
        for (int i = 0; i < playingList.size(); ++i) {
            if (playingList.get(i).getStatus() == MediaPlayer.Status.STOPPED) {
                playingList.remove(i);
            }
        }
    }

    public void playMoveSound(int direction) {
        moveUp.pause();
        moveRight.pause();
        moveDown.pause();
        moveLeft.pause();
        switch (direction) {
            case Mob.UP_DIR -> moveUp.play();
            case Mob.RIGHT_DIR -> moveRight.play();
            case Mob.DOWN_DIR -> moveDown.play();
            case Mob.LEFT_DIR -> moveLeft.play();
        }
    }

    public void reset() {
        for (MediaPlayer mp : playingList) {
            mp.stop();
        }

        playingList.clear();
    }

    public void changeVolume(Number newVal) {
        System.out.println(newVal.doubleValue());
        for (MediaPlayer mp : playingList) {
            mp.setVolume(newVal.doubleValue() / 100.0);
        }
    }

    public void pause() {
        for (int i = 0; i < playingList.size(); ++i) {
            playingList.get(i).pause();
        }
    }

    public void unpause() {
        for (int i = 0; i < playingList.size(); ++i) {
            playingList.get(i).play();
        }
    }
}
