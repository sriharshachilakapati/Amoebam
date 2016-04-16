package com.shc.ld35.amoebam;

import com.shc.ld35.amoebam.entities.Amoebam;
import com.shc.ld35.amoebam.entities.Ground;
import com.shc.ld35.amoebam.entities.Water;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.scene.Scene2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Level
{
    public boolean loaded = false;

    public int width;
    public int height;

    public Level(Scene2D scene, String fileName)
    {
        FileReader fileReader = SilenceEngine.io.getFileReader();

        fileReader.readTextFile(FilePath.getResourceFile(fileName), string ->
        {
            String[] lines = string.split("\n");

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
                    }

                    x += 64; // Tile width
                }

                y += 64; // Tile height
                x = 0;
            }

            loaded = true;
        });
    }
}
