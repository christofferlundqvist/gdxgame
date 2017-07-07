package se.christoffer.gdx.game.util;

import java.util.Map;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.item.Coin;
import se.christoffer.gdx.game.item.HPPotion;

/**
 * Created by christofferlundqvist on 2017-07-07.
 */
public class ItemUtil {

    public static void dropItems(GameScreen gameScreen, Map<String, Float> itemDrops, float x, float y) {
        for (Map.Entry<String, Float> entry : itemDrops.entrySet()) {
            if ((int) (Math.random() * 100) <= entry.getValue()) {
                if (entry.getKey().equals("Coin")) {
                    gameScreen.addItem(new Coin(x, y));
                }
                if (entry.getKey().equals("HPPotion")) {
                    gameScreen.addItem(new HPPotion(x, y));
                }
            }
        }
    }

}
