package com.shc.ld35.amoebam;

import com.shc.silenceengine.core.GameState;

/**
 * @author Sri Harsha Chilakapati
 */
@FunctionalInterface
public interface StateProvider
{
    GameState provide();
}
