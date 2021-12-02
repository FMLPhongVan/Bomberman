package com.phongvan.bomberman.graphics;

import com.phongvan.bomberman.Bomberman;
import com.phongvan.bomberman.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class SpriteSheet {
    private String path;
    private int size;
    private int[] pixels;
    private BufferedImage image;

    public SpriteSheet(String path) {
        this.path = path;
        try {
            URL sheetURL = Bomberman.class.getResource("assets/" + path);
            if (sheetURL != null) {
                image = ImageIO.read(sheetURL);
                int width = image.getWidth();
                int height = image.getHeight();
                size = Math.max(width, height);
                pixels = new int[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);
                System.out.println(pixels.length);
            }
        } catch (NullPointerException e) {
            Logger.log(Logger.ERROR, "SpriteSheet", "Cannot find " + path);
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "SpriteSheet", e.getMessage());
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getSize() {
        return size;
    }
}
