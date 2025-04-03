package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.concurrent.ThreadLocalRandom;

public class Obstacle {
    private Texture totemTopTexture;
    private Texture totemBottomTexture;
    private Texture cactusTopTexture;
    private Texture cactusBottomTexture;
    private Texture pillarTopTexture;
    private Texture pillarBottomTexture;
    private Rectangle obstacleTopRectangle;
    private Rectangle obstacleBottomRectangle;
    private float speed;

    private Array<ObstaclePair> obstacleArray;

    public Obstacle() {
        this.totemBottomTexture = new Texture("obstacles/totempole-bottom.png");
        this.totemTopTexture = new Texture("obstacles/totempole-top.png");
        this.cactusBottomTexture = new Texture("obstacles/cactus-bottom.png");
        this.cactusTopTexture = new Texture("obstacles/cactus-top.png");
        this.pillarBottomTexture = new Texture("obstacles/pillar-bottom.png");
        this.pillarTopTexture = new Texture("obstacles/pillar-top.png");

        this.obstacleTopRectangle = new Rectangle();
        this.obstacleBottomRectangle = new Rectangle();

        this.speed = 3f;
        this.obstacleArray = new Array<>();
    }

    public Array<ObstaclePair> getObstacleArray() {
        return obstacleArray;
    }

    public void createObstacles(FitViewport viewport, int points) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float obstacleWidth = 1f;
        float obstacleHeight = 5f;
        float gap = 5f;
        if (points > 9 && points < 20) {
            gap = 4f;
        } else if (points >= 20) {
            gap = 3f;
        }

        float obstacleBottomHeight = ThreadLocalRandom.current().nextFloat(obstacleHeight) + 1;
        float obstacleTopHeight = worldHeight + 2 - obstacleBottomHeight - gap;

        Sprite obstacleSpriteBottom = new Sprite(totemBottomTexture);
        Sprite obstacleSpriteTop = new Sprite(totemTopTexture);
        if (points > 9 && points < 20) {
            obstacleSpriteBottom.setTexture(pillarBottomTexture);
            obstacleSpriteTop.setTexture(pillarTopTexture);
        } else if (points > 19) {
            obstacleSpriteBottom.setTexture(cactusBottomTexture);
            obstacleSpriteTop.setTexture(cactusTopTexture);
        }
        obstacleSpriteBottom.setSize(obstacleWidth, obstacleBottomHeight);
        obstacleSpriteBottom.setX(worldWidth);
        obstacleSpriteBottom.setY(-1);

        obstacleSpriteTop.setSize(obstacleWidth, obstacleTopHeight);
        obstacleSpriteTop.setX(worldWidth);
        obstacleSpriteTop.setY(worldHeight - obstacleTopHeight + 1);
        ObstaclePair pair = new ObstaclePair(obstacleSpriteTop, obstacleSpriteBottom);
        obstacleArray.add(pair);
    }

    public void createObstacleMechanics(int i, float delta) {
        ObstaclePair pair = obstacleArray.get(i);

        pair.obstacleBottom.translateX(-speed * delta);
        pair.obstacleTop.translateX(-speed * delta);

        obstacleTopRectangle.set(pair.obstacleTop.getX(), pair.obstacleTop.getY(), pair.obstacleTop.getWidth(), pair.obstacleTop.getHeight());
        obstacleBottomRectangle.set(pair.obstacleBottom.getX(), pair.obstacleBottom.getY(), pair.obstacleBottom.getWidth(), pair.obstacleBottom.getHeight());

        if (obstacleIsOutOfScreen(pair.obstacleBottom, pair.obstacleBottom.getWidth())) {
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

    public void addSpeed(float change) {
        speed += change;
    }
}
