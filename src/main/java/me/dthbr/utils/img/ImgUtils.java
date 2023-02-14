package me.dthbr.utils.img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class ImgUtils {

    // Utility class
    private ImgUtils() {
    }

    // Basic manipulation
    public static BufferedImage merge(BufferedImage base, BufferedImage... overlays) {
        if (base == null)
            return null;
        Graphics2D g = base.createGraphics();
        for (BufferedImage overlay : overlays)
            g.drawImage(overlay, 0, 0, null);
        g.dispose();
        return base;
    }

    public static BufferedImage center(BufferedImage image, int size) {
        return center(image, size, size);
    }

    public static BufferedImage center(BufferedImage image, int width, int height) {
        BufferedImage centered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = centered.createGraphics();
        g.drawImage(image, (width - image.getWidth()) / 2, (height - image.getHeight()) / 2, null);
        g.dispose();
        return centered;
    }

    // Filters
    public static BufferedImage toGrayScale(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y), true);
                int a = color.getAlpha();
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                if (a == 0)
                    continue;

                int avg = (r + g + b) / 3;
                gray.setRGB(x, y, new Color(avg, avg, avg, a).getRGB());
            }
        }

        return gray;
    }

    public static BufferedImage toSepia(BufferedImage image) {

        BufferedImage sepia = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y), true);
                int a = color.getAlpha();
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                if (a == 0)
                    continue;

                int sr = (int) (r * 0.393 + g * 0.769 + b * 0.189);
                int sg = (int) (r * 0.349 + g * 0.686 + b * 0.168);
                int sb = (int) (r * 0.272 + g * 0.534 + b * 0.131);
                sepia.setRGB(x, y, new Color(Math.min(sr, 255), Math.min(sg, 255), Math.min(sb, 255), a).getRGB());
            }
        }

        return sepia;
    }

    // Size manipulation
    public static BufferedImage resize(BufferedImage img, int size) {
        return resize(img, size, size);
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, img.getType());
        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    public static BufferedImage cut(BufferedImage img, int size) {
        return cut(img, size, size);
    }

    public static BufferedImage cut(BufferedImage img, int width, int height) {
        return img.getSubimage(0, 0, width, height);
    }

    public static BufferedImage cut(BufferedImage img, int x, int y, int width, int height) {
        return img.getSubimage(x, y, width, height);
    }

    // Read
    public static BufferedImage fromResource(String path) {

        if (!path.startsWith("/"))
            path = "/" + path;

        try (InputStream is = ImgUtils.class.getResourceAsStream(path)) {

            if (is == null) {
                System.out.println("Could not find resource: " + path);
                return null;
            }

            BufferedImage read = ImageIO.read(is);
            is.close();
            return read;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage fromUrl(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage fromFile(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage fromBytes(byte[] bytes) {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Serialization
    public static byte[] toBytes(BufferedImage image, String format) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
