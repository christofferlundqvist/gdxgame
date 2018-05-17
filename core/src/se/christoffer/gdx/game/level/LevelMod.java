package se.christoffer.gdx.game.level;

import java.util.ArrayList;
import java.util.List;

import se.christoffer.gdx.game.util.Logger;
import se.christoffer.gdx.game.util.MathUtil;

public class LevelMod {

    private static List<String> availableMods = new ArrayList<String>();

    private int numberOfMods = 0;
    private final List<Mod> mods;

    static {
        availableMods.add("goldMultiplier");
        availableMods.add("playerDamageMultiplier");
        availableMods.add("monsterDamageMultiplier");
        availableMods.add("bossDamageMultiplier");
        availableMods.add("monsterMoveSpeedMultiplier");
        availableMods.add("playerMoveSpeedMultiplier");
        availableMods.add("playerSpellCooldownMultiplier");
    }

    enum Amount {
        POSITIVE, NEGATIVE, ZERO
    }

    private class Mod {
        final String name;
        final float value;
        final Amount amount;

        Mod(String name, float value, Amount amount) {
            this.name = name;
            this.value = value;
            this.amount = amount;
        }
    }

    public LevelMod(int numberOfMods) {
        this.numberOfMods = numberOfMods;

        if (numberOfMods > availableMods.size()) {
            throw new IllegalArgumentException("numberOfMods can not be larger than availableMods.size()!");
        }

        this.mods = getRandomizedMods();
        logLevel();
    }

    private void logLevel() {
        String logString = "";
        for (Mod mod: mods) {
            logString += mod.name + ", " + mod.value + ", " + mod.amount + " \n";
        }
        Logger.log("Creating mods for level...\n" +
                "numberOfMods: " + numberOfMods + "\n" +
                "mods: " + logString);
    }

    private List<Mod> getRandomizedMods() {

        List<Mod> mods = new ArrayList<Mod>();

        int numMods = MathUtil.randomBetween(1, numberOfMods);

        for (int i = 0; i < numMods; i++) {
            int newModIndex = MathUtil.randomBetween(1, availableMods.size());
            String mod = availableMods.get(newModIndex);
            float value = (float) MathUtil.randomBetween(1, 3);

            // TODS dont add same twice?
            mods.add(new Mod(mod, value, getRandomizedAmount()));
        }
        return mods;
    }

    private Amount getRandomizedAmount() {
        int randomValue = MathUtil.randomBetween(1, 3);
        if (randomValue == 1) {
            return Amount.POSITIVE;
        } else if (randomValue == 2) {
            return Amount.NEGATIVE;
        } else {
            return Amount.ZERO;
        }
    }

}
