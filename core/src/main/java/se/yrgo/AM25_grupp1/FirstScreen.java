package se.yrgo.AM25_grupp1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import org.w3c.dom.css.Rect;

import java.awt.*;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private Texture texture;
    private Rectangle rectangle;
    private Main main;

    public FirstScreen(Main main){
        this.main = main;
        batch = new SpriteBatch();
        font = new BitmapFont();
        rectangle = new Rectangle(0,0, 50,40);
        texture = new Texture("bucket.png");
    }


    @Override
    public void show() {
        // Prepare your screen here.
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();

        rectangle.setLocation(100, 0);
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
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
