package io.itch.leftsock.taburetka;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class InfoScreen extends TaburetkaScreen {
    Texture bg;
    public InfoScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("bg.png");

        fadeIn();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.4f,0.7f,0.5f,1);
        batch.begin();
        fontArial100.draw(batch,"About",VP_WIDTH/2-175,VP_HEIGHT-50);
        batch.draw(bg,100,50,880,1620);
        fontArial40.draw(batch,"Taburetka - is a turn-based strategy.\n" +
                "The game map consists of cells. Some cells \n" +
                "are empty. Each player has 1 unit placed\n" +
                " on the map. You \n" +
                " can place 1 unit near\n" +
                "another unit or move a unit to a neighbour\n" +
                " cell on your move. \n" +
                "If you move your unit to a cell where another\n" +
                " player's unit stays \n" +
                "you will eat it. Last player that has units wins.",120,1600);
        batch.end();

        if(Gdx.input.isTouched()){
            try {
                fadeOut(SettingsScreen.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
