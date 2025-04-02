package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighscoreManager {
    public static final int MAX_SCORES = 5; // Top 5-lista
    private Preferences prefs;

    public HighscoreManager() {
        prefs = Gdx.app.getPreferences("Group1HighscorePrefs");
    }

    public static class ScoreEntry {
        String name;
        int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public void saveHighscore(String playerName, int score) {
        ArrayList<ScoreEntry> highScores = getHighscores();
        highScores.add(new ScoreEntry(playerName, score));

        Collections.sort(highScores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry o1, ScoreEntry o2) {
                return Integer.compare(o2.score, o1.score); // Största först
            }
        });

        if (highScores.size() > MAX_SCORES) {
            highScores.remove(highScores.size() - 1);
        }

        for (int i = 0; i < highScores.size(); i++) {
            prefs.putString("name" + i, highScores.get(i).name);
            prefs.putInteger("score" + i, highScores.get(i).score);
        }

        prefs.flush();
    }

    public ArrayList<ScoreEntry> getHighscores() {
        ArrayList<ScoreEntry> highScores = new ArrayList<>();

        for (int i = 0; i < MAX_SCORES; i++) {
            String name = prefs.getString("name" + i, ""); // Hämta namn
            int score = prefs.getInteger("score" + i, 0); // Hämta poäng

            if (!name.equals("")) { // Undvik tomma poster
                highScores.add(new ScoreEntry(name, score));
            }
        }
        return highScores;
    }

    public int getBestScore() {
        return getHighscores().isEmpty() ? 0 : getHighscores().getFirst().score;
    }

    public int getLowestHighscore() {
        return getHighscores().isEmpty() ? 0 : getHighscores().getLast().score;
    }

    public void clearHighscores() {
        prefs.clear();
        prefs.flush();
    }
}

