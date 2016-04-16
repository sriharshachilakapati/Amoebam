package com.shc.ld35.amoebam.entities;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.components.SpriteRenderer;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.scene.entity.Entity2D;

import static com.shc.ld35.amoebam.AmoebamGame.GAME_WIDTH;

/**
 * @author Sri Harsha Chilakapati
 */
public class Clouds extends Entity2D
{
    public Clouds(float offset)
    {
        position.set(GAME_WIDTH / 2 + offset, Resources.Textures.CLOUDS.getHeight() / 2 + 100);
        addComponent(new SpriteRenderer(new Sprite(Resources.Textures.CLOUDS), false, false));

        scale.set(2);
    }

    @Override
    protected void onUpdate(float deltaTime)
    {
        position.x -= 100 * deltaTime;

        if (position.x < -Resources.Textures.CLOUDS.getWidth())
            position.x = GAME_WIDTH + Resources.Textures.CLOUDS.getWidth();
    }
}
