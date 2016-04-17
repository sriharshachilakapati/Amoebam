package com.shc.ld35.amoebam.components;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.entities.Enemy;
import com.shc.ld35.amoebam.states.PlayState;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class EnemyMovement implements IComponent2D
{
    private Enemy entity;
    private Polygon polygon;

    @Override
    public void init(Entity2D entity)
    {
        this.entity = (Enemy) entity;
        polygon = new Rectangle(64, 96);
    }

    @Override
    public void update(float deltaTime)
    {
        polygon.setCenter(entity.position);

        if (!Resources.LEVEL_FRUSTUM.intersects(polygon))
            return;

        entity.position.x += ((entity.scale.x == 1) ? 250 : -250) * deltaTime;
        entity.position.y += 300 * deltaTime;

        if (entity.position.x <= 32)
        {
            entity.position.x = 32;
            entity.scale.x = 1;
        }

        if (entity.position.x >= PlayState.currentLevel.width * 64 - 32)
        {
            entity.position.x = PlayState.currentLevel.width * 64 - 32;
            entity.scale.x = -1;
        }
    }

    @Override
    public void render(float deltaTime)
    {
    }

    @Override
    public void dispose()
    {
    }
}
