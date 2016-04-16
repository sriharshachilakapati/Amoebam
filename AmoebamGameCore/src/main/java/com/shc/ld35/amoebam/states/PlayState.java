package com.shc.ld35.amoebam.states;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Level;
import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.entities.Clouds;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.scene.Scene2D;

import static com.shc.ld35.amoebam.AmoebamGame.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static Level level;

    private Scene2D scene;
    private Matrix4 background;

    private SceneCollider2D collider;

    @Override
    public void onEnter()
    {
        background = new Matrix4().initIdentity();

        Program program = Resources.Programs.SPRITE_PROGRAM;

        DynamicRenderer renderer = AmoebamGame.spriteRenderer;
        renderer.setVertexLocation(program.getAttribute("position"));
        renderer.setTexCoordLocation(program.getAttribute("texCoords"));

        program.setUniform("camProj", Resources.CAMERA.getProjection());
        program.setUniform("camView", Resources.CAMERA.getView());
        program.use();

        scene = new Scene2D();
        scene.entities.add(new Clouds(0));
        scene.entities.add(new Clouds(GAME_WIDTH / 2));
        scene.entities.add(new Clouds(GAME_WIDTH));

        level = new Level(scene, "levels/level1.lvl");

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(scene);

        collider.register(Resources.CollisionTags.AMOEBAM, Resources.CollisionTags.GROUND);
    }

    @Override
    public void update(float delta)
    {
        if (!level.loaded)
            return;

        scene.update(delta);
        collider.checkCollisions();

        Resources.CAMERA.apply();
        Resources.LEVEL_FRUSTUM.update(Resources.CAMERA);

        Program program = Resources.Programs.SPRITE_PROGRAM;
        program.setUniform("camProj", Resources.CAMERA.getProjection());
        program.setUniform("camView", Resources.CAMERA.getView());
    }

    @Override
    public void render(float delta)
    {
        if (!level.loaded)
            return;

        DynamicRenderer renderer = AmoebamGame.spriteRenderer;
        Texture texture = Resources.Textures.BACKGROUND;
        texture.bind(0);

        Resources.Programs.SPRITE_PROGRAM.setUniform("sprite", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("useView", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("entity", background);
        Resources.Programs.SPRITE_PROGRAM.use();

        renderer.begin(Primitive.TRIANGLE_FAN);
        {
            renderer.vertex(0, 0);
            renderer.texCoord(texture.getMinU(), texture.getMinV());

            renderer.vertex(GAME_WIDTH, 0);
            renderer.texCoord(texture.getMaxU(), texture.getMinV());

            renderer.vertex(GAME_WIDTH, GAME_HEIGHT);
            renderer.texCoord(texture.getMaxU(), texture.getMaxV());

            renderer.vertex(0, GAME_HEIGHT);
            renderer.texCoord(texture.getMinU(), texture.getMaxV());
        }
        renderer.end();

        scene.render(delta);

        AmoebamGame.spriteBatch.render();

        Resources.Programs.FONT_PROGRAM.use();

        String text = "FPS: " + SilenceEngine.gameLoop.getFPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10, 10, Color.WHITE, text);

        text = "UPS: " + SilenceEngine.gameLoop.getUPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10,
                10 + Resources.FONT.getHeight(), Color.WHITE, text);
    }

    @Override
    public void onLeave()
    {
    }
}
