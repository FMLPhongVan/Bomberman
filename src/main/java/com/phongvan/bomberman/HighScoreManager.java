package com.phongvan.bomberman;

import com.phongvan.bomberman.gui.InGameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import javax.imageio.IIOException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class HighScoreManager {
    private int saveScore = 0;
    private static HighScoreManager instance = null;
    private List<String> scoresList = new ArrayList<>();

    public final String USER_APP_DIR = System.getProperty("user.home").concat("\\Documents\\Bomberman");
    public final String SCORES_FILE_DIR = USER_APP_DIR.concat("\\scores.txt");

    private HighScoreManager() {
        File userDir = new File(USER_APP_DIR);
        if (userDir.mkdir()) {
            File scoresFile = new File(SCORES_FILE_DIR);
            try {
                scoresFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }

        return instance;
    }

    private void sortScore() {
        scoresList.sort(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return -Integer.compare(Integer.parseInt(a.split("-")[1]), Integer.parseInt(b.split("-")[1]));
            }
        });
    }

    public void loadSavedScores() {
        try {
            InputStream input = new FileInputStream(SCORES_FILE_DIR);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String data;

            while ((data = reader.readLine()) != null) {
                scoresList.add(data);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sortScore();
        System.out.println(scoresList);
    }

    public boolean aNewHighScore(int scores) {
        System.out.println(scores);
        if (scores != 0 && scoresList.size() < 5) {
            saveScore = scores;
            return true;
        }

        sortScore();

        for (int i = 0; i < scoresList.size(); ++i) {
            int score = Integer.parseInt(scoresList.get(i).split("-")[1]);
            if (score < scores || (score == scores && i != scoresList.size() - 1)) {
                saveScore = scores;
                return true;
            }
        }

        return false;
    }

    public void saveHighScores() {
        scoresList.add(InGameController.getInstance().getName() + "-" + saveScore);
        sortScore();

        System.out.println(scoresList);

        for (int i = 5; i < scoresList.size(); ++i) {
            scoresList.remove(i);
        }
    }

    public void getHighScores(ListView<String> storyModeHighScore) {
        ObservableList<String> data = FXCollections.observableArrayList();
        sortScore();

        data.addAll(scoresList);
        storyModeHighScore.setItems(data);
    }

    public void printScoreData() {
        sortScore();
        try {
            FileOutputStream outputStream = new FileOutputStream(SCORES_FILE_DIR);
            PrintStream output = new PrintStream(outputStream, true, "UTF-8");
            for (int i = 0; i < scoresList.size(); ++i) {
                output.println(scoresList.get(i));
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
