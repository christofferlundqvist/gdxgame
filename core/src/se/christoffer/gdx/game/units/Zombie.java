package se.christoffer.gdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import se.christoffer.gdx.game.GdxGame;
import se.christoffer.gdx.game.item.Item;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.util.DamageType;
import se.christoffer.gdx.game.util.Direction;

/**
 * Created by christofferlundqvist on 2017-05-26.
 */
public class Zombie extends Monster {

    private static final float WIDTH = 64;
    private static final float HEIGHT = 64;
    private static final float WALK_SPEED = 50;
    private static final float ATTACK_RANGE = 2; // extra px attack range
    private static final float ATTACK_SPEED_MULTIPLIER = 10; //
    private static final float MAX_HP = 50;
    private static final float ATTACK_DAMAGE = 5;
    private static final DamageType DAMAGE_TYPE = DamageType.PHYSICAL;

    private static Texture[] walkSprites = new Texture[10];
    private static Texture[] attackSprites = new Texture[8];
    private static Texture[] deadSprites = new Texture[12];

    private static final Map<String, Float> itemDrops = new HashMap<String, Float>();

    private Texture currentImage;
    private int walkTimer;
    private int attackTimer;
    private int deadTimer;

    private Rectangle attackHitBox;

    static {
        itemDrops.put("Coin", 50f);
    }

    public Zombie(float x, float y) {
        rect = new Rectangle(x, y, WIDTH, HEIGHT);
        state = State.MOVING;
        currentHp = MAX_HP;
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

        // currentImage = walkSprites[0];
    }

    @Override
    public void onDeath() {
        for (Map.Entry<String, Float> entry : itemDrops.entrySet()) {
            if ((int) (Math.random() * 100) <= entry.getValue()) {
                if (entry.getKey().equals("Coin")) {
                    
                }
            }
        }
    }

    private void updateDead() {
        deadTimer++;

        int currentIndex = 0;

        if (deadTimer <= 5) {
            currentIndex = 0;
        } else if (deadTimer <= 10) {
            currentIndex = 1;
        } else if (deadTimer <= 15) {
            currentIndex = 2;
        } else if (deadTimer <= 20) {
            currentIndex = 3;
        } else if (deadTimer <= 25) {
            currentIndex = 4;
        } else if (deadTimer <= 30) {
            currentIndex = 5;
        } else if (deadTimer <= 35) {
            currentIndex = 6;
        } else if (deadTimer <= 40) {
            currentIndex = 7;
        } else if (deadTimer <= 45) {
            currentIndex = 8;
        } else if (deadTimer <= 50) {
            currentIndex = 9;
        } else if (deadTimer <= 55) {
            currentIndex = 10;
        } else if (deadTimer <= 60) {
            currentIndex = 11;
        }

        currentImage = deadSprites[currentIndex];

        if (deadTimer == 60) {
            state = State.INVALID;
        }
    }

    private void updateAttack(final Player player, final float walkX) {
        attackTimer++;

        int currentIndex = 0;

        if (attackTimer <= 5) {
            currentIndex = 0;
        } else if (attackTimer <= 10) {
            currentIndex = 1;
        } else if (attackTimer <= 15) {
            currentIndex = 2;
        } else if (attackTimer <= 20) {
            currentIndex = 3;
        } else if (attackTimer <= 25) {
            currentIndex = 4;
        } else if (attackTimer <= 30) {
            currentIndex = 5;
        } else if (attackTimer <= 35) {
            currentIndex = 6;
        } else if (attackTimer <= 40) {
            currentIndex = 7;
        }

        currentImage = attackSprites[currentIndex];

        if (attackTimer == 40) {
            attackTimer = 0;
            state = State.IDLE;
        }

        if (rect.x >= player.getRect().x + 32 + walkX) {
            direction = Direction.LEFT;
        } else if (rect.x < player.getRect().x - 32 + walkX) {
            direction = Direction.RIGHT;
        }

        if (attackTimer == 16) {
            boolean flip = (direction == Direction.LEFT);
            attackHitBox = new Rectangle(flip ? rect.x - walkX - 16 : rect.x - walkX + WIDTH, rect.y, 16, HEIGHT);
            player.didGetAttacked(attackHitBox, ATTACK_DAMAGE, DAMAGE_TYPE);
        }

    }

    private void updateWalk(final Player player, final float walkX) {
        walkTimer++;

        int currentIndex = 0;

        if (walkTimer <= 5) {
            currentIndex = 0;
        } else if (walkTimer <= 10) {
            currentIndex = 1;
        } else if (walkTimer <= 15) {
            currentIndex = 2;
        } else if (walkTimer <= 20) {
            currentIndex = 3;
        } else if (walkTimer <= 25) {
            currentIndex = 4;
        } else if (walkTimer <= 30) {
            currentIndex = 5;
        } else if (walkTimer <= 35) {
            currentIndex = 6;
        } else if (walkTimer <= 40) {
            currentIndex = 7;
        } else if (walkTimer <= 45) {
            currentIndex = 8;
        } else if (walkTimer <= 50) {
            currentIndex = 9;
        }

        currentImage = walkSprites[currentIndex];

        if (walkTimer == 50) {
            walkTimer = 0;
        }

        if (rect.x >= player.getRect().x + player.getRect().width / 2 + walkX) {
            rect.x -= WALK_SPEED * Gdx.graphics.getDeltaTime();
            direction = Direction.LEFT;
        } else if (rect.x < player.getRect().x - player.getRect().width / 2 + walkX) {
            rect.x += WALK_SPEED * Gdx.graphics.getDeltaTime();
            direction = Direction.RIGHT;
        }
    }

    private boolean isCloseTo(final Rectangle target, final float walkX) {
        float x = target.x + target.width / 2 + walkX;
        float y = target.y + target.width / 2; // + walkY

        double distance = Math.sqrt( (rect.x + WIDTH / 2 - x) * (rect.x + WIDTH / 2 - x) + (rect.y + HEIGHT / 2 - y) * (rect.y + HEIGHT / 2 - y) );
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
        game.batch.draw(currentImage, flip ? rect.x + WIDTH - walkX: rect.x - walkX, rect.y, flip ? - WIDTH : WIDTH, HEIGHT);
        game.batch.end();
    }

}
