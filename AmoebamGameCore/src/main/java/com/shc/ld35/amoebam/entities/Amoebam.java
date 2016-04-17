package com.shc.ld35.amoebam.entities;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.components.AmoebamMovement;
import com.shc.ld35.amoebam.components.PolygonUnRotate;
import com.shc.ld35.amoebam.components.SpriteChanger;
import com.shc.ld35.amoebam.components.SpriteRenderer;
import com.shc.ld35.amoebam.states.PlayState;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

import static com.shc.ld35.amoebam.AmoebamGame.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Amoebam extends Entity2D
{
    public boolean onGround;
    public boolean inJump;
    public boolean inRoll;

    public Amoebam(float x, float y)
    {
        // Because the entity position is it's center!
        position.set(x + 32, y + 50);

        addComponent(new SpriteRenderer(null, true, true, false));
        addComponent(new SpriteChanger());
        addComponent(new AmoebamMovement());
        addComponent(new CollisionComponent2D(Resources.CollisionTags.AMOEBAM, new Rectangle(64, 64), this::onCollision));
        addComponent(new PolygonUnRotate());
    }

    private void onCollision(Entity2D self, CollisionComponent2D otherComponent)
    {
        onGround = inJump = false;

        if (otherComponent.tag == Resources.CollisionTags.GROUND)
        {
            if (position.y < otherComponent.entity.position.y)
            {
                onGround = true;
                position.y = otherComponent.entity.position.y - 58;
            }
        }

        if (otherComponent.tag == Resources.CollisionTags.ENEMY)
        {
            PlayState.score -= 100;
            PlayState.levelFailed = true;
        }

        if (otherComponent.tag == Resources.CollisionTags.WATER)
        {
            if (position.y >= otherComponent.entity.position.y + 20)
            {
                PlayState.score -= 100;
                PlayState.levelFailed = true;

                Resources.Sounds.EXPLOSION.play();
            }
        }
    }

    @Override
    protected void onUpdate(float deltaTime)
    {
        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        temp.set(position);

        if (temp.x <= GAME_WIDTH / 2)
            temp.x = GAME_WIDTH / 2;

        if (temp.y <= GAME_HEIGHT / 2)
            temp.y = GAME_HEIGHT / 2;

        if (temp.x >= PlayState.currentLevel.width * 64 - GAME_WIDTH / 2)
            temp.x = PlayState.currentLevel.width * 64 - GAME_WIDTH / 2;

        if (temp.y >= PlayState.currentLevel.height * 64 - GAME_HEIGHT / 2)
            temp.y = PlayState.currentLevel.height * 64 - GAME_HEIGHT / 2;

        Resources.CAMERA.center(temp);

        Vector2.REUSABLE_STACK.push(temp);
    }
}
