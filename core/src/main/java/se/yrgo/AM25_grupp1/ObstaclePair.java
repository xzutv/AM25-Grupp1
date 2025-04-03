package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class ObstaclePair {
    public Sprite obstacleTop;
    public Sprite obstacleBottom;
    public boolean passed = false;

    public ObstaclePair(Sprite top, Sprite bottom) {
        this.obstacleTop = top;
        this.obstacleBottom = bottom;
    }
}
