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

    final GdxGame game;
    public static Logger log = new Logger("Game");

    private static final int VIEWPORT_WIDTH = 800;
    private static final int VIEWPORT_HEIGHT = 480;

    public static float GRAVITY = 3.8f;
    public static final float BASE_GROUND_Y = 70f;

    private Player player;

    private float bgAdded = 0;
    private float bgAdded2 = VIEWPORT_WIDTH;

    private Texture jumpButton;
    private Texture attackButton;

    Texture dropImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;

    private Texture backgroundImage;
    private Texture backgroundImage2;

    private List<Monster> monsters = new ArrayList<Monster>();

    public GameScreen(final GdxGame gam) {
        this.game = gam;

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));

        jumpButton = new Texture(Gdx.files.internal("jumpButton.png"));
        attackButton = new Texture(Gdx.files.internal("attackButton.png"));

        Zombie.loadAssets();

        monsters.add(new Zombie((int) (Math.random() * 3000), BASE_GROUND_Y));
        monsters.add(new Zombie((int) (Math.random() * 3000), BASE_GROUND_Y));
        monsters.add(new Zombie((int) (Math.random() * 3000), BASE_GROUND_Y));
        monsters.add(new Zombie((int) (Math.random() * 3000), BASE_GROUND_Y));
        monsters.add(new Zombie((int) (Math.random() * 3000), BASE_GROUND_Y));
        monsters.add(new Zombie((int) (Math.random() * 3000), BASE_GROUND_Y));

        backgroundImage = new Texture(Gdx.files.internal("bg3.jpg"));
        backgroundImage2 = new Texture(Gdx.files.internal("bg3.jpg"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        player = new Player(VIEWPORT_WIDTH / 6, BASE_GROUND_Y);
    }

    private void applyGravity() {
        if (player.getRect().y >= BASE_GROUND_Y) {
            player.getRect().setY(player.getRect().y - GRAVITY * 200 * Gdx.graphics.getDeltaTime());
        }
    }

    float first = 0;
    float second = 0;

    private static final int actionButtonWidth = 150;
    private static final int actionButtonHeight = 150;
    private static final int attackButtonIndex = 2;
    private static final int jumpButtonIndex = 1;

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

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();

        float bgAdded = - player.getWalkX();
        float bgAdded2 = VIEWPORT_WIDTH - player.getWalkX();

        if (bgAdded < -VIEWPORT_WIDTH) {
            bgAdded = bgAdded2 + VIEWPORT_WIDTH;
        }

        if (bgAdded2 < -VIEWPORT_WIDTH) {
            bgAdded2 = bgAdded + VIEWPORT_WIDTH;
        }

        game.batch.draw(backgroundImage, bgAdded, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        game.batch.draw(backgroundImage2, bgAdded2, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        game.font.draw(game.batch, "player.x, player.y: " + player.getRect().x + ", " + player.getRect().y, 0, VIEWPORT_HEIGHT);
        game.font.draw(game.batch, "walkX: " + player.getWalkX(), 0, VIEWPORT_HEIGHT - 50);

        game.batch.end();

        boolean jumpPressed = false;
        boolean attackPressed = false;
        boolean runLeft = false;
        boolean runRight = false;

        for (int i = 0; i < 20; i++) { // 20 is max number of touch points
            if (Gdx.input.isTouched(i)) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touchPos);

                first = touchPos.x;
                second = touchPos.y;

                if (touchPos.x > (VIEWPORT_WIDTH - actionButtonWidth * jumpButtonIndex) && touchPos.y > (VIEWPORT_HEIGHT - actionButtonHeight)) {
                    jumpPressed = true;
                } else if (touchPos.x > (VIEWPORT_WIDTH - actionButtonWidth * attackButtonIndex) && touchPos.x <= (VIEWPORT_WIDTH - actionButtonWidth) && touchPos.y > (VIEWPORT_HEIGHT - actionButtonHeight)) {
                    attackPressed = true;
                }

                if (touchPos.x > VIEWPORT_WIDTH - 200 && touchPos.y <= (VIEWPORT_HEIGHT - actionButtonHeight)) {
                    // move right
                    runRight = true;
                } else if (touchPos.x < 200) {
                    // move left
                    runLeft = true;
                }
            }
        }

        // process user input
        player.update(jumpPressed, attackPressed, runLeft, runRight, monsters);

        // remove invalid monsters?
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

        game.batch.begin();

        player.render(game);

        // touchPos.x > (VIEWPORT_WIDTH - 180) && touchPos.y > (VIEWPORT_HEIGHT - 180)
        game.batch.draw(jumpButton, VIEWPORT_WIDTH - actionButtonWidth * jumpButtonIndex, VIEWPORT_HEIGHT - actionButtonHeight, actionButtonWidth, actionButtonHeight);

        // touchPos.x > (VIEWPORT_WIDTH - 360) && touchPos.x <= (VIEWPORT_WIDTH - 180) && touchPos.y > (VIEWPORT_HEIGHT - 180)
        game.batch.draw(attackButton, VIEWPORT_WIDTH - actionButtonWidth * attackButtonIndex, VIEWPORT_HEIGHT - actionButtonHeight, actionButtonWidth, actionButtonHeight);


        game.batch.end();

        applyGravity();
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
        dropImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        // TODO dispose player + monster images????
    }

}