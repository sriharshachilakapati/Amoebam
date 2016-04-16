package com.shc.ld35.amoebam;

import com.shc.ld35.amoebam.states.LoadingState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.input.Keyboard;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

public class AmoebamGame extends Game
{
    public static final int GAME_WIDTH  = 1280;
    public static final int GAME_HEIGHT = 720;

    public static AmoebamGame     instance;
    public static DynamicRenderer spriteRenderer;
    public static DynamicRenderer fontRenderer;
    public static SpriteBatch     spriteBatch;

    @Override
    public void init()
    {
        instance = this;

        SilenceEngine.display.setTitle("AmoebamGame: SilenceEngine v1.0.1");
        SilenceEngine.display.setSize(GAME_WIDTH, GAME_HEIGHT);
        SilenceEngine.display.centerOnScreen();

        GLContext.enable(GL_BLEND);
        GLContext.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        spriteRenderer = new DynamicRenderer();
        spriteRenderer.setBatchSize(50 * 3 + 5);
        spriteRenderer.setMaxBatchSize(50 * 3 + 5);

        fontRenderer = new DynamicRenderer();
        fontRenderer.setBatchSize(30);
        fontRenderer.setMaxBatchSize(30);

        spriteBatch = new SpriteBatch();

        setGameState(new LoadingState());
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();
    }

    @Override
    public void resized()
    {
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }
}