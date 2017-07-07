package se.christoffer.gdx.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.util.SpriteUtil;

/**
 * Created by christofferlundqvist on 2017-06-09.
 */
public class Item {

    protected Texture currentImage;
    protected Rectangle rect;
    protected String name;

    protected float spriteTimer = 0;

    public void update(GameScreen gameScreen, final Player player, final float walkX) {

    }

    public void render(final GameScreen gameScreen, final float walkX) {
        gameScreen.game.batch.begin();
        gameScreen.game.batch.draw(currentImage, rect.x - walkX, rect.y, rect.width, rect.height);
        gameScreen.game.batch.end();
    }

    public Rectangle getRect() {
        return rect;
    }

    public String getName() {
        return name;
    }

}
