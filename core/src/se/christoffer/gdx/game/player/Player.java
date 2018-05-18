package se.christoffer.gdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.christoffer.gdx.game.GameScreen;
import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.item.Coin;
import se.christoffer.gdx.game.item.HPPotion;
import se.christoffer.gdx.game.item.Item;
import se.christoffer.gdx.game.spells.Haste;
import se.christoffer.gdx.game.spells.Spell;
import se.christoffer.gdx.game.units.Monster;
import se.christoffer.gdx.game.units.Zombie;
import se.christoffer.gdx.game.util.DamageType;
import se.christoffer.gdx.game.util.Direction;
import se.christoffer.gdx.game.util.Logger;
import se.christoffer.gdx.game.util.RenderUtil;
import se.christoffer.gdx.game.util.SpriteUtil;

/**
 * Created by christofferlundqvist on 2017-06-09.
 */
public class Player {

    private float maxHp = 100;
    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;
    private static final float WALK_SPEED = 150f;

    private static Texture[] runSprites = new Texture[10];
    private static Texture[] attackSprites = new Texture[10];
    private static Texture[] idleSprites = new Texture[10];
    private static Texture[] deadSprites = new Texture[10];

    private Rectangle rect;
    private Texture currentImage;

    private float currentHp = maxHp;
    private float extraAttackRange = 0;
    private float extraAttackSpeedMultiplier = 1;
    private float extraMoveSpeedMultiplier = 1;

    private float damage = 10;
    private DamageType damageType = DamageType.PHYSICAL;
    private Direction direction = Direction.RIGHT;
    private float maxMana = 100f;
    private float currentMana = maxMana;
    private List<Spell> activeSpellBuffs = new ArrayList<Spell>();
    private List<Spell> spells = new ArrayList<Spell>();

    private long walkX = 0;
    private boolean isDead = false;
    private float deadTimer = 0;

    private float attackTimer = 0;
    private boolean isAttacking = false;

    private boolean isIdling = false;
    private float idleTimer = 0;

    private boolean isRunning = false;
    private float runTimer = 0;

    private boolean ballIsJumping = false;
    private float jumpTimer = 0;

    private int level = 1;
    private long gold = 0;
    private long experience = 0;
    private long experienceInCurrentLevel = 0;
    private long experiencePerLevel = 100;
    private float experienceMultiplier = 1f;

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

        deadSprites[0] = new Texture(Gdx.files.internal("Dead (1).png"));
        deadSprites[1] = new Texture(Gdx.files.internal("Dead (2).png"));
        deadSprites[2] = new Texture(Gdx.files.internal("Dead (3).png"));
        deadSprites[3] = new Texture(Gdx.files.internal("Dead (4).png"));
        deadSprites[4] = new Texture(Gdx.files.internal("Dead (5).png"));
        deadSprites[5] = new Texture(Gdx.files.internal("Dead (6).png"));
        deadSprites[6] = new Texture(Gdx.files.internal("Dead (7).png"));
        deadSprites[7] = new Texture(Gdx.files.internal("Dead (8).png"));
        deadSprites[8] = new Texture(Gdx.files.internal("Dead (9).png"));
        deadSprites[9] = new Texture(Gdx.files.internal("Dead (10).png"));

        currentImage = idleSprites[0];

