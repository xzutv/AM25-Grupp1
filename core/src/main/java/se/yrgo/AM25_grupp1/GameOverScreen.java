package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class GameOverScreen implements Screen {
    private Main main;
    private HighscoreManager highscoreManager;
    private FitViewport viewport;
    private Texture backgroundTexture;
    private Texture gameOverText;
    private Texture newHighscoreText;
    private Texture returnText;
    private SpriteBatch spriteBatch;
    private SpriteBatch batch;
    private BitmapFont bigFont;
    private BitmapFont smallFont;

    private String playerName = "";
    private boolean isEnteringName = false;
    private boolean hasEnteredName = false;
    private boolean newHighscore = false;
    private Sound highscoreSound;
    private boolean highscoreSoundPlayed = false;

    private float width;
    private float height;

    public GameOverScreen(Main main) {
        this.main = main;
        this.highscoreManager = new HighscoreManager();
        this.viewport = new FitViewport(16, 10);
        this.backgroundTexture = new Texture("backgrounds/background-gameover.png");
        this.gameOverText = new Texture("text/text-gameover-restart.png");
        this.newHighscoreText = new Texture("text/text-gameover-newhighscore.png");
        this.returnText = new Texture("text/text-gameover-return.png");
        this.spriteBatch = new SpriteBatch();
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        this.batch = new SpriteBatch();
        this.bigFont = new BitmapFont();

        final Color smallFontColor = Color.BLACK;
        final Color bigFontColor = Color.WHITE;
        this.bigFont.setColor(bigFontColor);
        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.bigFont.getData().setScale(width / 300);

        this.smallFont = new BitmapFont();
        this.smallFont.setColor(smallFontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(width / 400);

        this.highscoreSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound-new-highscore.wav"));
    }

    @Override
    public void dispose() {
        bigFont.dispose();
        smallFont.dispose();
        batch.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        main.playGameOverMusic();
        draw();
        batch.begin();

        // Player just wants to see highscore from the firstScreen
        if (main.isSeeHighscore()) {
            batch.draw(returnText, 0, 0, width, height);
            printHighscores();
        }

        if (((hasEnteredName || (main.getRoundScore() <= highscoreManager.getLowestHighscore() && highscoreManager.getHighscores().size() >= 5 ) || main.getRoundScore() == 0)) && !main.isSeeHighscore()) {
            batch.draw(gameOverText, 0, 0, width, height);
            newHighscore = false;
            printScore();
            printHighscores();
        }

        if (main.getRoundScore() > highscoreManager.getLowestHighscore() ||
                (highscoreManager.getHighscores().size() < 5) && main.getRoundScore() > 0) {
            printScore();
            newHighscore = true;
            if (isEnteringName && !hasEnteredName) {
                newHighscore = false;
                bigFont.draw(batch, "Enter your name: " + playerName, width / 3.3f, height / 2.5f, 300, Align.left, false);
            } else if (!hasEnteredName) {
                batch.draw(newHighscoreText, 0, 0, width, height);
            }
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isEnteringName && !newHighscore) {
            if (main.isSeeHighscore()) {
                main.setSeeHighscore(false);
            }
            main.setRoundScore(0);
            main.stopGameOverMusic();
            main.create();
        }

        if (isEnteringName) {
            handleTextInput();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            isEnteringName = true;
        }

        if (newHighscore && !highscoreSoundPlayed) {
            highscoreSound.play(.2f);
            highscoreSoundPlayed = true;
        }
    }

    private void printScore() {
        String roundPoints = String.format("Score: %d", main.getRoundScore());
        smallFont.draw(batch, roundPoints, width / 40, height * .95f, 300, Align.left, false);
        smallFont.draw(batch, "Best: " + highscoreManager.getBestScore(), width / 35, height * .88f, 300, Align.left, false);
    }

    private void printHighscores() {
        smallFont.draw(batch, "Top 5 Highscores:", width / 3, height * .97f, 300, Align.left, false);
        float heightCounter = .87f;
        ArrayList<HighscoreManager.ScoreEntry> highScores = highscoreManager.getHighscores();
        for (int i = 0; i < highScores.size(); i++) {
            smallFont.draw(batch,
                    (i + 1) + ". " + highScores.get(i).name + " - " + highScores.get(i).score,
                    width / 2.6f, height * heightCounter, 300, Align.left, false);
            heightCounter -= .07f;
        }
    }

    private void handleTextInput() {
        for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) {
            if (Gdx.input.isKeyJustPressed(key)) {
                if (playerName.length() < 10) {
                    playerName += Input.Keys.toString(key);
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && !playerName.isEmpty()) {
            playerName = playerName.substring(0, playerName.length() - 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !playerName.isEmpty()) {
            highscoreManager.saveHighscore(playerName, main.getRoundScore());
            newHighscore = false;
            isEnteringName = false;
            hasEnteredName = true;
            main.setRoundScore(0);
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        spriteBatch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }
}
