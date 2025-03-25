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

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class GameScreen implements Screen {
    private Main main;
    private Texture charTexture;
    private Texture charAnimationTexture;
    private Texture backgroundTexture;
    private Texture pipeTopTexture;
    private Texture pipeBottomTexture;

    private Array<Sprite> pipeArray;
    private float pipeTimer;
    private float scoreTimer;
    private float animTimer;
    private Rectangle pipeTopRectangle;
    private Rectangle pipeBottomRectangle;

    private Sprite charSprite;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Rectangle charRectangle;
    private Vector2 touchPos;

    private final float speed = 5f;
    private float velocity;
    private final float gravity = -.2f;

    private int points;

    private SpriteBatch batch;
    private BitmapFont smallFont;

    private float width;
    private float height;

    public GameScreen(Main main) {
        this.main = main;
        this.viewport = new FitViewport(16, 10);
        this.charTexture = new Texture("character.png");
        this.charAnimationTexture = new Texture("characterAnimation.png");
        this.backgroundTexture = new Texture("background2.png");
        this.pipeBottomTexture = new Texture("pipe-bottom.png");
        this.pipeTopTexture = new Texture("pipe-top.png");

        this.charSprite = new Sprite(charTexture);
        charSprite.setSize(1, 1);
        charSprite.setPosition(7, 6);
        this.charRectangle = new Rectangle();
        this.spriteBatch = new SpriteBatch();

        this.touchPos = new Vector2();

        this.pipeArray = new Array<>();
        this.pipeTopRectangle = new Rectangle();
        this.pipeBottomRectangle = new Rectangle();

        createPipes();

        this.pipeTimer = 0f;
        this.scoreTimer = -.8f;
        this.animTimer = 0f;

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        this.batch = new SpriteBatch();

        final Color fontColor = Color.SCARLET;

        this.smallFont = new BitmapFont();
        this.smallFont.setColor(fontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(width / 400);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        System.out.println("WorldWidth: " + Gdx.graphics.getWidth());
        System.out.println("WorldHeight: " + Gdx.graphics.getHeight());
        draw();
        batch.begin();
        smallFont.draw(batch, "Score: " + points, width / 30, height * .95f, 200, Align.left, false);
        smallFont.draw(batch, "Best: " + main.getAllTimeHighscore(), width / 26, height * .88f, 300, Align.left, false);
        batch.end();
        input(delta);
        try {
            logic();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void input(float delta) {
        animTimer += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            animTimer = 0f;
            velocity = speed;
            charSprite.setTexture(charAnimationTexture);
        }

        if (animTimer >= 0.4f) {
            charSprite.setTexture(charTexture);
        }
    }

    private void logic() throws IOException {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float charWidth = charSprite.getWidth();
        float charHeight = charSprite.getHeight();

        float delta = Gdx.graphics.getDeltaTime();
        charRectangle.set(charSprite.getX(), charSprite.getY(), (charWidth * .9f), (charHeight * .8f));

        velocity += gravity;
        charSprite.translateY(velocity * delta);

        charSprite.setY(MathUtils.clamp(charSprite.getY(), 0 - charHeight, worldHeight - charHeight));

        for (int i = pipeArray.size - 1; i >= 0; i--) {
            Sprite pipeSpriteTop = pipeArray.get(i);
            float pipeTopWidth = pipeSpriteTop.getWidth();
            float pipeTopHeight = pipeSpriteTop.getHeight();

            Sprite pipeSpriteBottom = pipeArray.get(i);
            float pipeBottomWidth = pipeSpriteBottom.getWidth();
            float pipeBottomHeight = pipeSpriteBottom.getHeight();

            pipeArray.get(i).translateX(-3f * delta);

            pipeBottomRectangle.set(pipeSpriteBottom.getX(), pipeSpriteBottom.getY(), pipeBottomWidth, pipeBottomHeight);
            pipeTopRectangle.set(pipeSpriteTop.getX(), pipeSpriteTop.getY(), pipeTopWidth, pipeTopHeight);


            if (pipeSpriteBottom.getX() < -pipeBottomWidth) {
                pipeArray.removeIndex(i);
            } else if (charRectangle.overlaps(pipeTopRectangle) || charRectangle.overlaps(pipeBottomRectangle)) {
                pipeArray.removeIndex(i);
                main.setRoundScore(points);

                main.create();
            } else if (charRectangle.getY() < 0) {
                main.setRoundScore(points);

                main.create();
            }
        }

        pipeTimer += delta; // Adds the current delta to the timer
        scoreTimer += delta;
        if (scoreTimer > 2f) {
            points++;
            if (points > main.getSessionHighscore()) {
                main.setSessionHighscore(points);
            }
            scoreTimer = 0f;
        }

        if (pipeTimer > 2f) { // Check if it has been more than given seconds
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
        for (Sprite pipeSprite : pipeArray) {
            pipeSprite.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void createPipes() {
        // create local variables for convenience
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float pipeWidth = 1f;
        float pipeHeight = 5f;
        float gap = 4f;

        float pipeHeightBottom = ThreadLocalRandom.current().nextFloat(pipeHeight) + 2;
        float pipeHeightTop = worldHeight + 3 - pipeHeightBottom - gap;


        Sprite pipeSpriteBottom = new Sprite(pipeBottomTexture);
        Sprite pipeSpriteTop = new Sprite(pipeTopTexture);

        pipeSpriteBottom.setSize(pipeWidth, pipeHeightBottom);
        pipeSpriteBottom.setX(worldWidth);
        pipeSpriteBottom.setY(-1);

        pipeSpriteTop.setSize(pipeWidth, pipeHeightTop);
        pipeSpriteTop.setX(worldWidth);
        pipeSpriteTop.setY(worldHeight + 1 - pipeHeightTop);
        pipeArray.add(pipeSpriteBottom, pipeSpriteTop); // Add bot pipe to the list

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
