package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is a simple interface for managing resources.
 */
public class Resources {

    /**
     * Returns an image from the resources with best quality.
     * @param path the path to the image resource, relative to the classpath.
     * @return a BufferedImage object representing the image.
     * @throws IOException if the image cannot be read from the specified path.
     */
    public static BufferedImage getImageBest(String path) throws IOException {
        // LOAD THE IMAGE FROM THE RESOURCES //
        BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(Resources.class.getResource(path)));

        // CREATE AN HIGH QUALITY BUFFERED IMAGE //

        // 1 -  new BufferedImage with the same dimensions
        BufferedImage optimizedImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        // 2 - better quality settings
        Graphics2D g2d = optimizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 3 - draw the original image onto the new buffer
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose(); // release resources

        return optimizedImage;
    }
}
