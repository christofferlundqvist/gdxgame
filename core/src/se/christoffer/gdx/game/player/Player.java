package se.christoffer.gdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Logger;

import java.util.Iterator;
import java.util.List;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.item.Coin;
import se.christoffer.gdx.game.item.Item;
import se.christoffer.gdx.game.units.Monster;
import se.christoffer.gdx.game.util.DamageType;
import se.christoffer.gdx.game.util.Direction;
import se.christoffer.gdx.game.util.SpriteUtil;

/**
 * Created by christofferlundqvist on 2017-06-09.
 */
public class Player {

    private static final int MAX_HP = 100;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final float WALK_SPEED = 200f;

    private static Texture[] runSprites = new Texture[10];
    private static Texture[] attackSprites = new Texture[10];
    private static Texture[] idleSprites = new Texture[10];

    private Rectangle rect;
    private Texture currentImage;

    private int currentHp = MAX_HP;
    private float extraAttackRange = 0;
    private float extraAttackSpeedMultiplier = 1;
    private float damage = 10;
    private DamageType damageType = DamageType.PHYSICAL;
    private Direction direction = Direction.RIGHT;

    private float walkX = 0;

    private float attackTimer = 0;
    private boolean isAttacking = false;

    private boolean isIdling = true;
    private float idleTimer = 0;

    private boolean isRunning = false;
    private float runTimer = 0;

    private boolean ballIsJumping = false;
    private float jumpTimer = 0;

    private long gold = 0;
    private long experience = 0;

    public Player(final float x, final float y) {
        rect = new Rectangle(x, y, WIDTH, HEIGHT);

        runSprites[0] = new Texture(Gdx.files.internal("Run (1).png"));
        runSprites[1] = new Texture(Gdx.files.internal("Run (2).png"));
        runSprites[2] = new Texture(Gdx.files.internal("Run (3).png"));
        runSprites[3] = new Texture(Gdx.files.internal("Run (4).png"));
        runSprites[4] = new Texture(Gdx.files.internal("Run (5).png"));
        runSprites[5] = new Texture(Gdx.files.internal("Run (6).png"));
        runSprites[6] = new Texture(Gdx.files.internal("Run (7).png"));
        runSprites[7] = new Texture(Gdx.files.internal("Run (8).png"));
        runSprites[8] = new Texture(Gdx.files.internal("Run (9).png"));
        runSprites[9] = new Texture(Gdx.files.internal("Run (10).png"));

        idleSprites[0] = new Texture(Gdx.files.internal("Idle (1).png"));
        idleSprites[1] = new Texture(Gdx.files.internal("Idle (2).png"));
        idleSprites[2] = new Texture(Gdx.files.internal("Idle (3).png"));
        idleSprites[3] = new Texture(Gdx.files.internal("Idle (4).png"));
        idleSprites[4] = new Texture(Gdx.files.internal("Idle (5).png"));
        idleSprites[5] = new Texture(Gdx.files.internal("Idle (6).png"));
        idleSprites[6] = new Texture(Gdx.files.internal("Idle (7).png"));
        idleSprites[7] = new Texture(Gdx.files.internal("Idle (8).png"));
        idleSprites[8] = new Texture(Gdx.files.internal("Idle (9).png"));
        idleSprites[9] = new Texture(Gdx.files.internal("Idle (10).png"));

        attackSprites[0] = new Texture(Gdx.files.internal("Attack (1).png"));
        attackSprites[1] = new Texture(Gdx.files.internal("Attack (2).png"));
        attackSprites[2] = new Texture(Gdx.files.internal("Attack (3).png"));
        attackSprites[3] = new Texture(Gdx.files.internal("Attack (4).png"));
        attackSprites[4] = new Texture(Gdx.files.internal("Attack (5).png"));
        attackSprites[5] = new Texture(Gdx.files.internal("Attack (6).png"));
        attackSprites[6] = new Texture(Gdx.files.internal("Attack (7).png"));
        attackSprites[7] = new Texture(Gdx.files.internal("Attack (8).png"));
        attackSprites[8] = new Texture(Gdx.files.internal("Attack (9).png"));
        attackSprites[9] = new Texture(Gdx.files.internal("Attack (10).png"));

        currentImage = idleSprites[0];
    }

