package io.itch.leftsock.taburetka;

import com.badlogic.gdx.graphics.Texture;

public class Unit {
    public int x,y;
    private Cell cell;
    private Player player;

    public Unit(Cell cell, Player player){
        this.cell = cell;
        this.player = player;
        player.act();
    }

    public void move(Cell cell) {
        this.cell.setUnit(null);
        setCell(cell);
        player.act();
    }

    public void setCell(Cell cell) {
        if(cell==null)return;
        this.cell = cell;
        cell.setUnit(this);
    }

    public Cell getCell() {
        return cell;
    }

    public boolean can(Player player, Cell cell) {
        return !this.player.acted() && this.player.equals(player) && this.getCell().isNextTo(cell);
    }

    public void update() {
        player.update();
    }

    public Player getPlayer(){
        return player;
    }

    public void move(int i, int i1) {
        Cell to = cell.gs.map[i][i1];
        if (to!=null) {
            cell.setUnit(null);
            move(to);
        }
    }
}
