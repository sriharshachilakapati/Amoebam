package com.shc.ld35.amoebam.html;

import com.google.gwt.core.client.EntryPoint;
import com.shc.silenceengine.backend.gwt.GwtRuntime;
import com.shc.ld35.amoebam.AmoebamGame;

public class AmoebamGameLauncher implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        GwtRuntime.start(new AmoebamGame());
    }
}