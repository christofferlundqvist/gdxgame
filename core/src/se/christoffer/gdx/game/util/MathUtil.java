package se.christoffer.gdx.game.util;

public class MathUtil {

    public static int randomBetween(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

}
