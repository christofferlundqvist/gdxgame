package se.christoffer.gdx.game.units;

import com.badlogic.gdx.math.Rectangle;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.util.DamageType;
import se.christoffer.gdx.game.util.Direction;

/**
 * Created by christofferlundqvist on 2017-05-26.
 */
public class Monster {

    protected long experienceGain = 0;
    protected State state = State.IDLE;
    protected Rectangle rect;
    protected float currentHp;
    protected Direction direction = Direction.LEFT;

    public enum State {
        MOVING, ATTACKING, IDLE, DEAD, INVALID
    }

    public void update(final Player player, final float walkX) {

    }

    public void render(final GdxGame gdxGame, final float walkX) {

    }

    public void didGetAttacked(final GameScreen gameScreen, final Rectangle hitBox, final float damage, final DamageType damageType, final float walkX, Player player) {
        //Rectangle rectangle = new Rectangle(direction == Direction.LEFT ? rect.x + rect.width - walkX: rect.x - walkX, rect.y, direction == Direction.LEFT ? - rect.width : rect.width, rect.height);
        Rectangle rectangle = new Rectangle(rect.x - walkX, rect.y, rect.width, rect.height);
        if (rectangle.overlaps(hitBox)) {
            currentHp -= damage;
        }

        if (currentHp <= 0 && state != State.DEAD && state != State.INVALID) {
            player.gainExperience(experienceGain);
            rect.y -= 10;
            state = State.DEAD;
            onDeath(gameScreen);
        }
    }

    public State getState() {
        return state;
    }

    public void onDeath(final GameScreen gameScreen) {

    }

}
