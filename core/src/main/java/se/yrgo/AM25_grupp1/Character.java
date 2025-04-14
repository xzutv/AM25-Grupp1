package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Character {
    private Texture charTexture;
    private Texture charFirstAnimationTexture;
    private Texture charSecondAnimationTexture;
    private Sprite charSprite;
    private Circle charCircle;
    private float charWidth;
    private float charHeight;
    public Vector2 charPosition;

    public Character() {
        this.charTexture = new Texture("character/character_normal.png");
        this.charFirstAnimationTexture = new Texture("character/character_Up.png");
        this.charSecondAnimationTexture = new Texture("character/character_Down.png");
        this.charSprite = new Sprite(charTexture);
        this.charSprite.setSize(1.5f, 1.5f);
        this.charSprite.setPosition(7, 6);
        this.charCircle = new Circle();

        this.charWidth = charSprite.getWidth();
        this.charHeight = charSprite.getHeight();
    }

    public Sprite getCharSprite() {
        return charSprite;
    }

    public Circle getCharCircle() {
        return charCircle;
    }

    public void animateCharacter() {
        charSprite.setTexture(charFirstAnimationTexture);
    }

    public void animateDown() {
        charSprite.setTexture(charSecondAnimationTexture);
    }

    public void animateBackToDefault() {
        charSprite.setTexture(charTexture);
    }

    public void createCharacterHitbox() {
        charCircle.set(charSprite.getX() + charSprite.getWidth()/2,charSprite.getY() + charSprite.getHeight()/2 + 0.03f, charSprite.getHeight()/3);
    }

    public void applyGravityToCharacter(Float velocity, Float delta) {
        float positionY = velocity * delta;
        charSprite.translateY(positionY);
    }

    public void restrictOutOfBoundsMovement(FitViewport viewport) {
        charSprite.setY(MathUtils.clamp(charSprite.getY(), 0 - charHeight, viewport.getWorldHeight() - charHeight));
    }
}
