package com.shc.ld35.amoebam.states;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Level;
import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.StateProvider;
import com.shc.ld35.amoebam.Utility;
import com.shc.ld35.amoebam.entities.Clouds;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.scene.Scene2D;

import static com.shc.ld35.amoebam.AmoebamGame.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static Level   currentLevel;
    public static Scene2D scene;

    public static boolean levelCompleted;
    public static boolean levelFailed;

    public static int score;

    private float levelBonus;

    private final Level level;

    private Matrix4         background;
    private SceneCollider2D collider;

    private StateProvider stateProvider;

    public PlayState(Level level, StateProvider stateProvider)
    {
        this.level = level;
        this.stateProvider = stateProvider;
    }

    @Override
    public void onEnter()
    {
        background = new Matrix4().initIdentity();
        levelCompleted = false;
        levelFailed = false;

        Program program = Resources.Programs.SPRITE_PROGRAM;

        DynamicRenderer renderer = AmoebamGame.spriteRenderer;
        renderer.setVertexLocation(program.getAttribute("position"));
        renderer.setTexCoordLocation(program.getAttribute("texCoords"));

        program.setUniform("camProj", Resources.CAMERA.getProjection());
        program.setUniform("camView", Resources.CAMERA.getView());
        program.use();

        scene = new Scene2D();
        currentLevel = level;
        currentLevel.create(scene);

        scene.entities.add(new Clouds(0));
        scene.entities.add(new Clouds(GAME_WIDTH / 2));
        scene.entities.add(new Clouds(GAME_WIDTH));

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(scene);

        collider.register(Resources.CollisionTags.AMOEBAM, Resources.CollisionTags.GROUND);
        collider.register(Resources.CollisionTags.AMOEBAM, Resources.CollisionTags.ENEMY);
        collider.register(Resources.CollisionTags.AMOEBAM, Resources.CollisionTags.WATER);
        collider.register(Resources.CollisionTags.ENEMY, Resources.CollisionTags.BULLET);
        collider.register(Resources.CollisionTags.ENEMY, Resources.CollisionTags.GROUND);
        collider.register(Resources.CollisionTags.EXIT, Resources.CollisionTags.AMOEBAM);

        levelBonus = 60;
    }

    @Override
    public void update(float delta)
    {
        levelBonus -= delta;
        levelBonus = Math.max(0, levelBonus);

        scene.update(delta);
        collider.checkCollisions();

        Resources.CAMERA.apply();
        Resources.LEVEL_FRUSTUM.update(Resources.CAMERA);

        Program program = Resources.Programs.SPRITE_PROGRAM;
        program.setUniform("camProj", Resources.CAMERA.getProjection());
        program.setUniform("camView", Resources.CAMERA.getView());

        if (levelCompleted)
        {
            AmoebamGame.instance.setGameState(stateProvider.provide());
            score += levelBonus;
        }

        if (levelFailed)
            AmoebamGame.instance.setGameState(this);

        if (Keyboard.isKeyTapped(Keyboard.KEY_ENTER))
            levelCompleted = true;

        if (Keyboard.isKeyTapped(Keyboard.KEY_R))
            levelFailed = true;
    }

    @Override
    public void render(float delta)
    {
        Utility.drawTexture(Resources.Textures.BACKGROUND, 0, 0, GAME_WIDTH, GAME_HEIGHT);

        scene.render(delta);
        AmoebamGame.spriteBatch.render();

        String text = "FPS: " + SilenceEngine.gameLoop.getFPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10, 10, Color.WHITE, text);

        text = "UPS: " + SilenceEngine.gameLoop.getUPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10,
                10 + Resources.FONT.getHeight(), Color.WHITE, text);

        score = Math.max(0, score);
        Resources.FONT.render(10, 10, Color.WHITE, "SCORE: " + score);
        Resources.FONT.render(10, 10 + Resources.FONT.getHeight(), Color.WHITE, "BONUS: " + (int) levelBonus);
    }

    @Override
    public void onLeave()
    {
    }
}
