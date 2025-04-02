package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
public class GameScreen implements Screen {
    private Main main;
    private HighscoreManager highscoreManager;
    private FitViewport viewport;
    private Character character;
    private Obstacle obstacle;
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;

    private float obstacleTimer;
    private float obstacleDelay;
    private float scoreTimer;
    private float scoreDelay;
    private float scoreChange;
    private float animationTimer;
    private int fasterObs;
    private int fasterLimit;

    private final float SPEED = 5f;
    private float velocity;
    private final float GRAVITY = -.2f;

    private int points;

    private SpriteBatch batch;
    private BitmapFont smallFont;

    private float width;
    private float height;

    private Sound jumpSound;
    private Music gameMusic;

    public GameScreen(Main main) {
        this.main = main;
        this.highscoreManager = new HighscoreManager();
        this.viewport = new FitViewport(16, 10);
        this.character = new Character();
        this.obstacle = new Obstacle();
        this.backgroundTexture = new Texture("background-game.png");
        this.spriteBatch = new SpriteBatch();

        obstacle.createObstacles(viewport);

        this.obstacleTimer = 0f;
        this.obstacleDelay = 2f;
        this.scoreTimer = -.8f;
        this.scoreDelay = 2f;
        this.scoreChange = 0f;
        this.animationTimer = 0f;
        this.fasterObs = 2;
        this.fasterLimit = 0;

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        this.batch = new SpriteBatch();
        final Color fontColor = Color.SCARLET;
        this.smallFont = new BitmapFont();
        this.smallFont.setColor(fontColor);
        this.smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.smallFont.getData().setScale(width / 400);

        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("sound-jump.mp3"));
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound-music.mp3"));

        this.gameMusic.setLooping(true);
        this.gameMusic.setVolume(.5f);
        this.gameMusic.play();
    }

    @Override
    public void show() {
        obstacleDelay = 2f;
        scoreDelay = 2f;
        fasterObs = 2;

    }

    @Override
    public void render(float delta) {
        draw();
        batch.begin();
        smallFont.draw(batch, "Score: " + points, width / 30, height * .95f, 200, Align.left, false);
        smallFont.draw(batch, "Best: " + highscoreManager.getBestScore(), width / 26, height * .88f, 300, Align.left, false);
        batch.end();
        input(delta);
        logic(delta);
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
            jumpSound.play();
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
        velocity += GRAVITY;
        character.applyGravityToCharacter(velocity, delta);
        character.restrictOutOfBoundsMovement(viewport);

        for (int i = obstacle.getObstacleArray().size - 1; i >= 0; i--) {
            obstacle.createObstacleMechanics(i, delta);
            if (obstacle.characterHitsObstacle(character)) {
                obstacle.getObstacleArray().removeIndex(i);
                main.setRoundScore(points);
                gameMusic.stop();
                main.goToGameOverScreen();
            } else if (character.getCharRectangle().getY() < 0) { // Character hits the bottom of the screen.
                main.setRoundScore(points);
                gameMusic.stop();
                main.goToGameOverScreen();
            }
        }

        obstacleTimer += delta; // Adds the current delta to the timer
        scoreTimer += delta;
        if (scoreTimer > scoreDelay && points >= fasterObs && fasterLimit < 3) {
            points++;
            scoreDelay -= .25f;
            scoreTimer = -.25f;
            scoreChange = 1;
            System.out.println(scoreDelay);
        }
        else if(scoreTimer > scoreDelay && scoreChange > 0){
            points++;
            scoreTimer = -.25f;
            scoreChange -= 1;
        }
        else if(scoreTimer > scoreDelay) {
            points++;
            scoreTimer = 0f;
            System.out.println(fasterObs);
        }

        if (obstacleTimer > obstacleDelay && points >= fasterObs && fasterLimit < 3) {
            obstacleDelay -= .15f;
            obstacleTimer = 0f;
            fasterObs += 2;
            fasterLimit += 1;
            obstacle.addSpeed(.5f);
            obstacle.createObstacles(viewport);
        }
        else if(obstacleTimer > obstacleDelay) { // Check if it has been more than given seconds
            obstacleTimer = 0; // reset timer
            obstacle.createObstacles(viewport);
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

        // draw each sprite
        for (Sprite obstacleSprite : obstacle.getObstacleArray()) {
            obstacleSprite.draw(spriteBatch);
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
        gameMusic.dispose();
    }
}
