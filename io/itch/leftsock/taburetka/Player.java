package io.itch.leftsock.taburetka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Player {
    private Object selected;
    private Color color;
    private String name;
    private boolean act;
    private boolean lose;

    public Object getSelected() {
        return selected;
    }

    public void setSelected(Object selected) {
        this.selected = selected;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void act(){
        act = true;
    }

    public boolean acted(){
        return act;
    }

    public void update(){
        if (!act) return;
        act = false;
        selected = null;
    }

    public void lose() {
        lose = true;
    }

    public boolean lost() {
        return lose;
    }
}
