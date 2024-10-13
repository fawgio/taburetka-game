package io.itch.leftsock.taburetka;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class SettingsScreen extends TaburetkaScreen {
    Texture bg;
    Texture info;

    Rectangle infoHitbox = new Rectangle(VP_WIDTH/2+300,VP_HEIGHT - 150,100,100);
    Rectangle debugHitbox = new Rectangle(125,1500,830,100);

    static boolean debug = false;

    public SettingsScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");
        info = new Texture("info.png");

        fadeIn();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.4f,0.7f,0.5f,1);
        batch.begin();
        fontArial100.draw(batch,"Settings",VP_WIDTH/2-175,VP_HEIGHT-50);
        batch.draw(bg,100,50,880,1620);
        fontArial40.draw(batch,"["+(debug?"+":"-")+"] Debug",debugHitbox.getX(),debugHitbox.getY()+60,debugHitbox.getWidth(), Align.left,true);
        batch.draw(info,infoHitbox.getX(),infoHitbox.getY(),infoHitbox.getWidth(),infoHitbox.getHeight());
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchDown(screenX, screenY, pointer, button);
        try {
            if(infoHitbox.contains(touch.x,touch.y))
                fadeOut(InfoScreen.class);
            else if(debugHitbox.contains(touch.x,touch.y))
                debug = !debug;
            else
                fadeOut(MainMenu.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
