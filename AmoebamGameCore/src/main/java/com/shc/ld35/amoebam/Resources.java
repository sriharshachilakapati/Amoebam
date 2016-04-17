package com.shc.ld35.amoebam;

import com.shc.ld35.amoebam.font.Font;
import com.shc.silenceengine.audio.openal.ALBuffer;
import com.shc.silenceengine.audio.openal.ALSource;
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
    public static OrthoCam CAMERA;
    public static Font FONT;

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
        public static Texture LOGO;
        public static Texture EXIT;
        public static Texture AMOEBAM_ROLL;
    }

    public static class Sounds
    {
        public static ALBuffer MUSIC;

        public static ALSource EXPLOSION;
        public static ALSource SHOOT;
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
        public static final CollisionTag EXIT    = new CollisionTag();
        public static final CollisionTag WATER   = new CollisionTag();
    }

    public static class Levels
    {
        public static Level LEVEL_1;
        public static Level LEVEL_2;
        public static Level LEVEL_3;
        public static Level LEVEL_4;
        public static Level LEVEL_5;
        public static Level LEVEL_6;
        public static Level LEVEL_7;
        public static Level LEVEL_8;
        public static Level LEVEL_9;
        public static Level LEVEL_10;
    }
}
