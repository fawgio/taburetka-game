package io.itch.leftsock.taburetka;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameScreen extends TaburetkaScreen {
    Texture cell;
    Texture plus;
    Texture arrow;
    Texture unitTexture;

    Player renderingPlayer;

    Cell[][] map;
    private Player[] players;

    Rectangle[] uiElements;
    private int next = 1;
    private Label label;
    private Label log;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        plus = new Texture("plus.png");
        arrow = new Texture("arrow.png");
        cell = new Texture("cell.png");
        unitTexture = new Texture("unit.png");

        LevelGenerator.gs = this;
        players = LevelGenerator.players();
        map = LevelGenerator.spawns(LevelGenerator.map(), players);
        renderingPlayer = players[0];

        uiElements = new Rectangle[]{
                new Rectangle(10*2,10*2,100*2,100*2),
                new Rectangle(430*2,10*2,100*2,100*2),
                new Rectangle(10*2,860*2.5f,100*2, 100*2),
                SettingsScreen.debug?new Rectangle(200*2,430*2,200*2,400*2):null,
        };
        label = new Label("",skin);
        label.setFontScale(2);
        label.setX(uiElements[2].getX());
        label.setY(uiElements[2].getY());
        label.setWidth(uiElements[2].getWidth());
        label.setHeight(uiElements[2].getHeight());
        label.setColor(renderingPlayer.getColor());
        label.setText(renderingPlayer.getName()+"'s move");
        log = new Label("", skin);
        if (SettingsScreen.debug) {
            log.setX(uiElements[3].getX());
            log.setY(uiElements[3].getY());
            log.setWidth(uiElements[3].getWidth());
            log.setHeight(uiElements[3].getHeight());
            log.setColor(0, 0, 0, 1);
            log.setFontScale(0.5f);
        }
        Image add = new Image(plus);
        add.setX(uiElements[0].getX());
        add.setY(uiElements[0].getY());
        add.setWidth(uiElements[0].getWidth());
        add.setHeight(uiElements[0].getHeight());
        Image nextMove = new Image(arrow);
        nextMove.setX(uiElements[1].getX());
        nextMove.setY(uiElements[1].getY());
        nextMove.setWidth(uiElements[1].getWidth());
        nextMove.setHeight(uiElements[1].getHeight());
        ui.addActor(label);
        ui.addActor(add);
        ui.addActor(nextMove);
        if (SettingsScreen.debug)
            ui.addActor(log);

        fadeIn();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(1f,1f,1f,1f);
        batch.begin();
        for (Cell[] line:
                map) {
            for (Cell cell:
                    line) {
                if(cell!=null) {
                    if(cell.equals(renderingPlayer.getSelected()))
                        batch.setColor(0.5f,0.5f,0.5f,1);
                    batch.draw(this.cell, cell.getHitbox().getX(), cell.getHitbox().getY(), cell.getHitbox().getWidth(), cell.getHitbox().getHeight());
                    batch.setColor(1f,1f,1f,1);
                    if(cell.hasUnit()) {
                        batch.setColor(cell.getUnit().getPlayer().getColor());
                        batch.draw(unitTexture, cell.getHitbox().getX(), cell.getHitbox().getY(), cell.getHitbox().getWidth(), cell.getHitbox().getHeight());
                        batch.setColor(1,1,1,1);
                    }
                }
            }
        }
        batch.end();

        if(Gdx.input.isTouched()&&touch!=null){
            Vector3 vec = cam.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).sub(touch);
            cam.translate(vec.scl(-1));
            batch.setProjectionMatrix(cam.combined);
            viewport.apply();
            touch = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0));
        }
        ui.draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean res = super.touchDown(screenX, screenY, pointer, button);
        Vector3 vec = cam.project(cam.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)));
        for (int i = 0; i < uiElements.length; i++) {
            if(uiElements[i] == null) continue;
            if(uiElements[i].contains(vec.x,vec.y)){
                switch (i){
                    case 0:
                        if(renderingPlayer.getSelected() instanceof Cell)
                            if(!((Cell)renderingPlayer.getSelected()).hasUnit()&&!renderingPlayer.acted()) {
                                Cell cell1, cell;
                                cell = (Cell) renderingPlayer.getSelected();
                                int j = ((int) ((cell.getHitbox().getY() - 25) / 50));
                                int i1 = ((int) ((cell.getHitbox().getX() - 25) / 50));
                                boolean hasNeighbourUnit = false;
                                for (Cell[] r:
                                        map) {
                                    for (Cell neighbour:
                                         r) {
                                        if (neighbour!=null&&neighbour.isNextTo(map[i1][j])&&neighbour.hasUnit()&&neighbour.getUnit().getPlayer().equals(renderingPlayer))
                                            hasNeighbourUnit = true;
                                    }
                                }
                                if (!hasNeighbourUnit) break;
                                ((Cell) renderingPlayer.getSelected()).setUnit(new Unit((Cell) renderingPlayer.getSelected(), renderingPlayer));
                                log.setText(log.getText()+" "+renderingPlayer.getName()+" placed new unit\n");
                            }
                            else
                                renderingPlayer.setSelected(((Cell) renderingPlayer.getSelected()).getUnit());
                        break;
                    case 1:
                        end();
                }
                return res;
            }
        }
        for (Cell[] line:
                map) {
            for (Cell cell:
                    line) {
                if(cell!=null&&!dragged && cell.getHitbox().contains(touch.x, touch.y)){
                    if (renderingPlayer.getSelected() instanceof Cell || renderingPlayer.getSelected() == null)
                        renderingPlayer.setSelected(cell.equals(renderingPlayer.getSelected())?cell.hasUnit()?cell.getUnit():null:cell);
                    else if (renderingPlayer.getSelected() instanceof Unit)
                        if(((Unit)renderingPlayer.getSelected()).can(renderingPlayer,cell)) {
                            Vector2 vector2 = new Vector2((((Unit) renderingPlayer.getSelected()).getCell().getHitbox().getX()-25)/50,((((Unit) renderingPlayer.getSelected())).getCell().getHitbox().getY()-25)/50);
                            ((Unit) renderingPlayer.getSelected()).move(cell);
                            log.setText(log.getText()+renderingPlayer.getName()+" moved a unit from "+vector2+" to \n\t"+
                                    new Vector2((((Unit) renderingPlayer.getSelected()).getCell().getHitbox().getX()-25)/50,((((Unit) renderingPlayer.getSelected())).getCell().getHitbox().getY()-25)/50)
                                    +"\n");
                        }
                        else
                            renderingPlayer.setSelected(cell.equals(renderingPlayer.getSelected())?cell.hasUnit()?cell.getUnit():null:cell);
                return true;
                }
            }
        }
        return res;
    }

    public void end() {
        log.setText(log.getText()+" "+renderingPlayer.getName()+" has ended move\n\n");
        if(next == players.length)
            next();
        do {
            renderingPlayer = players[next++];
            ArrayList<ArrayList<Unit>> units = new ArrayList<>();
            for (Player ignored :
                    players) {
                units.add(new ArrayList<>());
            }
            for (Cell[] cells :
                    map) {
                for (Cell cell :
                        cells) {
                    if(cell!=null&&cell.hasUnit()) {
                        units.get(Arrays.asList(players).indexOf(cell.getUnit().getPlayer())).add(cell.getUnit());
                    }
                }
            }
            if (units.get(Arrays.asList(players).indexOf(renderingPlayer)).isEmpty()){
                renderingPlayer.lose();
            }
            if (next == players.length)
                next();
        }while (renderingPlayer.lost());
        label.setColor(renderingPlayer.getColor());
        label.setText(renderingPlayer.getName()+"'s move");
    }

    void next() {
        log.setText(log.getText()+" Preparing for next move\n");
        ArrayList<ArrayList<Unit>> units = new ArrayList<>();
        for (Player ignored :
                players) {
            units.add(new ArrayList<>());
        }
        for (Cell[] cells :
                map) {
            for (Cell cell :
                    cells) {
                if(cell!=null){
                    cell.update();
                    if (cell.hasUnit()) {
                        units.get(Arrays.asList(players).indexOf(cell.getUnit().getPlayer())).add(cell.getUnit());
                    }
                }
            }
        }
        ArrayList<Player> fighting = new ArrayList<>();
        for (Player player :
                players) {
            if (units.get(Arrays.asList(players).indexOf(player)).isEmpty()){
                player.lose();
            } else
                fighting.add(player);
        }
        if (fighting.size() == 1){
            log.setText(log.getText()+" "+fighting.get(0).getName()+" has won\n");
            try {
                Thread.sleep(1000);
                fadeOut(MainMenu.class);
            } catch (InterruptedException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        next = 0;
        log.setText(log.getText()+" Next move\n----\n");
    }
}
