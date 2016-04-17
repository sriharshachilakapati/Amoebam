package com.shc.ld35.amoebam.components;

import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.entities.Amoebam;
import com.shc.ld35.amoebam.entities.Bullet;
import com.shc.ld35.amoebam.states.PlayState;
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

        walking = new Sprite(Resources.Animations.AMOEBAM_SMALL);
        jumping = new Sprite(Resources.Animations.AMOEBAM);
        shootStart = new Sprite(Resources.Animations.AMOEBAM_SHOOT_START);
        shootEnd = new Sprite(Resources.Animations.AMOEBAM_SHOOT_STOP);

        walking.setEndCallback(walking::start);
        jumping.setEndCallback(jumping::start);

        shootStart.setStartCallback(() -> inShoot = true);

        shootStart.setEndCallback(() -> {
            shootEnd.start();
            renderer.sprite = shootEnd;

            PlayState.scene.entities.add(new Bullet(this.entity.position.x + (this.entity.scale.x == 1 ? 60 : -60),
                    this.entity.position.y - 10, this.entity.scale.x == 1));
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

    @Override
    public void render(float deltaTime)
    {
    }

    @Override
    public void dispose()
    {
    }

    private void changeSprite(Sprite sprite)
    {
        renderer.sprite.pause();
        renderer.sprite = sprite;
        renderer.sprite.start();
    }
}
