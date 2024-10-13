package io.itch.leftsock.taburetka;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.reflect.InvocationTargetException;

public class MainMenu extends TaburetkaScreen{
    Texture title;
    Texture play;
    Texture settings;

    Rectangle settingsHitbox;
    Rectangle playHitbox;

    public MainMenu(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        title = new Texture("title.png");
        play = new Texture("play.png");
        playHitbox = new Rectangle(VP_WIDTH/2 - play.getWidth() / 2,VP_HEIGHT/2 - play.getHeight() / 2,play.getWidth(),play.getHeight());
        settings = new Texture("settings.png");
        settingsHitbox = new Rectangle(VP_WIDTH/2 - 150 / 2 - 300,VP_HEIGHT/2 - 150 / 2,150,150);

        fade = 1;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.4f,0.7f,0.5f,1);
        batch.begin();
        batch.draw(title,VP_WIDTH/2 - title.getWidth() / 2,VP_HEIGHT/2 - title.getHeight() / 2 + 299);
        batch.draw(play,playHitbox.getX(),playHitbox.getY(),playHitbox.getWidth(),playHitbox.getHeight());
        batch.draw(settings,settingsHitbox.getX(),settingsHitbox.getY(),settingsHitbox.getWidth(),settingsHitbox.getHeight());

        if(Gdx.input.isTouched()&&touch!=null){
            if(settingsHitbox.contains(touch.x,touch.y)){
                try {
                    fadeOut(SettingsScreen.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if(playHitbox.contains(touch.x,touch.y)){
                try {
                    fadeOut(CreateLevelScreen.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        batch.end();
    }

}
