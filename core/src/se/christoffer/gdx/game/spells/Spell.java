package se.christoffer.gdx.game.spells;

import com.badlogic.gdx.utils.Timer;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.util.Logger;

public class Spell {

    protected String name;
    protected Type type = Type.UNDEFINED;
    protected float manaCost = 0f;
    protected float coolDownInSeconds = 0f;
    protected float currentCoolDown = 0f;

    protected float currentExperience = 0;
    protected int level = 1;

    protected boolean isCasting = false;
    protected boolean isActive = false;

   private Timer coolDownTimer;

    public Spell(String name, float manaCost, Type type, float coolDownInSeconds) {
        this.name = name;
        this.manaCost = manaCost;
        this.type = type;
        this.coolDownInSeconds = coolDownInSeconds;
        this.currentCoolDown = coolDownInSeconds;
    }

    public void update(GameScreen gameScreen) {
        if (isOnCoolDown() || isCasting) {

        } else {
            onCast(gameScreen);
        }
    }

    public void render(GdxGame game) {

    }

    public void onCast(final GameScreen gameScreen) {

        /*
        if (!isOnCoolDown()) {
            //             coolDownTimer.scheduleTask(coolDownTask, 0, 1);
        } else {
            Logger.log("Spell on cd: " + name);
            return;
        } */

        if (manaCost <= gameScreen.getPlayer().getCurrentMana()) {
            isCasting = true;
            isActive = true;
            gameScreen.getPlayer().drainMana(manaCost);
            currentCoolDown = coolDownInSeconds;
            //coolDownTimer.stop();

            coolDownTimer = new Timer();

            Timer.Task coolDownTask = new Timer.Task() {
                @Override
                public void run() {
                    //Logger.log("Cooldown --");
                    if (currentCoolDown > 0) {
                        currentCoolDown--;
                    } else {
                        coolDownTimer.stop();
                        currentCoolDown = coolDownInSeconds;
                        Logger.log("Spell ready: " + name);
                    }

                    if (isActive) {
                        onSpellActiveTick(gameScreen);
                    }
                }
            };

            coolDownTimer.scheduleTask(coolDownTask, 0, 1);
            Logger.log("Casting spell: " + name);
        } else {
            Logger.log("Spell not enough mana: " + name);
            // not enough mana
        }
    }

    public void onSpellActiveTick(GameScreen gameScreen) {

    }

    public boolean isBuff() {
        return type == Type.BUFF;
    }

    // for buffs
    protected void stopSpell() {
        isActive = false;
    }

    private boolean isOnCoolDown() {
        return currentCoolDown < coolDownInSeconds;
    }

}
