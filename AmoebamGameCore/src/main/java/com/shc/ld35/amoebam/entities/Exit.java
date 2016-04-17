package com.shc.ld35.amoebam.entities;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.components.SpriteRenderer;
import com.shc.ld35.amoebam.states.PlayState;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Exit extends Entity2D
{
    public Exit(int x, int y)
    {
        position.set(x + 32, y + 32);
        addComponent(new SpriteRenderer(new Sprite(Resources.Textures.EXIT)));
        addComponent(new CollisionComponent2D(Resources.CollisionTags.EXIT, new Rectangle(64, 64), this::onCollision));
    }

    private void onCollision(Entity2D self, CollisionComponent2D otherComponent)
    {
        PlayState.levelCompleted = true;
    }
}
