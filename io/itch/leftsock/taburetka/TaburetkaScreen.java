package io.itch.leftsock.taburetka;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;

import java.lang.reflect.InvocationTargetException;

public abstract class TaburetkaScreen  implements Screen, InputProcessor {

    public static final float VP_WIDTH = 1080;
    public static final float VP_HEIGHT = 2400;
    Game game;
    Viewport viewport;
    Camera cam;

    Batch batch;
    BitmapFont fontArial100;
    BitmapFont fontArial40;
    BitmapFont fontArial20;

    float fade = 0f;
    private boolean fadingOut;
    private boolean fadingIn;
    private Class<? extends TaburetkaScreen> goTo;
    public Vector3 touch = new Vector3(0,0,0);
    public boolean dragged;
    public Stage ui;
    public Skin skin;
    public TaburetkaScreen(Game game){
        this.game = game;
        cam = new OrthographicCamera(VP_WIDTH,VP_HEIGHT);
        viewport = new StretchViewport(VP_WIDTH, VP_HEIGHT,cam);
        cam.translate(VP_WIDTH/2,VP_HEIGHT/2,0);
        viewport.apply();
    } 
    
    @Override
    public void show() {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0f,0f,VP_WIDTH,VP_HEIGHT);
        ui = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        fontArial100 = new BitmapFont(Gdx.files.internal("arial_100.fnt"));
        fontArial100.setColor(new Color(0,0,0,1));
        fontArial40 = new BitmapFont(Gdx.files.internal("arial_40.fnt"));
        fontArial40.setColor(new Color(0,0,0,1));
        fontArial20 = new BitmapFont(Gdx.files.internal("arial.fnt"));
        fontArial20.setColor(new Color(0,0,0,1));
        Gdx.input.setInputProcessor(this);

        if (SettingsScreen.debug) ui.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        try {
            if (fadingOut) {
                fadeOut(goTo);
            }
            if (fadingIn) {
                fadeIn();
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void fadeOut(Class<? extends TaburetkaScreen> screenClass) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        fade-=4*Gdx.graphics.getDeltaTime();
        fadingOut = fade > 0;
        batch.setColor(1,1,1,fade);
        fontArial100.setColor(0,0,0,fade);
        fontArial40.setColor(0,0,0,fade);
        fontArial20.setColor(0,0,0,fade);
        goTo = screenClass;
        if (!fadingOut){
            //dispose();
            game.setScreen(screenClass.getConstructor(Game.class).newInstance(game));
        }
        Gdx.app.debug("Taburetka:TaburetkaScreen:fadeOut() ","fade out "+this.getClass().getName());
    }

    public void fadeIn() {
        fade += 4 * Gdx.graphics.getDeltaTime();
        fadingIn = fade < 1;
        batch.setColor(1, 1, 1, fade);
        fontArial100.setColor(0, 0, 0, fade);
        fontArial40.setColor(0, 0, 0, fade);
        fontArial20.setColor(0, 0, 0, fade);
        Gdx.app.debug("Taburetka:TaburetkaScreen:fadeIn() ", "fade in " + this.getClass().getName());
    }
    @Override
    public boolean keyDown(int keycode) {
        return ui.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return ui.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return ui.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0));
        return ui.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch = null;
        dragged = false;
        return ui.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragged = true;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
