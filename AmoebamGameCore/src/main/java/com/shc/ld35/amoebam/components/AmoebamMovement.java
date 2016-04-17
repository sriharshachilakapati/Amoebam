package com.shc.ld35.amoebam.components;

import com.shc.ld35.amoebam.entities.Amoebam;
import com.shc.ld35.amoebam.states.PlayState;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class AmoebamMovement implements IComponent2D
{
    private Amoebam entity;

    private float jumpSpeed;
    private float fallSpeed;

    private boolean lockLeft;
    private boolean lockRight;

    @Override
    public void init(Entity2D entity2D)
    {
        this.entity = (Amoebam) entity2D;
    }

    @Override
    public void update(float deltaTime)
    {
        if (!entity.inRoll)
            entity.rotation = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || lockRight)
        {
            entity.position.x += (entity.inRoll ? 500 : 250) * deltaTime;
            entity.scale.x = 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || lockLeft)
        {
            entity.position.x -= (entity.inRoll ? 500 : 250) * deltaTime;
            entity.scale.x = -1;
        }

        if (entity.onGround)
            lockLeft = lockRight = false;

        if (Keyboard.isKeyTapped(Keyboard.KEY_UP) && !entity.inJump && entity.onGround)
        {
            lockLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
            lockRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);

            if (!lockLeft && !lockRight)
            {
                lockLeft = lockRight = true;
                entity.scale.x = 1;
            }

            entity.inJump = true;
            entity.position.y -= 5;
            jumpSpeed = 500;
        }

        if (!entity.inJump)
        {
            entity.position.y += fallSpeed * deltaTime;
            fallSpeed += 10;

            if (fallSpeed >= 500)
                fallSpeed = 500;
        }
        else
        {
            entity.position.y -= jumpSpeed * deltaTime;
            jumpSpeed -= 10;

            if (jumpSpeed <= 0)
            {
                fallSpeed = 0;
                entity.inJump = false;
            }

            if (entity.position.y <= 32)
                entity.inJump = false;
        }

        entity.onGround = false;

        entity.position.x = MathUtils.clamp(entity.position.x, 32, PlayState.currentLevel.width * 64 - 32);
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
