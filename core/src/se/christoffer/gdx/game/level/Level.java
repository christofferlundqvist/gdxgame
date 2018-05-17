package se.christoffer.gdx.game.level;

public class Level {

    private Difficulty difficulty;
    private LevelMod levelMod;

    public Level(Difficulty difficulty, LevelMod levelMod) {
        this.difficulty = difficulty;
        this.levelMod = levelMod;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public LevelMod getLevelMod() {
        return levelMod;
    }
}
