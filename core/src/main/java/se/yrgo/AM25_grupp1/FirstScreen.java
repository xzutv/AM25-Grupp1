package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.IOException;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    private Main main;
    private Texture charTexture;
    private Texture backgroundTexture;

    private Sprite charSprite;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private SpriteBatch batch;
    private BitmapFont bigFont;
    private BitmapFont smallFont;

    private float width;
    private float height;

    public FirstScreen(Main main) {
        this.main = main;
        this.viewport = new FitViewport(16, 10);
        this.charTexture = new Texture("character.png");
        this.backgroundTexture = new Texture("background2.png");

        this.charSprite = new Sprite(charTexture);
        charSprite.setSize(1, 1);
        charSprite.setPosition(7, 6);
        this.spriteBatch = new SpriteBatch();

        this.batch = new SpriteBatch();
        this.bigFont = new BitmapFont();

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        final Color fontColor = Color.SCARLET;
        this.bigFont.setColor(fontColor);
        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.bigFont.getData().setScale(width / 300);

        this.smallFont = new BitmapFont();
        this.smallFont.setColor(fontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(width / 400);
    }

    @Override
    public void dispose() {
        bigFont.dispose();
        smallFont.dispose();
        batch.dispose();
        spriteBatch.dispose();
        charSprite.getTexture().dispose();
    }

    @Override
    public void show() {
        try {
            if (main.getSessionHighscore() > main.getAllTimeHighscore()) {
                main.updateAllTimeHighscore(main.getSessionHighscore());
            } else {
                main.updateAllTimeHighscore(main.getAllTimeHighscore());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        if (!main.isFirstRound()) {
            String roundPoints = String.format("Score: %d", main.getRoundScore());
            smallFont.draw(batch, roundPoints, width / 30, height * .95f, 300, Align.left, false);
            smallFont.draw(batch, "Session best: " + main.getSessionHighscore(), width / 30, height * .88f, 300, Align.left, false);
            smallFont.draw(batch, "Best: " + main.getAllTimeHighscore(), width / 26, height * .81f, 300, Align.left, false);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            main.setFirstRound(false);
            main.startGame();
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
        charSprite.draw(spriteBatch);

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
