package com.shc.ld35.amoebam;

import com.shc.ld35.amoebam.entities.Amoebam;
import com.shc.ld35.amoebam.entities.Enemy;
import com.shc.ld35.amoebam.entities.Ground;
import com.shc.ld35.amoebam.entities.Water;
import com.shc.silenceengine.scene.Scene2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Level
{
    public int width;
    public int height;

    public String levelData;

    public Level(String levelData)
    {
        this.levelData = levelData;
    }

    public void create(Scene2D scene)
    {
        scene.entities.clear();

        String[] lines = levelData.split("\n");

        int x = 0;
        int y = 0;

        for (String line : lines)
        {
            height++;
            width = Math.max(width, line.length());

            for (char c : line.toCharArray())
            {
                switch (c)
                {
                    case 'G':
                        scene.entities.add(new Ground(x, y));
                        break;

                    case 'W':
                        scene.entities.add(new Water(x, y));
                        break;

                    case 'A':
                        scene.entities.add(new Amoebam(x, y));
                        break;

                    case 'E':
                        scene.entities.add(new Enemy(x, y));
                        break;
                }

                x += 64; // Tile width
            }

            y += 64; // Tile height
            x = 0;
        }
    }
}
