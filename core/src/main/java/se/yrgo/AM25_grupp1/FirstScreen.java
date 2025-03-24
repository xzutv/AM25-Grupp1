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

    private float scaleFactor;
    private float baseFontSize;

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
//        this.scaleFactor = viewport.getWorldWidth() / 800f;
//        this.baseFontSize = 100.0f;

        final Color fontColor = Color.SCARLET;
        this.bigFont.setColor(fontColor);
        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.bigFont.getData().setScale(2.5f);

        this.smallFont = new BitmapFont();
        this.smallFont.setColor(fontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(1.5f);
    }

    @Override
    public void dispose() {
        bigFont.dispose();
        batch.dispose();
        spriteBatch.dispose();
        charSprite.getTexture().dispose();
    }

    @Override
    public void show() {
        try {
            if (main.getSessionHighscore() > main.getAllTimeHighscore()) {
                main.updateAllTimeHighscore(main.getSessionHighscore());
            }
            else {
                main.updateAllTimeHighscore(main.getAllTimeHighscore());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(float delta) {
        draw();
        batch.begin();
        bigFont.draw(batch, "Press space to start!", 250, 200, 300, Align.center, false);
        if (!main.isFirstRound()) {
            String roundPoints = String.format("You scored: %d", main.getRoundScore());
            smallFont.draw(batch, roundPoints, 250, 150, 300, Align.center, false);
            bigFont.draw(batch, "Your session high-score is: " + main.getSessionHighscore(), 250, 100, 300, Align.center, false);
        }
        bigFont.draw(batch, "Your all-time highscore is: " + main.getAllTimeHighscore(), 250, 50, 300, Align.center, false);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            main.setFirstRound(false);
            main.startGame();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
