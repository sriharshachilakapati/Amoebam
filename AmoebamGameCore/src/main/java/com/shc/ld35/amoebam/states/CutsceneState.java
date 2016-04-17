package com.shc.ld35.amoebam.states;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.StateProvider;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Matrix4;

import static com.shc.ld35.amoebam.AmoebamGame.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class CutsceneState extends GameState
{
    private float   duration;
    private float   alpha;
    private String  text;
    private boolean decreaseAlpha;

    private Matrix4 background = new Matrix4();

    private StateProvider provider;

    public CutsceneState(String text, float duration, StateProvider provider)
    {
        this.text = text;
        this.duration = duration;

        if (duration == -1)
            this.duration = 1;

        this.provider = provider;
    }

    @Override
    public void update(float delta)
    {
        alpha += (decreaseAlpha ? -1 : 1) * (delta / duration) * 2;

        if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE))
        {
            AmoebamGame.instance.setGameState(provider.provide());
            return;
        }

        if (!decreaseAlpha && alpha >= 1f)
            decreaseAlpha = true;

        if (decreaseAlpha && alpha <= 0f)
            AmoebamGame.instance.setGameState(provider.provide());
    }

    @Override
    public void render(float delta)
    {
        DynamicRenderer renderer = AmoebamGame.spriteRenderer;
        Texture texture = Resources.Textures.BACKGROUND;
        texture.bind(0);

        Resources.Programs.SPRITE_PROGRAM.setUniform("sprite", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("useView", 0);
        Resources.Programs.SPRITE_PROGRAM.setUniform("entity", background);
        Resources.Programs.SPRITE_PROGRAM.setUniform("camProj", Resources.CAMERA.getProjection());
        Resources.Programs.SPRITE_PROGRAM.setUniform("camView", Resources.CAMERA.getView());
        Resources.Programs.SPRITE_PROGRAM.use();

        renderer.setVertexLocation(Resources.Programs.SPRITE_PROGRAM.getAttribute("position"));
        renderer.setTexCoordLocation(Resources.Programs.SPRITE_PROGRAM.getAttribute("texCoords"));
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

        Resources.Programs.FONT_PROGRAM.use();

        String text = "FPS: " + SilenceEngine.gameLoop.getFPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10, 10, Color.WHITE, text);

        text = "UPS: " + SilenceEngine.gameLoop.getUPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10,
                10 + Resources.FONT.getHeight(), Color.WHITE, text);

        text = this.text;
        Resources.FONT.render((GAME_WIDTH - Resources.FONT.getWidth(text)) / 2,
                (GAME_HEIGHT - Resources.FONT.getHeight()) / 2, Color.WHITE, alpha, text);
    }
}
