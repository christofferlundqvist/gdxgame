package se.christoffer.gdx.game.util;

public class Logger {

    private static com.badlogic.gdx.utils.Logger logger = new com.badlogic.gdx.utils.Logger("GdxGame");

    public static void log(String logString) {
        logger.error(logString);
    }

}
