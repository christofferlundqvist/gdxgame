package se.christoffer.gdx.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.util.SpriteUtil;

/**
 * Created by christofferlundqvist on 2017-06-09.
 */
public class Coin extends Item {

    public static final String ITEM_NAME = "Coin";
    private static Texture[] sprites;
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    static {
        sprites = new Texture[10];
        sprites[0] = new Texture(Gdx.files.internal("coin/coin-1.png"));
        sprites[1] = new Texture(Gdx.files.internal("coin/coin-2.png"));
        sprites[2] = new Texture(Gdx.files.internal("coin/coin-3.png"));
        sprites[3] = new Texture(Gdx.files.internal("coin/coin-4.png"));
        sprites[4] = new Texture(Gdx.files.internal("coin/coin-5.png"));
        sprites[5] = new Texture(Gdx.files.internal("coin/coin-6.png"));
        sprites[6] = new Texture(Gdx.files.internal("coin/coin-7.png"));
        sprites[7] = new Texture(Gdx.files.internal("coin/coin-8.png"));
        sprites[8] = new Texture(Gdx.files.internal("coin/coin-9.png"));
        sprites[9] = new Texture(Gdx.files.internal("coin/coin-10.png"));
    }

    public Coin(final float x, final float y) {
        this.rect = new Rectangle(x, y, WIDTH, HEIGHT);
        this.name = ITEM_NAME;
        currentImage = sprites[0];
    }

    public void update(GameScreen gameScreen, final Player player, final float walkX) {
        currentImage = SpriteUtil.getCurrentSprite(spriteTimer, sprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT);
        spriteTimer = SpriteUtil.updateSpriteTimer(spriteTimer, sprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT);
    }

}
