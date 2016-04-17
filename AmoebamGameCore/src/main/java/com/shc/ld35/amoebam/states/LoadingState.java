package com.shc.ld35.amoebam.states;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Level;
import com.shc.ld35.amoebam.Resources;
import com.shc.ld35.amoebam.font.Font;
import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.io.ImageReader;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

import static com.shc.ld35.amoebam.AmoebamGame.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class LoadingState extends GameState
{
    private DynamicRenderer renderer;
    private Program         program;

    private int numLoaded    = 0;
    private int numResources = 15; // TODO: Increment this whenever we add a new resource

    private boolean isReady = false;

    @Override
    public void onEnter()
    {
        FileReader fileReader = SilenceEngine.io.getFileReader();

        fileReader.readTextFile(FilePath.getResourceFile("shaders/loading.vert"), vSource ->
                fileReader.readTextFile(FilePath.getResourceFile("shaders/loading.frag"), fSource ->
                        makeProgram(vSource, fSource)));
    }

    @Override
    public void render(float delta)
    {
        if (!isReady)
            return;

        float percentage = ((float) numLoaded / (float) numResources) * 100f;
        percentage = MathUtils.convertRange(percentage, 0, 100, 40, GAME_WIDTH - 40);

        renderer.begin(Primitive.TRIANGLE_FAN);
        {
            renderer.vertex(0, 0);
            renderer.color(Color.MAROON);
            renderer.vertex(GAME_WIDTH, 0);
            renderer.color(Color.MAROON);
            renderer.vertex(GAME_WIDTH, GAME_HEIGHT);
            renderer.color(Color.DARK_RED);
            renderer.vertex(0, GAME_HEIGHT);
            renderer.color(Color.DARK_RED);
        }
        renderer.end();

        renderer.begin(Primitive.TRIANGLE_FAN);
        {
            renderer.vertex(40, GAME_HEIGHT - 60);
            renderer.color(Color.GREEN);

            renderer.vertex(percentage, GAME_HEIGHT - 60);
            renderer.color(Color.GREEN);

            renderer.vertex(percentage, GAME_HEIGHT - 40);
            renderer.color(Color.GREEN);

            renderer.vertex(40, GAME_HEIGHT - 40);
            renderer.color(Color.GREEN);
        }
        renderer.end();

        if (percentage == GAME_WIDTH - 40)
        {
            GameState cutScene6 = new CutsceneState("SAVE THEM!!", 4, PlayState::new);
            GameState cutScene5 = new CutsceneState("Your friends are being eaten!", 3, () -> cutScene6);
            GameState cutScene4 = new CutsceneState("YOU ARE AMOEBAM", 2, () -> cutScene5);
            GameState cutScene3 = new CutsceneState("Use the SPACE key to shoot", 3, () -> cutScene4);
            GameState cutScene2 = new CutsceneState("Use the ARROW keys to move", 3, () -> cutScene3);
            GameState cutScene1 = new CutsceneState("A SMALL TUTORIAL", 4, () -> cutScene2);

            AmoebamGame.instance.setGameState(cutScene1);
        }
    }

    @Override
    public void onLeave()
    {
        renderer.dispose();
        program.dispose();
    }

    private void makeProgram(String vertexSource, String fragmentSource)
    {
        program = Resources.Programs.create(vertexSource, fragmentSource);

        renderer = new DynamicRenderer();
        renderer.setMaxBatchSize(100);
        renderer.setBatchSize(100);

        renderer.setVertexLocation(program.getAttribute("position"));
        renderer.setColorLocation(program.getAttribute("color"));

        Resources.CAMERA = new OrthoCam(GAME_WIDTH, GAME_HEIGHT);
        Resources.CAMERA.apply();

        program.setUniform("cameraProj", Resources.CAMERA.getProjection());
        program.setUniform("cameraView", Resources.CAMERA.getView());
        program.use();

        isReady = true;

        loadResources();
    }

    private void loadResources()
    {
        ImageReader imageReader = SilenceEngine.io.getImageReader();
        FileReader fileReader = SilenceEngine.io.getFileReader();

        imageReader.readImage(FilePath.getResourceFile("textures/amoebam_sheet.png"), image -> {
            Resources.Textures.AMOEBAM_SHEET = Texture.fromImage(image);
            image.free();

            SpriteSheet sheet = new SpriteSheet(Resources.Textures.AMOEBAM_SHEET, 64, 64);

            Resources.Animations.AMOEBAM = new Animation();
            Resources.Animations.AMOEBAM.addFrame(sheet.getCell(0, 0), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM.addFrame(sheet.getCell(0, 1), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM.addFrame(sheet.getCell(0, 2), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM.addFrame(sheet.getCell(0, 1), 50, TimeUtils.Unit.MILLIS);

            Resources.Animations.AMOEBAM_SMALL = new Animation();
            Resources.Animations.AMOEBAM_SMALL.addFrame(sheet.getCell(0, 3), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SMALL.addFrame(sheet.getCell(0, 4), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SMALL.addFrame(sheet.getCell(0, 5), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SMALL.addFrame(sheet.getCell(0, 4), 50, TimeUtils.Unit.MILLIS);

            numLoaded++;
        });

        imageReader.readImage(FilePath.getResourceFile("textures/blocks.png"), image -> {
            Resources.Textures.BLOCKS_SHEET = Texture.fromImage(image);
            image.free();

            SpriteSheet sheet = new SpriteSheet(Resources.Textures.BLOCKS_SHEET, 64, 66);

            Resources.Textures.GROUND = sheet.getCell(0, 0);

            Resources.Animations.WATER = new Animation();
            Resources.Animations.WATER.addFrame(sheet.getCell(0, 1), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.WATER.addFrame(sheet.getCell(0, 2), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.WATER.addFrame(sheet.getCell(0, 3), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.WATER.addFrame(sheet.getCell(0, 2), 75, TimeUtils.Unit.MILLIS);

            numLoaded++;
        });

        imageReader.readImage(FilePath.getResourceFile("textures/enemy_sheet.png"), image -> {
            Resources.Textures.ENEMY_SHEET = Texture.fromImage(image);
            image.free();

            SpriteSheet sheet = new SpriteSheet(Resources.Textures.ENEMY_SHEET, 64, 96);

            Resources.Animations.ENEMY = new Animation();
            Resources.Animations.ENEMY.addFrame(sheet.getCell(0, 0), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.ENEMY.addFrame(sheet.getCell(0, 1), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.ENEMY.addFrame(sheet.getCell(0, 2), 75, TimeUtils.Unit.MILLIS);
            Resources.Animations.ENEMY.addFrame(sheet.getCell(0, 1), 50, TimeUtils.Unit.MILLIS);

            numLoaded++;
        });

        imageReader.readImage(FilePath.getResourceFile("textures/background.png"), image -> {
            Resources.Textures.BACKGROUND = Texture.fromImage(image);
            image.free();
            numLoaded++;
        });

        imageReader.readImage(FilePath.getResourceFile("textures/bullet.png"), image -> {
            Resources.Textures.BULLET = Texture.fromImage(image);
            image.free();
            numLoaded++;
        });

        imageReader.readImage(FilePath.getResourceFile("textures/clouds.png"), image -> {
            Resources.Textures.CLOUDS = Texture.fromImage(image);
            image.free();
            numLoaded++;
        });

        fileReader.readTextFile(FilePath.getResourceFile("shaders/sprite.vert"), vSource -> {
            fileReader.readTextFile(FilePath.getResourceFile("shaders/sprite.frag"), fSource -> {
                Resources.Programs.SPRITE_PROGRAM = Resources.Programs.create(vSource, fSource);
                numLoaded++;
            });
            numLoaded++;
        });

        fileReader.readTextFile(FilePath.getResourceFile("shaders/font.vert"), vSource -> {
            fileReader.readTextFile(FilePath.getResourceFile("shaders/font.frag"), fSource -> {
                Resources.Programs.FONT_PROGRAM = Resources.Programs.create(vSource, fSource);

                fileReader.readTextFile(FilePath.getResourceFile("fonts/arialround.fnt"), font -> {
                    imageReader.readImage(FilePath.getResourceFile("fonts/arialround_0.png"), image -> {
                        Texture fontTexture = Texture.fromImage(image);
                        image.free();
                        Resources.FONT = new Font(font, fontTexture);
                        numLoaded++;
                    });
                    numLoaded++;
                });
                numLoaded++;
            });
            numLoaded++;

        });

        imageReader.readImage(FilePath.getResourceFile("textures/amoebam_shooting_sheet.png"), image -> {
            Resources.Textures.AMOEBAM_SHOOT_SHEET = Texture.fromImage(image);
            image.free();

            SpriteSheet sheet = new SpriteSheet(Resources.Textures.AMOEBAM_SHOOT_SHEET, 131, 70);

            Resources.Animations.AMOEBAM_SHOOT_START = new Animation();
            Resources.Animations.AMOEBAM_SHOOT_START.addFrame(sheet.getCell(0, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_START.addFrame(sheet.getCell(1, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_START.addFrame(sheet.getCell(2, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_START.addFrame(sheet.getCell(3, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_START.addFrame(sheet.getCell(4, 0), 50, TimeUtils.Unit.MILLIS);

            Resources.Animations.AMOEBAM_SHOOT_STOP = new Animation();
            Resources.Animations.AMOEBAM_SHOOT_STOP.addFrame(sheet.getCell(4, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_STOP.addFrame(sheet.getCell(3, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_STOP.addFrame(sheet.getCell(2, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_STOP.addFrame(sheet.getCell(1, 0), 50, TimeUtils.Unit.MILLIS);
            Resources.Animations.AMOEBAM_SHOOT_STOP.addFrame(sheet.getCell(0, 0), 50, TimeUtils.Unit.MILLIS);

            numLoaded++;
        });

        fileReader.readBinaryFile(FilePath.getResourceFile("sounds/music.ogg"), file -> {
            SilenceEngine.audio.readToALBuffer(AudioDevice.AudioFormat.OGG, file, alBuffer ->
                    Resources.Sounds.MUSIC = alBuffer);
            numLoaded++;
        });

        fileReader.readTextFile(FilePath.getResourceFile("levels/level1.lvl"), data -> {
            Resources.Levels.LEVEL_1 = new Level(data);
            numLoaded++;
        });
    }
}
