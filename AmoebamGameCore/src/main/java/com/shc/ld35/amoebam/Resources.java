package com.shc.ld35.amoebam;

import com.shc.ld35.amoebam.font.Font;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Frustum;

/**
 * @author Sri Harsha Chilakapati
 */
public class Resources
{
    public static Frustum LEVEL_FRUSTUM = new Frustum();

    public static class Programs
    {
        public static Program SPRITE_PROGRAM;
        public static Program FONT_PROGRAM;

        public static Program create(String vSource, String fSource)
        {
            Shader vertexShader = new Shader(Shader.Type.VERTEX_SHADER);
            vertexShader.source(vSource);
            vertexShader.compile();

            Shader fragmentShader = new Shader(Shader.Type.FRAGMENT_SHADER);
            fragmentShader.source(fSource);
            fragmentShader.compile();

            Program program = new Program();
            program.attach(vertexShader);
            program.attach(fragmentShader);
            program.link();

            vertexShader.dispose();
            fragmentShader.dispose();

            return program;
        }
    }

    public static OrthoCam CAMERA;

    public static Font FONT;

    public static class Textures
    {
        public static Texture BACKGROUND;
        public static Texture CLOUDS;
        public static Texture GROUND;
        public static Texture BULLET;
        public static Texture BLOCKS_SHEET;
        public static Texture AMOEBAM_SHEET;
        public static Texture ENEMY_SHEET;
        public static Texture AMOEBAM_SHOOT_SHEET;
    }

    public static class Sounds
    {
        public static ALBuffer MUSIC;
    }

    public static class Animations
    {
        public static Animation AMOEBAM;
        public static Animation AMOEBAM_SMALL;
        public static Animation WATER;
        public static Animation AMOEBAM_SHOOT_START;
        public static Animation AMOEBAM_SHOOT_STOP;
        public static Animation ENEMY;
    }

    public static class CollisionTags
    {
        public static final CollisionTag GROUND  = new CollisionTag();
        public static final CollisionTag AMOEBAM = new CollisionTag();
        public static final CollisionTag BULLET  = new CollisionTag();
        public static final CollisionTag ENEMY   = new CollisionTag();
    }

    public static class Levels
    {
        public static Level LEVEL_1;
    }
}
