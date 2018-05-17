package se.christoffer.gdx.game.spells;

import com.badlogic.gdx.utils.Timer;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.util.Logger;

public class Haste extends Spell {

    private static final String NAME = "Haste";
    private static final float MANA_COST = 10f;
    private static final Type TYPE = Type.BUFF;
    private static final float COOL_DOWN_IN_SECONDS = 30;

    private float attackSpeedMultiplier = 2f;
    private float walkSpeedMultiplier = 2f;

    private float BUFF_TIME_IN_SECONDS = 15f;
    private float currentBuffTime = BUFF_TIME_IN_SECONDS;

    public Haste() {
        super(NAME, MANA_COST, TYPE, COOL_DOWN_IN_SECONDS);
    }

    @Override
    public void onCast(GameScreen gameScreen) {
        super.onCast(gameScreen);

        gameScreen.getPlayer().addSpellBuff(this);
    }

    public void onSpellActiveTick(GameScreen gameScreen) {
        // Logger.log("buffTime --");
        if (currentBuffTime > 0) {
            currentBuffTime--;
        } else {
            Logger.log("Spell " + name + " buff duration done");
            stopSpell();
            gameScreen.getPlayer().removeSpellBuff(this);
            currentBuffTime = BUFF_TIME_IN_SECONDS;
            isCasting = false;
        }
    }

    public float getAttackSpeedMultiplier() {
        return attackSpeedMultiplier;
    }

    public float getWalkSpeedMultiplier() {
        return walkSpeedMultiplier;
    }

}
