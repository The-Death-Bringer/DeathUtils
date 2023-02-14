package me.dthbr.utils.img;

import java.awt.image.BufferedImage;
import java.io.File;

public class Images {

    public static Images of(byte[] data) {
        return new Images(ImgUtils.fromBytes(data));
    }

    public static Images of(BufferedImage image) {
        return new Images(image);
    }

    public static Images of(File file) {
        return new Images(ImgUtils.fromFile(file));
    }

    public static Images of(String url) {
        return new Images(ImgUtils.fromUrl(url));
    }

    private BufferedImage image;

    private Images(BufferedImage image) {
        this.image = image;
    }

    public Images resize(int width, int height) {
        image = ImgUtils.resize(image, width, height);
        return this;
    }

    public Images resize(int size) {
        return resize(size, size);
    }

    public Images cut(int x, int y, int width, int height) {
        image = ImgUtils.cut(image, x, y, width, height);
        return this;
    }

    public Images cut(int width, int height) {
        return cut(0, 0, width, height);
    }

    public Images cut(int size) {
        return cut(size, size);
    }

    public Images center(int width, int height) {
        image = ImgUtils.center(image, width, height);
        return this;
    }

    public Images center(int size) {
        return center(size, size);
    }

    public Images toSepia() {
        image = ImgUtils.toSepia(image);
        return this;
    }

    public Images toGray() {
        image = ImgUtils.toGrayScale(image);
        return this;
    }

    public Images asBg(BufferedImage... images) {
        image = ImgUtils.merge(image, images);
        return this;
    }

    public Images withBg(BufferedImage bg) {
        image = ImgUtils.merge(bg, image);
        return this;
    }

    public BufferedImage build() {
        return image;
    }

    public byte[] toBytes(String format) {
        return ImgUtils.toBytes(image, format);
    }

}
