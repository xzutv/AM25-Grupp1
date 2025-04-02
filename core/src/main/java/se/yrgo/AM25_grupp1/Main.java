package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private FirstScreen firstScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    private boolean firstRound = true;
    private int roundScore;
    private boolean seeHighscore = false;
    private Music gameMusic;


    @Override
    public void create() {
        setScreen(new FirstScreen(this));
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound-music.mp3"));
        this.gameMusic.setLooping(true);
        this.gameMusic.setVolume(0.5f);
        this.gameMusic.play();
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
    public void goToGameOverScreen() {
        setScreen(gameOverScreen);
    }

    public void setSeeHighscore(boolean seeHighscore) {
        this.seeHighscore = seeHighscore;
    }

    public boolean isSeeHighscore() {
        return seeHighscore;
    }

    public void stopMusic() {
        this.gameMusic.stop();
    }
}
