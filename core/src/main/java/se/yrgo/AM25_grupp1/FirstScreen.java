package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    Main main;
    Texture charTexture;
    Texture backgroundTexture;

    Sprite charSprite;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    Rectangle charRectangle;



    public FirstScreen(Main main) {
        this.main = main;
        charTexture = new Texture("character.png");
        backgroundTexture = new Texture("background2.png");
        charSprite = new Sprite(charTexture);
        charSprite.setSize(1, 1);
        charSprite.setPosition(3, 3);
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        charRectangle = new Rectangle();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float charWidth = charSprite.getWidth();
        float charHeight = charSprite.getHeight();

        charSprite.setX(MathUtils.clamp(charSprite.getX(), 0, worldWidth - charWidth));

        float delta = Gdx.graphics.getDeltaTime();
        charRectangle.set(charSprite.getX(), charSprite.getY(), charWidth, charHeight);
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

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
