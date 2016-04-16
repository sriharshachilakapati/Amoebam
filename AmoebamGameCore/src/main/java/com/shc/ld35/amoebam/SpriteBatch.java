package com.shc.ld35.amoebam;

import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteBatch
{
    private List<Sprite>    sprites;
    private List<Transform> transforms;
    private List<Integer>   indices; // Used to sort both sprites and transforms at once

    private Matrix4 entity;

    public SpriteBatch()
    {
        sprites = new ArrayList<>();
        transforms = new ArrayList<>();
        indices = new ArrayList<>();

        entity = new Matrix4().initIdentity();
    }

    public void add(Sprite sprite, Transform transform)
    {
        sprites.add(sprite);
        transforms.add(transform);
        indices.add(sprites.size() - 1);
    }

    public void render()
    {
        if (indices.size() == 0)
            return;

        // Learnt a quick note, list.sort is not available in GWT, but Collections.sort is present
        Collections.sort(indices, (o1, o2) ->
                sprites.get(o1).getCurrentFrame().getID() - sprites.get(o2).getCurrentFrame().getID());

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        DynamicRenderer renderer = AmoebamGame.spriteRenderer;

        Texture currentTexture = sprites.get(indices.get(0)).getCurrentFrame();
        currentTexture.bind(0);

        int triangles = 0;

        Resources.Programs.SPRITE_PROGRAM.setUniform("entity", entity);
        Resources.Programs.SPRITE_PROGRAM.setUniform("sprite", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("useView", 1);
        Resources.Programs.SPRITE_PROGRAM.setUniform("camView", Resources.CAMERA.getView());

        renderer.begin(Primitive.TRIANGLES);
        {
            for (int index : indices)
            {
                Sprite sprite = sprites.get(index);
                Transform transform = transforms.get(index);

                Texture texture = sprite.getCurrentFrame();

                if (currentTexture.getID() != texture.getID() || triangles >= 50)
                {
                    currentTexture = texture;
                    renderer.end();
                    renderer.begin(Primitive.TRIANGLES);
                    currentTexture.bind(0);

                    triangles = 0;
                }

                renderer.vertex(temp.set(-texture.getWidth() / 2, -texture.getHeight() / 2, 0)
                        .multiply(transform.getMatrix()));
                renderer.texCoord(texture.getMinU(), texture.getMinV());

                renderer.vertex(temp.set(texture.getWidth() / 2, -texture.getHeight() / 2, 0)
                        .multiply(transform.getMatrix()));
                renderer.texCoord(texture.getMaxU(), texture.getMinV());

                renderer.vertex(temp.set(-texture.getWidth() / 2, texture.getHeight() / 2, 0)
                        .multiply(transform.getMatrix()));
                renderer.texCoord(texture.getMinU(), texture.getMaxV());

                renderer.vertex(temp.set(texture.getWidth() / 2, -texture.getHeight() / 2, 0)
                        .multiply(transform.getMatrix()));
                renderer.texCoord(texture.getMaxU(), texture.getMinV());

                renderer.vertex(temp.set(texture.getWidth() / 2, texture.getHeight() / 2, 0)
                        .multiply(transform.getMatrix()));
                renderer.texCoord(texture.getMaxU(), texture.getMaxV());

                renderer.vertex(temp.set(-texture.getWidth() / 2, texture.getHeight() / 2, 0)
                        .multiply(transform.getMatrix()));
                renderer.texCoord(texture.getMinU(), texture.getMaxV());

                triangles += 2;
            }
        }
        renderer.end();
        Vector3.REUSABLE_STACK.push(temp);

        indices.clear();
        sprites.clear();
        transforms.clear();
    }
}
