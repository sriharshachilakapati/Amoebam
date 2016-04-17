package com.shc.ld35.amoebam.entities;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.components.SpriteRenderer;
import com.shc.ld35.amoebam.states.PlayState;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Bullet extends Entity2D
{
    private boolean movingRight;
    private Polygon polygon;

    public Bullet(float x, float y, boolean movingRight)
    {
        position.set(x + 16, y);
        this.movingRight = movingRight;

        addComponent(new SpriteRenderer(new Sprite(Resources.Textures.BULLET)));
        addComponent(new CollisionComponent2D(Resources.CollisionTags.BULLET, polygon = new Rectangle(32, 32)));
    }

    @Override
    protected void onUpdate(float deltaTime)
    {
        position.x += (movingRight ? 600 : -600) * deltaTime;
        rotation += 360 * deltaTime;

        polygon.setCenter(position);

        if (!Resources.LEVEL_FRUSTUM.intersects(polygon))
            PlayState.scene.entities.remove(this);
    }
}
