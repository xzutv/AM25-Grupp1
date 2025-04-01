package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Character {
    private Texture charTexture;
    private Texture charFirstAnimationTexture;
    private Texture charSecondAnimationTexture;
    private Sprite charSprite;
    private Rectangle charRectangle;
    private float charWidth;
    private float charHeight;

    public Character() {
        this.charTexture = new Texture("character_normal.png");
        this.charFirstAnimationTexture = new Texture("character_Up.png");
        this.charSecondAnimationTexture = new Texture("character_Down.png");
        this.charSprite = new Sprite(charTexture);
        this.charSprite.setSize(1.5f, 1.5f);
        this.charSprite.setPosition(7, 6);
        this.charRectangle = new Rectangle();

        this.charWidth = charSprite.getWidth();
        this.charHeight = charSprite.getHeight();
    }

    public Sprite getCharSprite() {
        return charSprite;
    }

    public Rectangle getCharRectangle() {
        return charRectangle;
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
        charRectangle.set(charSprite.getX(), charSprite.getY(), (charWidth * .9f), (charHeight * .8f));
    }

    public void applyGravityToCharacter(Float velocity, Float delta) {
        charSprite.translateY(velocity * delta);
    }

    public void restrictOutOfBoundsMovement(FitViewport viewport) {
        charSprite.setY(MathUtils.clamp(charSprite.getY(), 0 - charHeight, viewport.getWorldHeight() - charHeight));
    }
}
