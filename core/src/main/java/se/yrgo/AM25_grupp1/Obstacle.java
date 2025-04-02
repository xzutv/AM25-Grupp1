package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.concurrent.ThreadLocalRandom;

public class Obstacle {
    private Texture obstacleTopTexture;
    private Texture obstacleBottomTexture;
    private Rectangle obstacleTopRectangle;
    private Rectangle obstacleBottomRectangle;
    private float speed;

    private Array<Sprite> obstacleArray;

    public Obstacle() {
        this.obstacleBottomTexture = new Texture("pillar-bottom.png");
        this.obstacleTopTexture = new Texture("pillar-top.png");

        this.obstacleTopRectangle = new Rectangle();
        this.obstacleBottomRectangle = new Rectangle();

        this.speed = 3f;
        this.obstacleArray = new Array<>();
    }

    public Array<Sprite> getObstacleArray() {
        return obstacleArray;
    }

    public void createObstacles(FitViewport viewport) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float obstacleWidth = 1f;
        float obstacleHeight = 5f;
        float gap = 4f;

        float obstacleBottomHeight = ThreadLocalRandom.current().nextFloat(obstacleHeight) + 2;
        float obstacleTopHeight = worldHeight + 3 - obstacleBottomHeight - gap;

        Sprite obstacleSpriteBottom = new Sprite(obstacleBottomTexture);
        obstacleSpriteBottom.setSize(obstacleWidth, obstacleBottomHeight);
        obstacleSpriteBottom.setX(worldWidth);
        obstacleSpriteBottom.setY(-1);

        Sprite obstacleSpriteTop = new Sprite(obstacleTopTexture);
        obstacleSpriteTop.setSize(obstacleWidth, obstacleTopHeight);
        obstacleSpriteTop.setX(worldWidth);
        obstacleSpriteTop.setY(worldHeight + 1 - obstacleTopHeight);
        obstacleArray.add(obstacleSpriteBottom, obstacleSpriteTop); // Add bot pipe to the list
    }

    public void createObstacleMechanics(int i, float delta) {
        Sprite obstacleSpriteTop = obstacleArray.get(i);
        float obstacleTopWidth = obstacleSpriteTop.getWidth();
        float obstacleTopHeight = obstacleSpriteTop.getHeight();

        Sprite obstacleSpriteBottom = obstacleArray.get(i);
        float obstacleBottomWidth = obstacleSpriteBottom.getWidth();
        float obstacleBottomHeight = obstacleSpriteBottom.getHeight();

        obstacleArray.get(i).translateX(-speed * delta);

        obstacleBottomRectangle.set(obstacleSpriteBottom.getX(), obstacleSpriteBottom.getY(), obstacleBottomWidth, obstacleBottomHeight);
        obstacleTopRectangle.set(obstacleSpriteTop.getX(), obstacleSpriteTop.getY(), obstacleTopWidth, obstacleTopHeight);

        if (obstacleIsOutOfScreen(obstacleSpriteBottom, obstacleBottomWidth)) {
            obstacleArray.removeIndex(i);
        }
    }

    public boolean obstacleIsOutOfScreen(Sprite obstacleSprite, Float width) {
        return obstacleSprite.getX() < -width;
    }

    public boolean characterHitsObstacle(Character character) {
        if (character.getCharRectangle().overlaps(obstacleTopRectangle) ||
            character.getCharRectangle().overlaps(obstacleBottomRectangle)) {

            return true;
        }
        return false;
    }
    public float getSpeed() {
        return speed;
    }

    public void addSpeed(float change){
        speed += change;
    }
}
