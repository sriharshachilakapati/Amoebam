package com.shc.ld35.amoebam.font;

import com.shc.ld35.amoebam.AmoebamGame;
import com.shc.ld35.amoebam.Resources;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Font
{
    private Texture fontTexture;
    private Charset charset;

    public Font(String text, Texture texture)
    {
        charset = new Charset();
        fontTexture = texture;

        String[] lines = splitNoEmptyString(text, "\n");

        String[] parts = splitNoEmptyString(lines[0], " ");
        charset.lineHeight = Integer.parseInt(parts[1].trim().replace("lineHeight=", "").trim());
        charset.base = Integer.parseInt(parts[2].trim().replace("base=", "").trim());
        charset.width = Integer.parseInt(parts[3].trim().replace("scaleW=", "").trim());
        charset.height = Integer.parseInt(parts[4].trim().replace("scaleH=", "").trim());

        charset.chars = new CharDescriptor[256]; // 256 characters, ASCII

        for (int i = 1; i < lines.length; i++)
        {
            String line = lines[i];
            parts = splitNoEmptyString(line, " ");

            CharDescriptor descriptor =
                    charset.chars[Integer.parseInt(parts[1].trim().replace("id=", ""))] = new CharDescriptor();

            descriptor.x = Integer.parseInt(parts[2].trim().replace("x=", ""));
            descriptor.y = Integer.parseInt(parts[3].trim().replace("y=", ""));
            descriptor.width = Integer.parseInt(parts[4].trim().replace("width=", ""));
            descriptor.height = Integer.parseInt(parts[5].trim().replace("height=", ""));
            descriptor.xOffset = Integer.parseInt(parts[6].trim().replace("xoffset=", ""));
            descriptor.yOffset = Integer.parseInt(parts[7].trim().replace("yoffset=", ""));
            descriptor.xAdvance = Integer.parseInt(parts[8].trim().replace("xadvance=", ""));
        }
    }

    private String[] splitNoEmptyString(String string, String separator)
    {
        String[] parts = string.split(separator);
        List<String> list = new ArrayList<>();

        for (String part : parts)
        {
            if (!part.trim().isEmpty())
                list.add(part.trim());
        }

        return list.toArray(new String[list.size()]);
    }

    public void render(int x, int y, Color color, String string)
    {
        render(x, y, color, 1, string);
    }

    public void render(int x, int y, Color color, float alpha, String string)
    {
        Program program = Resources.Programs.FONT_PROGRAM;
        program.use();

        fontTexture.bind(0);

        program.setUniform("color", color);
        Resources.CAMERA.apply();
        program.setUniform("proj", Resources.CAMERA.getProjection());
        program.setUniform("fontTexture", 0);
        program.setUniform("alpha", alpha);

        DynamicRenderer renderer = AmoebamGame.fontRenderer;
        renderer.setVertexLocation(program.getAttribute("position"));
        renderer.setTexCoordLocation(program.getAttribute("texCoords"));

        int triangles = 0;

        renderer.begin(Primitive.TRIANGLES);
        {
            for (char c : string.toCharArray())
            {
                CharDescriptor ch = charset.chars[c];

                int sLeft = ch.x;
                int sTop = ch.y;
                int sRight = ch.x + ch.width;
                int sBot = ch.y + ch.height;

                int dLeft = x + ch.xOffset;
                int dTop = y + ch.yOffset;
                int dRight = dLeft + ch.width;
                int dBot = dTop + ch.height;

                // Draw the character here
                float u1 = (float) sLeft / (float) charset.width;
                float v1 = (float) sTop / (float) charset.height;
                float u2 = (float) sRight / (float) charset.width;
                float v2 = (float) sBot / (float) charset.height;

                if (triangles == 10)
                {
                    triangles = 0;
                    renderer.end();
                    renderer.begin(Primitive.TRIANGLES);
                }

                renderer.vertex(dLeft, dTop);
                renderer.texCoord(u1, v1);

                renderer.vertex(dRight, dTop);
                renderer.texCoord(u2, v1);

                renderer.vertex(dLeft, dBot);
                renderer.texCoord(u1, v2);

                renderer.vertex(dRight, dTop);
                renderer.texCoord(u2, v1);

                renderer.vertex(dRight, dBot);
                renderer.texCoord(u2, v2);

                renderer.vertex(dLeft, dBot);
                renderer.texCoord(u1, v2);

                triangles += 2;

                x += ch.xAdvance;
            }
        }
        renderer.end();
    }

    public int getWidth(String text)
    {
        int width = 0;

        for (char ch : text.toCharArray())
            width += charset.chars[ch].xAdvance;

        return width;
    }

    public int getHeight()
    {
        return charset.lineHeight;
    }
}
