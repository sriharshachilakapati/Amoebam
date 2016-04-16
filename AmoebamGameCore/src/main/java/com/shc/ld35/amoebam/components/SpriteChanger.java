package com.shc.ld35.amoebam.components;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.entities.Amoebam;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteChanger implements IComponent2D
{
    private Amoebam        entity;
    private SpriteRenderer renderer;

    private Sprite walking;
    private Sprite jumping;
    private Sprite shootStart;
    private Sprite shootEnd;

    private boolean inShoot = false;

    @Override
    public void init(Entity2D entity)
    {
        this.entity = (Amoebam) entity;
        renderer = entity.getComponent(SpriteRenderer.class);

        walking = new Sprite(Resources.Animations.AMOEBAM_SMALL_WALK);
        jumping = new Sprite(Resources.Animations.AMOEBAM_WALK);
        shootStart = new Sprite(Resources.Animations.AMOEBAM_SHOOT_START);
        shootEnd = new Sprite(Resources.Animations.AMOEBAM_SHOOT_STOP);

        walking.setEndCallback(walking::start);
        jumping.setEndCallback(jumping::start);

        shootStart.setStartCallback(() -> inShoot = true);

        shootStart.setEndCallback(() -> {
            shootEnd.start();
            renderer.sprite = shootEnd;
        });

        shootEnd.setEndCallback(() -> {
            walking.start();
            renderer.sprite = walking;
            inShoot = false;
        });

        renderer.sprite = walking;
        walking.start();
    }

    @Override
    public void update(float deltaTime)
    {
        if (inShoot)
            return;

        if (entity.inJump || !entity.onGround)
            changeSprite(jumping);

        if (!entity.inJump && entity.onGround)
            changeSprite(walking);

        if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE))
            changeSprite(shootStart);
    }

    private void changeSprite(Sprite sprite)
    {
        renderer.sprite.pause();
        renderer.sprite = sprite;
        renderer.sprite.start();
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
