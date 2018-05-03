package se.christoffer.gdx.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import se.christoffer.gdx.game.GdxGame;

public class RenderUtil {

    private static final float DEFAULT_HEALTH_BAR_HEIGHT = 8f;

    public static void drawHpBar(final GdxGame game, float x, float y, float width, float currentHp, float maxHp) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(x, y, width, DEFAULT_HEALTH_BAR_HEIGHT);

        float percentageRed = Math.max(currentHp, 0) / maxHp;
        game.shapeRenderer.setColor(Color.SCARLET);

        game.shapeRenderer.rect(x, y, width * percentageRed, DEFAULT_HEALTH_BAR_HEIGHT);
        game.shapeRenderer.end();
    }

}
