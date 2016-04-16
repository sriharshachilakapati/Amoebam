package com.shc.ld35.amoebam.components;

import com.shc.ld35.amoebam.entities.Amoebam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

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
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || lockRight)
            entity.position.x += 250 * deltaTime;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || lockLeft)
            entity.position.x -= 250 * deltaTime;

        if (entity.onGround)
            lockLeft = lockRight = false;

        if (Keyboard.isKeyTapped(Keyboard.KEY_UP) && !entity.inJump && entity.onGround)
        {
            lockLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
            lockRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);

            if (!lockLeft && !lockRight)
                lockLeft = lockRight = true;

            entity.inJump = true;
            entity.position.y -= 5;
            jumpSpeed = 500;
        }

        if (!entity.inJump)
        {
            entity.position.y += fallSpeed * deltaTime;
            fallSpeed += 10;

            if (fallSpeed >= 500)
                fallSpeed = 0;
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
        }

        entity.onGround = false;
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
