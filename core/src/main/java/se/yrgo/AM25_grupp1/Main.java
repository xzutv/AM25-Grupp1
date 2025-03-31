package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private FirstScreen firstScreen;
    private GameScreen gameScreen;

    private boolean firstRound = true;
    private int roundScore;
    private int sessionHighscore;
    private int allTimeHighscore;
    private String highscoreFileName;

    @Override
    public void create() {
        setScreen(new FirstScreen(this));
        gameScreen = new GameScreen(this);
    }

    public int getSessionHighscore() {
        return sessionHighscore;
    }

    public void setSessionHighscore(int sessionHighscore) {
        this.sessionHighscore = sessionHighscore;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }

    public boolean isFirstRound() {
        return firstRound;
    }

    public void setFirstRound(boolean firstRound) {
        this.firstRound = firstRound;
    }

    public int getAllTimeHighscore() {
        return allTimeHighscore;
    }

    public void updateAllTimeHighscore(int sessionHighscore) throws IOException {
        highscoreFileName = "highscore.txt";
        Path filename = Path.of(highscoreFileName);

        if (!Files.exists(filename)) {
            try (BufferedWriter bw = Files.newBufferedWriter(filename)) {
                bw.write(Integer.toString(sessionHighscore));
            } catch (IOException e) {
                throw new RuntimeException("Error when writing to file: " + e.getMessage());
            }
        } else {
            try (BufferedReader br = Files.newBufferedReader(filename);
                BufferedWriter bw = Files.newBufferedWriter(filename, StandardOpenOption.WRITE)) {
                String line;
                while ((line = br.readLine()) != null) {
                    int oldAllTimeHighscore = Integer.parseInt(line.trim());
                    if (sessionHighscore > oldAllTimeHighscore) {
                        bw.write(Integer.toString(sessionHighscore));
                        allTimeHighscore = sessionHighscore;
                    } else {
                        allTimeHighscore = oldAllTimeHighscore;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error when reading from/writing to file: " + e.getMessage());
            }
        }
    }

    public void startGame() {
        setScreen(gameScreen);
    }
}
