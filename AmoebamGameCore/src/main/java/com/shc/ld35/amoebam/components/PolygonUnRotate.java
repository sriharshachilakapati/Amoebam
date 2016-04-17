package com.shc.ld35.amoebam.components;

import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class PolygonUnRotate implements IComponent2D
{
    private CollisionComponent2D component;

    @Override
    public void init(Entity2D entity)
    {
        component = entity.getComponent(CollisionComponent2D.class);
    }

    @Override
    public void update(float deltaTime)
    {
        component.polygon.setRotation(0); // Force the rotation to be zero
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
