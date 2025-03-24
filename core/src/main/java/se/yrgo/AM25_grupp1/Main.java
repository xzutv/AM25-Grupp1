package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private FirstScreen firstScreen;
    private GameScreen gameScreen;

    private boolean firstRound = true;
    private int roundScore;
    private int sessionHighscore;

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

    public void startGame() {
        setScreen(gameScreen);
    }
}
