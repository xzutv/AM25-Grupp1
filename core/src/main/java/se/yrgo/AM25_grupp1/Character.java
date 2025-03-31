package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Character {
    private Texture charTexture;
    private Texture charAnimationTexture;
    private Sprite charSprite;
    private Rectangle charRectangle;
    private float charWidth;
    private float charHeight;

    public Character() {
        this.charTexture = new Texture("character.png");
        this.charAnimationTexture = new Texture("characterAnimation.png");
        this.charSprite = new Sprite(charTexture);
        this.charSprite.setSize(1, 1);
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
        charSprite.setTexture(charAnimationTexture);
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
