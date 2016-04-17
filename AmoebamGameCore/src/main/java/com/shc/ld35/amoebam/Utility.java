package com.shc.ld35.amoebam;

import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Matrix4;

/**
 * @author Sri Harsha Chilakapati
 */
public class Utility
{
    private static Matrix4 nullMatrix = new Matrix4().initIdentity();

    public static void drawTexture(Texture texture, float x, float y, float w, float h)
    {
        DynamicRenderer renderer = AmoebamGame.spriteRenderer;
        texture.bind(0);

        Resources.Programs.SPRITE_PROGRAM.setUniform("sprite", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("useView", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("entity", nullMatrix);
        Resources.Programs.SPRITE_PROGRAM.setUniform("camProj", Resources.CAMERA.getProjection());
        Resources.Programs.SPRITE_PROGRAM.setUniform("camView", Resources.CAMERA.getView());
        Resources.Programs.SPRITE_PROGRAM.use();

        renderer.setVertexLocation(Resources.Programs.SPRITE_PROGRAM.getAttribute("position"));
        renderer.setTexCoordLocation(Resources.Programs.SPRITE_PROGRAM.getAttribute("texCoords"));
        renderer.begin(Primitive.TRIANGLE_FAN);
        {
            renderer.vertex(x, y);
            renderer.texCoord(texture.getMinU(), texture.getMinV());

            renderer.vertex(x + w, y);
            renderer.texCoord(texture.getMaxU(), texture.getMinV());

            renderer.vertex(x + w, y + h);
            renderer.texCoord(texture.getMaxU(), texture.getMaxV());

            renderer.vertex(x, y + h);
            renderer.texCoord(texture.getMinU(), texture.getMaxV());
        }
        renderer.end();
    }
}
