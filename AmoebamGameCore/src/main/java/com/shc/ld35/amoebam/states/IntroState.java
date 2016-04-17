package com.shc.ld35.amoebam.states;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Resources;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.input.Keyboard;

import static com.shc.ld35.amoebam.AmoebamGame.*;
import static com.shc.ld35.amoebam.Utility.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class IntroState extends GameState
{
    @Override
    public void update(float delta)
    {
        if (Keyboard.isAnyKeyDown())
        {
            PlayState.score = 0;

            GameState gOverState3 = new CutsceneState("YOU SCORED " + PlayState.score, 4, () -> this);
            GameState gOverState2 = new CutsceneState("YOU BEAT THE GAME!!!", 4, () -> gOverState3);
            GameState gOverState = new CutsceneState("CONGRATULATIONS!!", 3, () -> gOverState2);
            GameState l10State = new PlayState(Resources.Levels.LEVEL_10, () -> gOverState);
            GameState l9State = new PlayState(Resources.Levels.LEVEL_9, () -> l10State);
            GameState l8State = new PlayState(Resources.Levels.LEVEL_8, () -> l9State);
            GameState l7State = new PlayState(Resources.Levels.LEVEL_7, () -> l8State);
            GameState l6State = new PlayState(Resources.Levels.LEVEL_6, () -> l7State);
            GameState cutScene14 = new CutsceneState("Seeming simple? It won't be", 3, () -> l6State);
            GameState l5State = new PlayState(Resources.Levels.LEVEL_5, () -> cutScene14);
            GameState cutScene13 = new CutsceneState("Go save your friends!!", 3, () -> l5State);
            GameState cutScene12 = new CutsceneState("YOU ARE NOW READY", 3, () -> cutScene13);
            GameState l4State = new PlayState(Resources.Levels.LEVEL_4, () -> cutScene12);
            GameState cutScene11 = new CutsceneState("Hold down SHIFT to roll", 3, () -> l4State);
            GameState cutScene10 = new CutsceneState("Speed is key to success", 1, () -> cutScene11);
            GameState l3State = new PlayState(Resources.Levels.LEVEL_3, () -> cutScene10);
            GameState cutScene9 = new CutsceneState("START NOW!! ACT FAST!! KILL THOSE BASTARDS!", 3, () -> l3State);
            GameState cutScene8 = new CutsceneState("The will eat your friends", 3, () -> cutScene9);
            GameState cutScene7 = new CutsceneState("They are mutated by a virus", 2, () -> cutScene8);
            GameState cutScene6 = new CutsceneState("What are they doing?", 4, () -> cutScene7);
            GameState cutScene5 = new CutsceneState("Amoebians", 3, () -> cutScene6);
            GameState cutScene4 = new CutsceneState("Who are they?", 4, () -> cutScene5);
            GameState l2State = new PlayState(Resources.Levels.LEVEL_2, () -> cutScene4);
            GameState cutScene3 = new CutsceneState("Use the SPACE key to shoot", 3, () -> l2State);
            GameState l1State = new PlayState(Resources.Levels.LEVEL_1, () -> cutScene3);
            GameState cutScene2 = new CutsceneState("Use the ARROW keys to move", 3, () -> l1State);
            GameState cutScene1 = new CutsceneState("TUTORIAL", 4, () -> cutScene2);

            AmoebamGame.instance.setGameState(cutScene1);
        }
    }

    @Override
    public void render(float delta)
    {
        drawTexture(Resources.Textures.BACKGROUND, 0, 0, GAME_WIDTH, GAME_HEIGHT);

        float w = Resources.Textures.LOGO.getWidth();
        float h = Resources.Textures.LOGO.getHeight();
        drawTexture(Resources.Textures.LOGO, (GAME_WIDTH - w) / 2, (GAME_HEIGHT - h) / 2 + 200, w, h);

        Resources.Programs.FONT_PROGRAM.use();

        String text = "FPS: " + SilenceEngine.gameLoop.getFPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10, 10, Color.WHITE, text);

        text = "UPS: " + SilenceEngine.gameLoop.getUPS();
        Resources.FONT.render(GAME_WIDTH - Resources.FONT.getWidth(text) - 10,
                10 + Resources.FONT.getHeight(), Color.WHITE, text);

        text = "Press Any Key to start";
        Resources.FONT.render((GAME_WIDTH - Resources.FONT.getWidth(text)) / 2,
                50, Color.WHITE, text);

        text = "Press ESCAPE to exit";
        Resources.FONT.render((GAME_WIDTH - Resources.FONT.getWidth(text)) / 2,
                60 + Resources.FONT.getHeight(), Color.WHITE, text);

        text = "Programmed by Sri Harsha Chilakapati for LD 35";
        Resources.FONT.render((GAME_WIDTH - Resources.FONT.getWidth(text)) / 2,
                90 + Resources.FONT.getHeight() * 2, Color.WHITE, text);
    }
}
