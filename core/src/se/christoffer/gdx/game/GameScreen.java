package se.christoffer.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.christoffer.gdx.game.item.Coin;
import se.christoffer.gdx.game.item.Item;
import se.christoffer.gdx.game.player.Player;
import se.christoffer.gdx.game.units.Monster;
import se.christoffer.gdx.game.units.Zombie;

/**
 * Created by christofferlundqvist on 2017-05-12.
 *
 * sprites from:
 *      http://www.gameart2d.com/freebies.html
 *      http://www.gameart2d.com/the-knight-free-sprites.html
 *
 *      http://libgdx.badlogicgames.com/download.html
 *      https://github.com/libgdx/libgdx/wiki/Extending-the-simple-game
 *      https://gist.github.com/sinistersnare/6367829
 */
public class GameScreen implements Screen {

    public final GdxGame game;
    public static Logger log = new Logger("Game");

    private static final int VIEWPORT_WIDTH = 480;
    private static final int VIEWPORT_HEIGHT = 800;

    public static float GRAVITY = 3.8f;
    public static final float BASE_GROUND_Y = 150f;

    private Player player;

    private float bgAdded = 0;
    private float bgAdded2 = VIEWPORT_WIDTH;

    private Texture jumpButton;
    private Texture attackButton;

    OrthographicCamera camera;

    private Texture backgroundImage;
    private Texture backgroundImage2;
    private int bg1X = 0;
    private int bg2X = VIEWPORT_WIDTH;
    private float lastBgX = 0;
    private float lastBgX2 = 0;

    private List<Monster> monsters = new ArrayList<Monster>();
    private List<Item> items = new ArrayList<Item>();

    private int level = 1;
    private static final float LEVEL_LENGTH = 5000;
    private static final int MONSTER_WAVES_PER_LEVEL = 7;
    private static final float MONSTER_WAVE_SPREAD_X = 200;
    private static final float MONSTER_WAVE_SPREAD_Y = 50;

    public GameScreen(final GdxGame gam) {
        this.game = gam;

        jumpButton = new Texture(Gdx.files.internal("jumpButton.png"));
        attackButton = new Texture(Gdx.files.internal("attackButton.png"));

        Zombie.loadAssets();

        loadLevel();

        backgroundImage = new Texture(Gdx.files.internal("background/Game_Background_17.png"));

        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        backgroundImage2 = new Texture(Gdx.files.internal("background/Game_Background_17.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        player = new Player(VIEWPORT_WIDTH / 6, BASE_GROUND_Y);
    }

    private void loadLevel() {
        level++;

        float spreadY = (float) (Math.random() * MONSTER_WAVE_SPREAD_Y);
        float negOrPosSpreadY = spreadY - spreadY * 2;

        float spreadX = (float) (Math.random() * MONSTER_WAVE_SPREAD_X);

        monsters.add(new Zombie((float) (Math.random() * level * LEVEL_LENGTH) + 70f + spreadX, BASE_GROUND_Y + negOrPosSpreadY));
        monsters.add(new Zombie((float) (Math.random() * level * LEVEL_LENGTH) + 70f + spreadX, BASE_GROUND_Y + negOrPosSpreadY));
        monsters.add(new Zombie((float) (Math.random() * level * LEVEL_LENGTH) + 70f + spreadX, BASE_GROUND_Y + negOrPosSpreadY));
        monsters.add(new Zombie((float) (Math.random() * level * LEVEL_LENGTH) + 70f + spreadX, BASE_GROUND_Y + negOrPosSpreadY));
        monsters.add(new Zombie((float) (Math.random() * level * LEVEL_LENGTH) + 70f + spreadX, BASE_GROUND_Y + negOrPosSpreadY));
        monsters.add(new Zombie((float) (Math.random() * level * LEVEL_LENGTH) + 70f + spreadX, BASE_GROUND_Y + negOrPosSpreadY));
    }

    private void applyGravity() {
        if (player.getRect().y > BASE_GROUND_Y) {
            player.getRect().setY(player.getRect().y - GRAVITY * 200 * Gdx.graphics.getDeltaTime());
        } else {
            player.getRect().setY(BASE_GROUND_Y);
        }
    }

    private void renderBackground() {
        if (lastBgX + VIEWPORT_WIDTH < 0) {
            bg1X += VIEWPORT_WIDTH;
        }

        if (lastBgX2 < 0) {
            bg2X += VIEWPORT_WIDTH;
        }

        lastBgX = bg1X - player.getWalkX();
        lastBgX2 = bg2X - player.getWalkX();
        game.batch.draw(backgroundImage, lastBgX, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        game.batch.draw(backgroundImage2, lastBgX2, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();

        // draw background / UI
        renderBackground();
        game.font.draw(game.batch, "Gold: " + player.getGold(), 0, VIEWPORT_HEIGHT);
        game.font.draw(game.batch, "Exp: " + player.getExperience(), 0, VIEWPORT_HEIGHT - 25);
        game.font.draw(game.batch, "HP: " + player.getCurrentHp(), 0, VIEWPORT_HEIGHT - 50);
        game.font.draw(game.batch, "Level: " + level, 0, VIEWPORT_HEIGHT - 75);

        game.batch.end();

        /*
        boolean jumpPressed = false;
        boolean attackPressed = false;
        boolean runLeft = false;
        boolean runRight = false;

        for (int i = 0; i < 20; i++) { // 20 is max number of touch points
            if (Gdx.input.isTouched(i)) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touchPos);

                if (touchPos.x > (VIEWPORT_WIDTH - actionButtonWidth * 2) && touchPos.y > (VIEWPORT_HEIGHT - actionButtonHeight)) {
                    jumpPressed = true;
                } else if (touchPos.x < (actionButtonWidth * 2) && touchPos.y > (VIEWPORT_HEIGHT - attackButtonHeight)) {
                    attackPressed = true;
                }

                if (touchPos.x > VIEWPORT_WIDTH - 200 && touchPos.y <= (VIEWPORT_HEIGHT - attackButtonHeight)) {
                    // move right
                    runRight = true;
                } else if (touchPos.x < 200 && !attackPressed) {
                    // move left
                    runLeft = true;
                }
            }
        } */

        // process user input
        player.update(this, monsters);

        // remove invalid monsters
        Iterator<Monster> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            if (monster.getState() == Monster.State.INVALID) {
                iterator.remove();
            }
        }

        for (Monster monster : monsters) {
            monster.update(player, player.getWalkX());
            monster.render(game, player.getWalkX());
        }

        // update/render coins first
        for (Item item : items) {
            if (item.getName().equals(Coin.ITEM_NAME)) {
                item.update(this, player, player.getWalkX());
                item.render(this, player.getWalkX());
            }
        }

        // update/render rest of items
        for (Item item : items) {
            if (!item.getName().equals(Coin.ITEM_NAME)) {
                item.update(this, player, player.getWalkX());
                item.render(this, player.getWalkX());
            }
        }


        player.render(game);

        applyGravity();

        // next level?
        if (player.getWalkX() >= level * LEVEL_LENGTH) {
            loadLevel();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        // rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

        // TODO dispose player + monster images????
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

}