        // add castable (and other?) spells
        spells.add(new Haste());
    }

    public void update(final GameScreen gameScreen, final List<Monster> monsters) {

        if (currentHp <= 0 && !isDead) {
            currentImage = SpriteUtil.getCurrentSprite(deadTimer, deadSprites);
            deadTimer = SpriteUtil.updateSpriteTimer(deadTimer, deadSprites);

            if (SpriteUtil.isAnimationFinished(deadTimer, deadSprites)) {
                isDead = true;
            }

            return;
        } else if (isDead) {
            return;
        }

        // check collision monsters
        for (Monster monster : monsters) {

            if (monster instanceof Zombie) {
                if (((Zombie) monster).isCloseTo(this.rect, getWalkX()) && !monster.isDead()) {
                    isAttacking = true;
                    isRunning = false;
                }
            }

            /*if (monster.getRect().overlaps(this.rect)) {
                isAttacking = true;
            } */
        }

        // update the rest
        if (ballIsJumping) {
            updateJump();
        }

        if (isIdling) {
            updateIdle();
        }

        if (isAttacking) {
            updateAttack(monsters, gameScreen);
        } else {

            // run
            walkX += WALK_SPEED * Gdx.graphics.getDeltaTime() * GameScreen.speedMultiplier * extraMoveSpeedMultiplier;
            direction = Direction.RIGHT;
            if (!isRunning) {
                startRun();
            }

        }

        if (isRunning) {
            updateRun();
        }

        // pick up items
        Iterator<Item> iterator = gameScreen.getItems().iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            Rectangle newRect = new Rectangle(item.getRect().x - walkX + 12, item.getRect().y + 12, item.getRect().width - 12, item.getRect().height - 12);
            if (newRect.overlaps(rect)) {

                boolean removeItem = true;

                if (item.getName().equals(Coin.ITEM_NAME)) {
                    gold++;
                }

                if (item.getName().equals(HPPotion.ITEM_NAME)) {
                    if (isFullHp()) {
                        removeItem = false;
                    } else {
                        HPPotion hpPotion = (HPPotion) item;
                        applyHeal(hpPotion.getHealAmount());
                    }
                }

                if (removeItem) {
                    iterator.remove();
                }
            }
        }

        // cast spells
        for (Spell spell: spells) {
            spell.update(gameScreen);
        }
    }

    Rectangle attackHitBox;

    private boolean isFullHp() {
        return currentHp == maxHp;
    }

    public void render(final GdxGame game) {
        boolean flip = (direction == Direction.LEFT);

        game.batch.begin();
        game.batch.draw(currentImage, flip ? rect.x + rect.width : rect.x, rect.y, flip ? - rect.width : rect.width, rect.height);
        game.batch.end();

        /* if (attackHitBox != null && runSprites[8] != null) {
            game.batch.draw(runSprites[8], flip ? rect.x - 32 + 8 - extraAttackRange : rect.x + 64 - 8 + extraAttackRange, rect.y, 32, HEIGHT);
        } */

        RenderUtil.drawHpBar(game, rect.x, rect.y + HEIGHT + 4, WIDTH, currentHp, maxHp);

        for (Spell spell: spells) {
            spell.render(game);
        }
    }

    public void didGetAttacked(final Rectangle hitBox, final float damage, final DamageType damageType) {
        if (rect.overlaps(hitBox)) {
            if (currentHp > 0) {
                currentHp = Math.max(currentHp - damage, 0);
            }
        }
    }

    public void giveGold(final float amount) {
        gold += amount;
    }

    public void applyHeal(final float amount) {
        Logger.log("Healing player with amount: " + amount);
        currentHp = Math.min(currentHp + amount, maxHp);
    }

    public void drainMana(final float amount) {
        currentMana = Math.max(currentMana - amount, 0);
    }

    public long getGold() {
        return gold;
    }

    public long getExperience() {
        return experience;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public int getLevel() {
        return level;
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
        currentImage = SpriteUtil.getCurrentSprite(runTimer, runSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * (1 / extraMoveSpeedMultiplier));
        runTimer = SpriteUtil.updateSpriteTimer(runTimer, runSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * (1 / extraMoveSpeedMultiplier));
    }

    private void updateIdle() {
        currentImage = SpriteUtil.getCurrentSprite(idleTimer, idleSprites);
        idleTimer = SpriteUtil.updateSpriteTimer(idleTimer, idleSprites);
    }

    private void updateAttack(List<Monster> monsters, final GameScreen gameScreen) {
        currentImage = SpriteUtil.getCurrentSprite(attackTimer, attackSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * (1 / extraAttackSpeedMultiplier));
        attackTimer = SpriteUtil.updateSpriteTimer(attackTimer, attackSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * (1 / extraAttackSpeedMultiplier));

        if (SpriteUtil.isAnimationFinished(attackTimer, attackSprites, SpriteUtil.SPRITE_TICK_INTERVAL_DEFAULT * (1 / extraAttackSpeedMultiplier))) {
            attackTimer = 0;
            isAttacking = false;
            currentImage = idleSprites[0];
            isIdling = true;
        }

        //se.christoffer.gdx.game.util.Logger.log("extraAttackSpeedMultiplier: " + extraAttackSpeedMultiplier + ", attackTimer: " + attackTimer);

        if (attackTimer == 26 / (Math.max(extraAttackSpeedMultiplier - 1, 1))) {
            boolean flip = (direction == Direction.LEFT);
            attackHitBox = new Rectangle(flip ? rect.x - 32 + 8 - extraAttackRange : rect.x + 64 - 8 + extraAttackRange, rect.y, 32, HEIGHT);
            for (Monster monster : monsters) {
                monster.didGetAttacked(gameScreen, attackHitBox, damage, damageType, walkX, this);
            }
        }
    }

    public void gainExperience(long experience) {
        long addedExperience = (long) (experience * experienceMultiplier);

        this.experienceInCurrentLevel += addedExperience;

        if (experienceInCurrentLevel >= experiencePerLevel) {
            experienceInCurrentLevel = experienceInCurrentLevel - experiencePerLevel;
            level++;
        }

        this.experience += addedExperience;
    }

    public long getWalkX() {
        return walkX;
    }

    public void setWalkX(long walkX) {
        this.walkX = walkX;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(float currentMana) {
        this.currentMana = currentMana;
    }

    public void addSpellBuff(Spell spell) {
        activeSpellBuffs.add(spell);
        if (spell.isBuff() && spell instanceof Haste) {
            Haste haste = (Haste) spell;
            extraAttackSpeedMultiplier += haste.getAttackSpeedMultiplier();
            extraMoveSpeedMultiplier += haste.getWalkSpeedMultiplier();
        }
    }

    public void removeSpellBuff(Spell spell) {
        activeSpellBuffs.remove(spell);
        if (spell.isBuff() && spell instanceof Haste) {
            Haste haste = (Haste) spell;
            extraAttackSpeedMultiplier -= haste.getAttackSpeedMultiplier();
            extraMoveSpeedMultiplier -= haste.getWalkSpeedMultiplier();
        }
    }

}
