package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Game;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private FirstScreen firstScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    private boolean firstRound = true;
    private int roundScore;


    @Override
    public void create() {
        setScreen(new FirstScreen(this));
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
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
    public void goToGameOverScreen() { setScreen(gameOverScreen); }
}
