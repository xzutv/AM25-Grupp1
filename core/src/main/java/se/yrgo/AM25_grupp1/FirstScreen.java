package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

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

    private Rectangle charRectangle;
    private Vector2 touchPos;

    private final float speed = 5f;
    private float velocity;
    private final float gravity = -.2f;

    private boolean firstRound;

    private SpriteBatch batch;
    private BitmapFont bigFont;

    private float scaleFactor;
    private float baseFontSize;



    public FirstScreen(Main main) {
        this.main = main;
        this.viewport = new FitViewport(8, 5);
        this.charTexture = new Texture("character.png");
        this.backgroundTexture = new Texture("background2.png");

        this.charSprite = new Sprite(charTexture);
        charSprite.setSize(1, 1);
        charSprite.setPosition(3, 3);
        this.charRectangle = new Rectangle();
        this.spriteBatch = new SpriteBatch();

        this.touchPos = new Vector2();
        this.firstRound = true;

        this.batch = new SpriteBatch();
        this.bigFont = new BitmapFont();

        this.scaleFactor = viewport.getWorldWidth() / 800f;
        this.baseFontSize = 300.0f;

        final Color fontColor = Color.SCARLET;

        this.bigFont.setColor(fontColor);
        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.bigFont.getData().setScale(baseFontSize * scaleFactor);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        draw();
        if (firstRound) {
            batch.begin();
            bigFont.draw(batch, "Press space to start!", 100, 200, 600, Align.center, false);

            batch.end();
        }

        if (firstRound && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            firstRound = false;
        }
        if (!firstRound) {
            input();
            logic();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            velocity = speed;
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            charSprite.setCenterX(touchPos.x);
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float charWidth = charSprite.getWidth();
        float charHeight = charSprite.getHeight();

        float delta = Gdx.graphics.getDeltaTime();
        charRectangle.set(charSprite.getX(), charSprite.getY(), charWidth, charHeight);

        velocity += gravity;
        charSprite.translateY(velocity * delta);

        charSprite.setY(MathUtils.clamp(charSprite.getY(), 0 - charHeight, worldHeight - charHeight));
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
