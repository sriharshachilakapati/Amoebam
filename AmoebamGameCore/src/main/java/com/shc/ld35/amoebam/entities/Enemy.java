package com.shc.ld35.amoebam.entities;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.components.EnemyMovement;
import com.shc.ld35.amoebam.components.SpriteRenderer;
import com.shc.ld35.amoebam.states.PlayState;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Enemy extends Entity2D
{
    public boolean moving;

    public Enemy(int x, int y)
    {
        this(x, y, false);
    }

    public Enemy(int x, int y, boolean moving)
    {
        this.moving = moving;
        scale.x = -1;

        position.set(x + 32, y + 20);
        addComponent(new SpriteRenderer(new Sprite(Resources.Animations.ENEMY)));

        if (moving)
            addComponent(new EnemyMovement());

        addComponent(new CollisionComponent2D(Resources.CollisionTags.ENEMY, new Rectangle(64, 96), this::onCollision));
    }

    private void onCollision(Entity2D self, CollisionComponent2D otherComponent)
    {
        if (otherComponent.tag == Resources.CollisionTags.BULLET)
        {
            Entity2D entity = new Entity2D();
            entity.position.set(position).y += 20;
            entity.addComponent(new SpriteRenderer(new Sprite(Resources.Animations.AMOEBAM_SMALL), true, true, false));

            PlayState.score += 10;

            PlayState.scene.entities.add(entity);
            PlayState.scene.entities.remove(self);
            PlayState.scene.entities.remove(otherComponent.entity);

            Resources.Sounds.EXPLOSION.play();
        }

        if (otherComponent.tag == Resources.CollisionTags.GROUND)
            if (position.y < otherComponent.entity.position.y)
                position.y = otherComponent.entity.position.y - 75;
    }
}
