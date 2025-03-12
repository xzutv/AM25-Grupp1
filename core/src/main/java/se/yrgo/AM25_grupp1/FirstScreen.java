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
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    private Main main;
    private Texture charTexture;
    private Texture backgroundTexture;
    private Texture pipeTopTexture;
    private Texture pipeBotTexture;

    private Array<Sprite> pipeArrayTop;
    private Array<Sprite> pipeArrayBot;
    private float pipeTimer;
    private Rectangle pipeTopRectangle;
    private Rectangle pipeBotRectangle;

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
        this.viewport = new FitViewport(16, 10);
        this.charTexture = new Texture("character.png");
        this.backgroundTexture = new Texture("background2.png");
        this.pipeBotTexture = new Texture("pipe-bottom.png");
        this.pipeTopTexture = new Texture("pipe-top.png");

        this.charSprite = new Sprite(charTexture);
        charSprite.setSize(1, 1);
        charSprite.setPosition(7, 6);
        this.charRectangle = new Rectangle();
        this.spriteBatch = new SpriteBatch();

        this.touchPos = new Vector2();
        this.firstRound = true;

        this.batch = new SpriteBatch();
        this.bigFont = new BitmapFont();

        this.scaleFactor = viewport.getWorldWidth() / 800f;
        this.baseFontSize = 100.0f;

        final Color fontColor = Color.SCARLET;

        this.bigFont.setColor(fontColor);
        this.bigFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.bigFont.getData().setScale(baseFontSize * scaleFactor);

        this.pipeArrayBot = new Array<>();
        this.pipeArrayTop = new Array<>();
        this.pipeTopRectangle = new Rectangle();
        this.pipeBotRectangle = new Rectangle();

        createPipes();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        draw();
        if (firstRound) {
            batch.begin();
            bigFont.draw(batch, "Press space to start!", 250, 200, 300, Align.center, false);

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

        for (int i = pipeArrayBot.size - 1; i >= 0; i--) {
            Sprite pipeSpriteBot = pipeArrayBot.get(i); 
            float pipeWidth = pipeSpriteBot.getWidth();
            float pipeHeight = pipeSpriteBot.getHeight();

            pipeSpriteBot.translateX(-2f * delta);

            pipeBotRectangle.set(pipeSpriteBot.getX(), pipeSpriteBot.getY(), pipeWidth, pipeHeight);

            if (pipeSpriteBot.getX() < -pipeWidth) {
                pipeArrayBot.removeIndex(i);
            } else if (charRectangle.overlaps(pipeBotRectangle)) { 
                pipeArrayBot.removeIndex(i); 
            }
        }

        for (int i = pipeArrayTop.size - 1; i >= 0; i--) {
            Sprite pipeSpriteTop = pipeArrayTop.get(i); 
            float pipeWidth = pipeSpriteTop.getWidth();
            float pipeHeight = pipeSpriteTop.getHeight();

            pipeSpriteTop.translateX(-2f * delta);
            pipeTopRectangle.set(pipeSpriteTop.getX(), pipeSpriteTop.getY(), pipeWidth, pipeHeight);


            if (pipeSpriteTop.getX() < -pipeWidth) {
                pipeArrayTop.removeIndex(i);
            } else if (charRectangle.overlaps(pipeTopRectangle)) { 
                pipeArrayTop.removeIndex(i); 
            }
        }

        pipeTimer += delta; // Adds the current delta to the timer
        if (pipeTimer > 3f) { // Check if it has been more than given seconds
            pipeTimer = 0; // reset timer
            createPipes(); 
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

        // draw each sprite
        for (Sprite pipeSprite : pipeArrayTop) {
            pipeSprite.draw(spriteBatch);
        }
        for (Sprite pipeSprite : pipeArrayBot) {
            pipeSprite.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void createPipes() {
        // create local variables for convenience
        float pipeWidth = 1;
        float pipeHeight = 4;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite pipeSpriteBot = new Sprite(pipeBotTexture);
        Sprite pipeSpriteTop = new Sprite(pipeTopTexture);

        pipeSpriteBot.setSize(pipeWidth, pipeHeight);
        pipeSpriteBot.setX(worldWidth);
        pipeSpriteBot.setY(-1);
        pipeArrayBot.add(pipeSpriteBot); // Add bot pipe to the list

        pipeSpriteTop.setSize(pipeWidth, pipeHeight);
        pipeSpriteTop.setX(worldWidth);
        pipeSpriteTop.setY(worldHeight + 1 - pipeHeight);
        pipeArrayTop.add(pipeSpriteTop); // Add top pipe to the list

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
