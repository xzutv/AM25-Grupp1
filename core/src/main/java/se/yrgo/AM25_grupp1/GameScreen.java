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
public class GameScreen implements Screen {
    private Main main;
    private HighscoreManager highscoreManager;
    private FitViewport viewport;
    private Character character;
    private Obstacle obstacle;
    private Texture backgroundTextureMountains;
    private Texture backgroundTextureForest;
    private Texture backgroundTextureDesert;
    private Texture pausedText;
    private SpriteBatch spriteBatch;

    private float obstacleTimer;
    private float animationTimer;

    private final float SPEED = 5f;
    private float velocity;
    private final float GRAVITY = -18f;

    private int points;

    private SpriteBatch batch;
    private BitmapFont smallFont;
    private BitmapFont bigFont;

    private float width;
    private float height;

    private Sound jumpSound;
    private Sound deathSound;
    private Sound startSound;

    private boolean paused;
    private boolean gameOver;
    private float deathTimer;

    private int speedIncreaseThreshold = 2;
    private int speedIncreaseStep = 2; // increase every 2 points
    private int speedIncreaseLimit = 4; // only 3 times
    private int speedIncreasesSoFar = 0;

    private float obstacleDelay = 2f;

    public GameScreen(Main main) {
        this.main = main;
        this.highscoreManager = new HighscoreManager();
        this.viewport = new FitViewport(16, 10);
        this.character = new Character();
        this.obstacle = new Obstacle();
        this.backgroundTextureMountains = new Texture("backgrounds/background-game-mountains.png");
        this.backgroundTextureForest = new Texture("backgrounds/background-game-forest.png");
        this.backgroundTextureDesert = new Texture("backgrounds/background-game-desert.png");
        this.pausedText = new Texture("text/text-gamescreen-paused.png");
        this.spriteBatch = new SpriteBatch();

        obstacle.createObstacles(viewport, points);

        this.obstacleTimer = 0f;
        this.animationTimer = 0f;

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        this.batch = new SpriteBatch();
        final Color smallFontColor = Color.BLACK;
        this.smallFont = new BitmapFont();
        this.smallFont.setColor(smallFontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(width / 400);

        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound-jump.mp3"));
        this.deathSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound-death.wav"));
        this.startSound = Gdx.audio.newSound(Gdx.files.internal("audio/sound-start.wav"));

        this.paused = false;
        this.gameOver = false;
        this.deathTimer = 0f;
    }

    @Override
    public void show() {
        startSound.play(.2f);
    }

    @Override
    public void render(float delta) {
        draw();
        batch.begin();
        if (points >= 20) {
            smallFont.setColor(Color.WHITE);
        }
        smallFont.draw(batch, "Score: " + points, width / 30, height * .95f, 200, Align.left, false);
        smallFont.draw(batch, "Best: " + highscoreManager.getBestScore(), width / 26, height * .88f, 300, Align.left, false);

        if (paused) {
            batch.draw(pausedText, 0, 0, width, height);
        }
        batch.end();
        if (!paused) {
            if (!gameOver) {
                input(delta);
                logic(delta);
            } else {
                deathTimer += delta;
                if (deathTimer >= 1.5f) {
                    gameOver = false;
                    main.goToGameOverScreen();
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void input(float delta) {
        animationTimer += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            animationTimer = 0f;
            velocity = SPEED;
            jumpSound.play(.1f);
            character.animateCharacter();
        }
        if (animationTimer >= 0.1f) {
            character.animateBackToDefault();
        }
        if (animationTimer >= 0.2f) {
            character.animateDown();
        }
    }

    private void logic(Float delta) {
        character.createCharacterHitbox();
        velocity += GRAVITY * delta;
        character.applyGravityToCharacter(velocity, delta);
        character.restrictOutOfBoundsMovement(viewport);

        for (int i = obstacle.getObstacleArray().size - 1; i >= 0; i--) {
            ObstaclePair pair = obstacle.getObstacleArray().get(i);
            obstacle.createObstacleMechanics(i, delta);
            if (obstacle.characterHitsObstacle(character)) {
                main.setRoundScore(points);
                main.stopMusic();
                gameOver = true;
                deathSound.play(.3f);
            } else if (character.getCharRectangle().getY() < 0) { // Character hits the bottom of the screen.
                main.setRoundScore(points);
                main.stopMusic();
                gameOver = true;
                deathSound.play(.3f);
            }
            if (!pair.passed && ((pair.obstacleTop.getX() + pair.obstacleTop.getWidth() / 10)) < character.getCharRectangle().x) {
                pair.passed = true;
                points++;
                if (points >= speedIncreaseThreshold && speedIncreasesSoFar < speedIncreaseLimit) {
                    increaseDifficulty();
                }
            }
        }
        obstacleTimer += delta;
        if (obstacleTimer > obstacleDelay) {
            obstacleTimer = 0;
            obstacle.createObstacles(viewport, points);
        }
    }

    private void increaseDifficulty() {
        obstacle.addSpeed(0.5f);
        speedIncreasesSoFar++;
        speedIncreaseThreshold += speedIncreaseStep;

        float minSpawnDelay = 0.8f;
        obstacleDelay = Math.max(minSpawnDelay, 2f - speedIncreasesSoFar * 0.25f);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        if (points < 10) {
            spriteBatch.draw(backgroundTextureForest, 0, 0, worldWidth, worldHeight);
        } else if (points < 20) {
            spriteBatch.draw(backgroundTextureMountains, 0, 0, worldWidth, worldHeight);
        } else {
            spriteBatch.draw(backgroundTextureDesert, 0, 0, worldWidth, worldHeight);
        }

        character.getCharSprite().draw(spriteBatch);

        // draw each obstacle-pair
        for (ObstaclePair pair : obstacle.getObstacleArray()) {
            pair.obstacleBottom.draw(spriteBatch);
            pair.obstacleTop.draw(spriteBatch);
        }
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
    }
}
