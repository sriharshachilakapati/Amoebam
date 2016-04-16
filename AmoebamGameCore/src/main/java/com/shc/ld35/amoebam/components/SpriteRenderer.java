package com.shc.ld35.amoebam.components;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Resources;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.components.TransformComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteRenderer implements IComponent2D
{
    public Sprite sprite;

    private boolean useView;
    private boolean useFrustum;
    private boolean useBatch;

    private Entity2D entity;

    private TransformComponent2D transformComponent;

    private static Rectangle tileRectangle = new Rectangle(64, 64);

    public SpriteRenderer(Sprite sprite)
    {
        this(sprite, true, true);
    }

    public SpriteRenderer(Sprite sprite, boolean useView, boolean useFrustum)
    {
        this(sprite, useView, useFrustum, true);
    }

    public SpriteRenderer(Sprite sprite, boolean useView, boolean useFrustum, boolean useBatch)
    {
        this.sprite = sprite;
        this.useView = useView;
        this.useFrustum = useFrustum;
        this.useBatch = useBatch;

        if (!useView)
            this.useBatch = false;
    }

    @Override
    public void init(Entity2D entity2D)
    {
        this.entity = entity2D;
        transformComponent = entity2D.getComponent(TransformComponent2D.class);
    }

    @Override
    public void update(float delta)
    {
        sprite.update(delta);
    }

    @Override
    public void render(float delta)
    {
        tileRectangle.setCenter(entity.position);

        if (useFrustum && !Resources.LEVEL_FRUSTUM.intersects(tileRectangle))
            return;

        if (useBatch)
            AmoebamGame.spriteBatch.add(sprite, transformComponent.transform);
        else
        {
            Texture texture = sprite.getCurrentFrame();
            texture.bind(0);

            Resources.Programs.SPRITE_PROGRAM.setUniform("entity", transformComponent.transform);
            Resources.Programs.SPRITE_PROGRAM.setUniform("sprite", 0);
            Resources.Programs.SPRITE_PROGRAM.setUniform("useView", useView ? 1 : 0);

            DynamicRenderer renderer = AmoebamGame.spriteRenderer;

            renderer.begin(Primitive.TRIANGLE_FAN);
            {
                renderer.vertex(-texture.getWidth() / 2, -texture.getHeight() / 2);
                renderer.texCoord(texture.getMinU(), texture.getMinV());

                renderer.vertex(texture.getWidth() / 2, -texture.getHeight() / 2);
                renderer.texCoord(texture.getMaxU(), texture.getMinV());

                renderer.vertex(texture.getWidth() / 2, texture.getHeight() / 2);
                renderer.texCoord(texture.getMaxU(), texture.getMaxV());

                renderer.vertex(-texture.getWidth() / 2, texture.getHeight() / 2);
                renderer.texCoord(texture.getMinU(), texture.getMaxV());
            }
            renderer.end();
        }
    }

    @Override
    public void dispose()
    {
    }
}
