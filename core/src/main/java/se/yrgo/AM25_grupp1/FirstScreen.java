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
        this.scaleFactor = viewport.getWorldWidth() / 800f;
        this.baseFontSize = 100.0f;

        final Color fontColor = Color.SCARLET;
        this.bigFont.setColor(fontColor);
        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.bigFont.getData().setScale(baseFontSize * scaleFactor);
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
    }

    @Override
    public void render(float delta) {
        draw();
        batch.begin();
        bigFont.draw(batch, "Press space to start!", 250, 200, 300, Align.center, false);
        bigFont.draw(batch, "Your session high-score is: " + main.getSessionHighscore(), 250, 100, 300, Align.center, false);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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
