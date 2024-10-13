package io.itch.leftsock.taburetka;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class LevelGenerator {
    private static final Random rand = new Random();
    public static GameScreen gs;
    public static int playerAmount;
    public static int mwidth, mheight;

    public static Cell[][] map(){
        Cell[][] map = new Cell[mheight][mwidth];

        float c1, c2;
        c1 = 1 - 0.3f;
        c2 = 1 - 0.05f;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(
                        (map[i>0?i-1:i][j>0?j-1:j] != null && rand.nextFloat()>c1) ||
                        (map[i>0?i-1:i][j] != null && rand.nextFloat()>c1) ||
                        (map[i][j>0?j-1:j] != null && rand.nextFloat()>c1) ||
                        (map[i<map.length-1?i+1:i][j<map[0].length-1?j+1:j] != null && rand.nextFloat()>c1) ||
                        (map[i<map.length-1?i+1:i][j] != null && rand.nextFloat()>c1) ||
                        (map[i][j<map[0].length-1?j+1:j] != null && rand.nextFloat()>c1) ||
                        (map[i<map.length-1?i+1:i][j>0?j-1:j] != null && rand.nextFloat()>c1) ||
                        (map[i>0?i-1:i][j<map[0].length-1?j+1:j] != null && rand.nextFloat()>c1) ||
                        (map[i>0?i-1:i][j>0?j-1:j] == null && rand.nextFloat()>c2) ||
                        (map[i>0?i-1:i][j] == null && rand.nextFloat()>c2) ||
                        (map[i][j>0?j-1:j] == null && rand.nextFloat()>c2) ||
                        (map[i<map.length-1?i+1:i][j<map[0].length-1?j+1:j] == null && rand.nextFloat()>c2) ||
                        (map[i<map.length-1?i+1:i][j] == null && rand.nextFloat()>c2) ||
                        (map[i][j<map[0].length-1?j+1:j] == null && rand.nextFloat()>c2) ||
                        (map[i<0?i+1:i][j>0?j-1:j] == null && rand.nextFloat()>c2) ||
                        (map[i>0?i-1:i][j<map[0].length-1?j+1:j] == null && rand.nextFloat()>c2)
                    ) {
                    map[i][j] = new Cell(new Rectangle(25 + i * 50, 25 + j * 50, 50, 50));
                    map[i][j].gs = gs;
                }
            }
        }
        return map;
    }

    public static Cell[][] spawns(Cell[][] map, Player[] players){
        for (Player player : players) {
            Cell cell;
            int i = 0;
            do {
                if(i > map().length*map[0].length)
                    throw new RuntimeException("Too much players or too small map!");
                cell = map[rand.nextInt(map.length)][rand.nextInt(map[0].length)];
                i++;
            } while (cell==null||cell.hasUnit());
            cell.setUnit(new Unit(cell, player));
        }
        return map;
    }

    public static Player[] players(){
        Player[] players = new Player[playerAmount];
        for (int i = 0; i < players.length; i++) {
            Player player = new Player();
            player.setColor(new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1));
            player.setName(name());
            players[i] = player;
        }
        return players;
    }

    private static String name() {
        String[] syls = new String[]{"qe","we","ro","na","qa","ta","ji","xi","mo","ru","u","a"};
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 1+rand.nextInt(3); i++) {
            s.append(syls[rand.nextInt(syls.length)]);
        }
        return s.toString().replaceFirst(s.substring(0,1),s.substring(0,1).toUpperCase());
    }
}
