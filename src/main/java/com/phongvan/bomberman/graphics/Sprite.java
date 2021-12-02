package com.phongvan.bomberman.graphics;

import com.phongvan.bomberman.Logger;
import com.phongvan.bomberman.gui.PaneController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.IOException;

public class Sprite {
    private int row;
    private int col;
    private int width;
    private int height;
    private int[] pixels;
    private Image image;
    private int scale = -1;

    private static final int TRANSPARENT_COLOR = 0xffff00ff;

    public Sprite(int col, int row, int width, int height) {
        this.row = row;
        this.col = col;
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        image = null;
    }

    public void loadPixels(int[] pixelsSheet) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixels[x + y * width] = pixelsSheet[x + row * height + (y + col * width) * 256];

                if (pixels[x + y * width] == TRANSPARENT_COLOR) {
                    pixels[x + y * width] = 16777215;
                }
            }
        }
    }

    public Image getImage() {
        if (scale != PaneController.getInstance().getScaleSize()) {
            scale = PaneController.getInstance().getScaleSize();
            WritableImage scaleImage = new WritableImage(width * scale, height * scale);
            PixelWriter pw = scaleImage.getPixelWriter();

            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    for (int dx = 0; dx < scale; ++dx) {
                        for (int dy = 0; dy < scale; ++dy) {
                            pw.setArgb(x * scale + dx, y * scale + dy, pixels[x + y * width]);
                        }
                    }
                }
            }

            image = new ImageView(scaleImage).getImage();
        }

        return image;
    }
}
