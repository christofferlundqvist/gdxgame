package se.christoffer.gdx.game.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.util.Logger;
import se.christoffer.gdx.game.util.SpriteUtil;

public class Cat {

    public static final String NAME = "Minx"; // Named by Olivia Ullebulle
    public static final float WIDTH = 64;
    public static final float HEIGHT = 64;

    private static Texture[] walkSprites = new Texture[12];
    private Texture currentImage;
    private float walkTimer;

    private Rectangle rectangle;
    private float x;
    private float y;

    public Cat(float x, float y) {
        this.x = x;
        this.y = y;
        this.rectangle = new Rectangle(x, y, WIDTH, HEIGHT);
        currentImage = walkSprites[0];
    }

    public static void loadAssets() {
        for (int i = 0; i < walkSprites.length; i++) {
            walkSprites[i] = new Texture(Gdx.files.internal("cat/Cat_walk_" + (i + 1) + ".png"));
        }
    }

    public void update(final GameScreen gameScreen) {
        currentImage = SpriteUtil.getCurrentSprite(walkTimer, walkSprites);
        walkTimer = SpriteUtil.updateSpriteTimer(walkTimer, walkSprites);
    }

    public void render(final GdxGame game, final GameScreen gameScreen) {
        game.batch.begin();
        Player player = gameScreen.getPlayer();
        float walkX = player.getWalkX();
        game.batch.draw(currentImage, rectangle.x, rectangle.y, WIDTH, HEIGHT);
        game.batch.end();
    }

}
