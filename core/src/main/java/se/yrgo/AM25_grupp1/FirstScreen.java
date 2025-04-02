package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    private Main main;
    private HighscoreManager highscoreManager;
    private FitViewport viewport;
    private Character character;
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;

    private SpriteBatch batch;
    private BitmapFont bigFont;
    private BitmapFont smallFont;

    private float width;
    private float height;

    private Sound jumpSound;

    public FirstScreen(Main main) {
        this.main = main;
        this.highscoreManager = new HighscoreManager();
        this.viewport = new FitViewport(16, 10);
        this.character = new Character();
        this.backgroundTexture = new Texture("background-game.png");
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

        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("sound-jump.mp3"));
    }

    @Override
    public void dispose() {
        bigFont.dispose();
        smallFont.dispose();
        batch.dispose();
        spriteBatch.dispose();
        character.getCharSprite().getTexture().dispose();
    }

    @Override
    public void show() {
        main.setRoundScore(0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        draw();
        batch.begin();
        bigFont.draw(batch, "Press space to start!", width / 3.5f, height / 2.5f, 300, Align.left ,false);
        bigFont.draw(batch, "Press \"H\" to see highscore", width / 4.5f, height / 3.5f, 300, Align.left ,false);
        if (!main.isFirstRound()) {
            String roundPoints = String.format("Score: %d", main.getRoundScore());
            smallFont.draw(batch, roundPoints, width / 30, height * .95f, 300, Align.left, false);
            smallFont.draw(batch, "Best: " + highscoreManager.getBestScore(), width / 26, height * .88f, 300, Align.left, false);
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            jumpSound.play(.1f);
            main.setFirstRound(false);
            main.startGame();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            main.stopMusic();
            main.setSeeHighscore(true);
            main.goToGameOverScreen();
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
        character.getCharSprite().draw(spriteBatch);

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
