package se.christoffer.gdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.util.DamageType;
import se.christoffer.gdx.game.util.Direction;
import se.christoffer.gdx.game.util.ItemUtil;
import se.christoffer.gdx.game.util.SpriteUtil;

/**
 * Created by christofferlundqvist on 2017-05-26.
 */
public class Zombie extends Monster {

    private static final long EXPERIENCE_GAIN = 10;
    private static final float WALK_SPEED = 50;
    private static final float ATTACK_RANGE = 2; // extra px attack range
    private static final float ATTACK_SPEED_MULTIPLIER = 1.5f; //
    private static final float SPRITE_TICK_INTERVAL = SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * ATTACK_SPEED_MULTIPLIER;
    private static final float ATTACK_DAMAGE = 5;
    private static final DamageType DAMAGE_TYPE = DamageType.PHYSICAL;

    private static Texture[] walkSprites = new Texture[10];
    private static Texture[] attackSprites = new Texture[8];
    private static Texture[] deadSprites = new Texture[12];

    private static final Map<String, Float> itemDrops = new HashMap<String, Float>();

    private Texture currentImage;
    private float walkTimer;
    private float attackTimer;
    private float deadTimer;

    private Rectangle attackHitBox;

    static {
        itemDrops.put("Coin", 100f);
        itemDrops.put("HPPotion", 100f);
    }

    public Zombie(float x, float y) {
        super(64, 64, 30);
        rect = new Rectangle(x, y, width, height);
        state = State.MOVING;
        currentHp = maxHp;
        currentImage = walkSprites[0];
        experienceGain = EXPERIENCE_GAIN;
    }

    public static void loadAssets() {
        walkSprites[0] = new Texture(Gdx.files.internal("zombie_male/Walk (1).png"));
        walkSprites[1] = new Texture(Gdx.files.internal("zombie_male/Walk (2).png"));
        walkSprites[2] = new Texture(Gdx.files.internal("zombie_male/Walk (3).png"));
        walkSprites[3] = new Texture(Gdx.files.internal("zombie_male/Walk (4).png"));
        walkSprites[4] = new Texture(Gdx.files.internal("zombie_male/Walk (5).png"));
        walkSprites[5] = new Texture(Gdx.files.internal("zombie_male/Walk (6).png"));
        walkSprites[6] = new Texture(Gdx.files.internal("zombie_male/Walk (7).png"));
        walkSprites[7] = new Texture(Gdx.files.internal("zombie_male/Walk (8).png"));
        walkSprites[8] = new Texture(Gdx.files.internal("zombie_male/Walk (9).png"));
        walkSprites[9] = new Texture(Gdx.files.internal("zombie_male/Walk (10).png"));

        attackSprites[0] = new Texture(Gdx.files.internal("zombie_male/Attack (1).png"));
        attackSprites[1] = new Texture(Gdx.files.internal("zombie_male/Attack (2).png"));
        attackSprites[2] = new Texture(Gdx.files.internal("zombie_male/Attack (3).png"));
        attackSprites[3] = new Texture(Gdx.files.internal("zombie_male/Attack (4).png"));
        attackSprites[4] = new Texture(Gdx.files.internal("zombie_male/Attack (5).png"));
        attackSprites[5] = new Texture(Gdx.files.internal("zombie_male/Attack (6).png"));
        attackSprites[6] = new Texture(Gdx.files.internal("zombie_male/Attack (7).png"));
        attackSprites[7] = new Texture(Gdx.files.internal("zombie_male/Attack (8).png"));

        deadSprites[0] = new Texture(Gdx.files.internal("zombie_male/Dead (1).png"));
        deadSprites[1] = new Texture(Gdx.files.internal("zombie_male/Dead (2).png"));
        deadSprites[2] = new Texture(Gdx.files.internal("zombie_male/Dead (3).png"));
        deadSprites[3] = new Texture(Gdx.files.internal("zombie_male/Dead (4).png"));
        deadSprites[4] = new Texture(Gdx.files.internal("zombie_male/Dead (5).png"));
        deadSprites[5] = new Texture(Gdx.files.internal("zombie_male/Dead (6).png"));
        deadSprites[6] = new Texture(Gdx.files.internal("zombie_male/Dead (7).png"));
        deadSprites[7] = new Texture(Gdx.files.internal("zombie_male/Dead (8).png"));
        deadSprites[8] = new Texture(Gdx.files.internal("zombie_male/Dead (9).png"));
        deadSprites[9] = new Texture(Gdx.files.internal("zombie_male/Dead (10).png"));
        deadSprites[10] = new Texture(Gdx.files.internal("zombie_male/Dead (11).png"));
        deadSprites[11] = new Texture(Gdx.files.internal("zombie_male/Dead (12).png"));
    }

