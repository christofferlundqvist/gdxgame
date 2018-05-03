package se.christoffer.gdx.game.util;

import com.badlogic.gdx.graphics.Texture;

import se.christoffer.gdx.game.GameScreen;

/**
 * Created by christofferlundqvist on 2017-07-07.
 */
public class SpriteUtil {

    public static final int SPRITE_TICK_INTERVAL_DEFAULT = 5;

    public static Texture getCurrentSprite(float spriteTimer, Texture[] sprites) {
        return getCurrentSprite(spriteTimer, sprites, SPRITE_TICK_INTERVAL_DEFAULT);
    }

    public static Texture getCurrentSprite(float spriteTimer, Texture[] sprites, float tickInterval) {
        spriteTimer = spriteTimer + 1 * GameScreen.speedMultiplier;
        int currentIndex = Math.max((int) Math.floor(spriteTimer / tickInterval) - 1, 0);
        return sprites[Math.min(currentIndex, sprites.length - 1)];
    }

    public static float updateSpriteTimer(float spriteTimer, Texture[] sprites) {
        return updateSpriteTimer(spriteTimer, sprites, SPRITE_TICK_INTERVAL_DEFAULT);
    }

    public static float updateSpriteTimer(float spriteTimer, Texture[] sprites, float tickInterval) {
        if (isAnimationFinished(spriteTimer, sprites, tickInterval)) {
            return 0;
        } else {
            //spriteTimer = spriteTimer + 1 * GameScreen.speedMultiplier;
            return spriteTimer + 1 * GameScreen.speedMultiplier;
        }
    }

    public static boolean isAnimationFinished(float spriteTimer, Texture[] sprites) {
        return isAnimationFinished(spriteTimer, sprites, SPRITE_TICK_INTERVAL_DEFAULT);
    }

    public static boolean isAnimationFinished(float spriteTimer, Texture[] sprites, float tickInterval) {
        return spriteTimer >= tickInterval * sprites.length;
    }

}
