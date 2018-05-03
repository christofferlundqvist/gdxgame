package se.christoffer.gdx.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by christofferlundqvist on 2017-07-07.
 */
public class HPPotion extends Item {

    private static Texture[] sprites;
    public static final String ITEM_NAME = "HPPotion";
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private float healAmount = 20;

    static {
        sprites = new Texture[1];
        sprites[0] = new Texture(Gdx.files.internal("HPPotion.png"));
    }

    public HPPotion(final float x, final float y) {
        this.rect = new Rectangle(x, y, WIDTH, HEIGHT);
        this.name = ITEM_NAME;
        currentImage = sprites[0];
    }

    public float getHealAmount() {
        return healAmount;
    }

}
