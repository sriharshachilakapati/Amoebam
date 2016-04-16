package com.shc.ld35.amoebam.desktop;

import com.shc.silenceengine.backend.lwjgl.LwjglRuntime;
import com.shc.ld35.amoebam.AmoebamGame;

public class AmoebamGameLauncher
{
    public static void main(String[] args)
    {
        LwjglRuntime.start(new AmoebamGame());
    }
}