    @Override
    public void onDeath(final GameScreen gameScreen) {
        ItemUtil.dropItems(gameScreen, itemDrops, rect.x + 24, rect.y + 12);
    }

    private void updateDead() {
        currentImage = SpriteUtil.getCurrentSprite(deadTimer, deadSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT);
        deadTimer = SpriteUtil.updateSpriteTimer(deadTimer, deadSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT);

        if (deadTimer == SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * deadSprites.length) {
            state = State.INVALID;
        }
    }

    private void updateAttack(final Player player, final float walkX) {
        currentImage = SpriteUtil.getCurrentSprite(attackTimer, attackSprites, SPRITE_TICK_INTERVAL);
        attackTimer = SpriteUtil.updateSpriteTimer(attackTimer, attackSprites, SPRITE_TICK_INTERVAL);

        if (SpriteUtil.isAnimationFinished(attackTimer, attackSprites, SPRITE_TICK_INTERVAL)) {
            attackTimer = 0;
            state = State.IDLE;
        }

        if (rect.x >= player.getRect().x + 32 + walkX) {
            direction = Direction.LEFT;
        } else if (rect.x < player.getRect().x - 32 + walkX) {
            direction = Direction.RIGHT;
        }

        // sprite index 3, did just swing arms
        // TODO can it ever skip 16?
        if (attackTimer == 16) {
            boolean flip = (direction == Direction.LEFT);
            attackHitBox = new Rectangle(flip ? rect.x - walkX - 16 : rect.x - walkX + width, rect.y, 16, height);
            player.didGetAttacked(attackHitBox, ATTACK_DAMAGE, DAMAGE_TYPE);
        }

    }

    private void updateWalk(final Player player, final float walkX) {
        currentImage = SpriteUtil.getCurrentSprite(walkTimer, walkSprites);
        walkTimer = SpriteUtil.updateSpriteTimer(walkTimer, walkSprites);

        if (rect.x >= player.getRect().x + player.getRect().width / 2 + walkX) {
            rect.x -= WALK_SPEED * Gdx.graphics.getDeltaTime();
            direction = Direction.LEFT;
        } else if (rect.x < player.getRect().x - player.getRect().width / 2 + walkX) {
            rect.x += WALK_SPEED * Gdx.graphics.getDeltaTime();
            direction = Direction.RIGHT;
        }
    }

    public boolean isCloseTo(final Rectangle target, final float walkX) {
        float x = target.x + target.width / 2 + walkX;
        float y = target.y + target.width / 2; // + walkY

        double distance = Math.sqrt( (rect.x + width / 2 - x) * (rect.x + width / 2 - x) + (rect.y + height / 2 - y) * (rect.y + height / 2 - y) );
        distance -= target.width / 2;
        return distance < ATTACK_RANGE;
    }

    public void update(final Player player, final float walkX) {
        if (state == State.INVALID) {
            return;
        }

        if (state == State.DEAD) {
            updateDead();
            return;
        }

        if (isCloseTo(player.getRect(), walkX)) {
            state = State.ATTACKING;
        }

        if (state == State.MOVING || state == State.IDLE) {
            updateWalk(player, walkX);
        } else if (state == State.ATTACKING) {
            updateAttack(player, walkX);
        }
    }

    public void render(final GdxGame game, final float walkX) {
        game.batch.begin();
        boolean flip = (direction == Direction.LEFT);
        game.batch.draw(currentImage, flip ? rect.x + width - walkX: rect.x - walkX, rect.y, flip ? - width : width, height);
        game.batch.end();

        super.render(game, walkX);
    }

}
