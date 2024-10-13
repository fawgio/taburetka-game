package io.itch.leftsock.taburetka;

import com.badlogic.gdx.math.Rectangle;

public class Cell {
    private Rectangle hitbox;
    private Unit unit;
    public GameScreen gs;

    public Cell(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public boolean hasUnit() {
        return unit!=null;
    }

    public void update() {
        if (hasUnit())
            unit.update();
    }

    public boolean isNextTo(Cell cell) {
        return Math.abs(getHitbox().getY() - cell.getHitbox().getY())<=50 &&
                Math.abs(getHitbox().getX() - cell.getHitbox().getX())<=50;
    }
}