    public void update(final GameScreen gameScreen, final boolean jumpPressed, final boolean attackPressed, final boolean runLeft, final boolean runRight, final List<Monster> monsters) {
        if (!jumpPressed && !attackPressed && !runLeft && !runRight) {
            // stop run
            isRunning = false;
            // runTimer = 0;

            if (!isAttacking) {
                isIdling = true;
            }
        }

        // update touch events
        if (jumpPressed) {
            if (!ballIsJumping && rect.y <= GameScreen.BASE_GROUND_Y) {
                startJump();
            }
        }

        if (runRight) {
            walkX += WALK_SPEED * Gdx.graphics.getDeltaTime();
            direction = Direction.RIGHT;
            if (!isRunning) {
                startRun();
            }
        }

        if (runLeft) {
            walkX -= WALK_SPEED * Gdx.graphics.getDeltaTime();
            direction = Direction.LEFT;
            if (!isRunning) {
                startRun();
            }
        }

        if (attackPressed) {
            if (!isAttacking) {
                startAttack();
            }
        }

        // update the rest
        if (ballIsJumping) {
            updateJump();
        }

        if (isIdling) {
            updateIdle();
        }

        if (isRunning) {
            updateRun();
        }

        if (isAttacking) {
            updateAttack(monsters, gameScreen);
        }

        // pick up items
        Iterator<Item> iterator = gameScreen.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            Rectangle newRect = new Rectangle(item.getRect().x - walkX + 12, item.getRect().y + 12, item.getRect().width - 12, item.getRect().height - 12);
            if (newRect.overlaps(rect)) {
                if (item.getName().equals(Coin.ITEM_NAME)) {
                    gold++;
                }

                iterator.remove();
            }
        }
    }

    Rectangle attackHitBox;

    public void render(final GdxGame game) {
        boolean flip = (direction == Direction.LEFT);
        game.batch.draw(currentImage, flip ? rect.x + rect.width : rect.x, rect.y, flip ? - rect.width : rect.width, rect.height);

        /* if (attackHitBox != null && runSprites[8] != null) {
            game.batch.draw(runSprites[8], flip ? rect.x - 32 + 8 - extraAttackRange : rect.x + 64 - 8 + extraAttackRange, rect.y, 32, HEIGHT);
        } */
    }

    public void didGetAttacked(final Rectangle hitBox, final float damage, final DamageType damageType) {
        if (rect.overlaps(hitBox)) {
            currentHp -= damage;
        }
    }

    public long getGold() {
        return gold;
    }

    public long getExperience() {
        return experience;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    private void startJump() {
        jumpTimer = 0;
        ballIsJumping = true;
        isIdling = false;
    }

    private void startAttack() {
        attackTimer = 0;
        isAttacking = true;
        isIdling = false;
    }

    private void startRun() {
        runTimer = 0;
        isRunning = true;
        isIdling = false;
    }

    private void updateJump() {
        rect.y += 1300 * Gdx.graphics.getDeltaTime();

        jumpTimer++;
        if (jumpTimer == 15 || rect.y < GameScreen.BASE_GROUND_Y) {
            jumpTimer = 0;
            ballIsJumping = false;
            isIdling = true;
        }
    }

    private void updateRun() {
        currentImage = SpriteUtil.getCurrentSprite(runTimer, runSprites);
        runTimer = SpriteUtil.updateSpriteTimer(runTimer, runSprites);
    }

    private void updateIdle() {
        currentImage = SpriteUtil.getCurrentSprite(idleTimer, idleSprites);
        idleTimer = SpriteUtil.updateSpriteTimer(idleTimer, idleSprites);
    }

    private void updateAttack(List<Monster> monsters, final GameScreen gameScreen) {
        currentImage = SpriteUtil.getCurrentSprite(attackTimer, attackSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * extraAttackSpeedMultiplier);
        attackTimer = SpriteUtil.updateSpriteTimer(attackTimer, attackSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * extraAttackSpeedMultiplier);

        if (SpriteUtil.isAnimationFinished(attackTimer, attackSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * extraAttackSpeedMultiplier)) {
            attackTimer = 0;
            isAttacking = false;
            currentImage = idleSprites[0];
            isIdling = true;
        }

        if (attackTimer == 26) {
            boolean flip = (direction == Direction.LEFT);
            attackHitBox = new Rectangle(flip ? rect.x - 32 + 8 - extraAttackRange : rect.x + 64 - 8 + extraAttackRange, rect.y, 32, HEIGHT);
            for (Monster monster : monsters) {
                monster.didGetAttacked(gameScreen, attackHitBox, damage, damageType, walkX, this);
            }
        }
    }

    public void gainExperience(long experience) {
        this.experience += experience;
    }

    public float getWalkX() {
        return walkX;
    }

    public void setWalkX(float walkX) {
        this.walkX = walkX;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

}
