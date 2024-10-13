package io.itch.leftsock.taburetka;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateLevelScreen extends TaburetkaScreen {
    Rectangle[] uiElements;

    TextField amount;
    TextField mheight;
    TextField mwidth;

    public CreateLevelScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        uiElements = new Rectangle[]{
                new Rectangle(VP_WIDTH/2-750/2,0*VP_HEIGHT/5,750,250),
                new Rectangle(VP_WIDTH/2-750/2,VP_HEIGHT/5,750,250),
                new Rectangle(VP_WIDTH/2-750/2,2*VP_HEIGHT/5,750,250),
                new Rectangle(VP_WIDTH/2-750/2,3*VP_HEIGHT/5,750,250),
                new Rectangle(VP_WIDTH/2-750/2,4*VP_HEIGHT/5,750,250),
        };

        TextButton play = new TextButton("start",skin);
        play.setX(uiElements[0].getX());
        play.setY(uiElements[0].getY());
        play.setWidth(uiElements[0].getWidth());
        play.setHeight(uiElements[0].getHeight());

        amount = new TextField("Amount of players",skin);
        amount.setX(uiElements[1].getX());
        amount.setY(uiElements[1].getY());
        amount.setWidth(uiElements[1].getWidth());
        amount.setHeight(uiElements[1].getHeight());

        mheight = new TextField("Map height",skin);
        mheight.setX(uiElements[2].getX());
        mheight.setY(uiElements[2].getY());
        mheight.setWidth(uiElements[2].getWidth());
        mheight.setHeight(uiElements[2].getHeight());

        mwidth = new TextField("Map width",skin);
        mwidth.setX(uiElements[3].getX());
        mwidth.setY(uiElements[3].getY());
        mwidth.setWidth(uiElements[3].getWidth());
        mwidth.setHeight(uiElements[3].getHeight());
        
        Label label = new Label("Create level",skin,"h");
        label.setAlignment(Align.center);
        label.setX(uiElements[4].getX());
        label.setY(uiElements[4].getY());
        label.setWidth(uiElements[4].getWidth());
        label.setHeight(uiElements[4].getHeight());
        
        ui.addActor(amount);
        ui.addActor(mwidth);
        ui.addActor(mheight);
        ui.addActor(play);
        ui.addActor(label);
        ui.setViewport(viewport);

        fadeIn();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.4f,0.7f,0.5f,1);
        ui.draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchDown(screenX, screenY, pointer, button);
        //Vector3 vec = viewport.project(viewport.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)));
        for (int i = 0; i < uiElements.length; i++) {
            if(uiElements[i] == null) continue;
            if(uiElements[i].contains(touch.x,touch.y)){
                switch (i){
                    case 0:
                        try {
                            LevelGenerator.playerAmount = Integer.parseInt(amount.getText());
                            LevelGenerator.mwidth = Integer.parseInt(mwidth.getText());
                            LevelGenerator.mheight = Integer.parseInt(mheight.getText());
                            fadeOut(GameScreen.class);
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 1:
                        amount.setText("");
                        break;
                    case 2:
                        mheight.setText("");
                        break;
                    case 3:
                        mwidth.setText("");
                        break;
                }
                return res;
            }
        }
        return res;
    }
}
