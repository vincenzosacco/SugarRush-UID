package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils._ResUtils.collectResourcePaths;

/**
 * This class is a simple interface for managing resources.
 */
public class Resources {
    private static final HashMap<String, Image> images = new HashMap<>();


    public static InputStream getResourceAsStream(String relPath) {
        InputStream inputStream = Resources.class.getResourceAsStream(relPath);
        if (inputStream == null) {
            throw new RuntimeException("Resource not found: " + relPath);
        }
        return inputStream;
    }


    public static Image getImage(String relPath) {
//        if (!(width == null) == (height == null)){
//            throw new IllegalArgumentException("Both width and height must be specified or both must be null");
//        }

        Image ret = images.get(relPath);
        if (ret == null) {
            throw new RuntimeException("No such image resource at " + relPath );
        }

//        if (width != null  ) { // && height != null - <height> cannot be null here, because of the check at func beginning
//            // Resize the image if width and height are specified
//            ret = ret.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        }

        return ret;
    }
    public static BufferedImage getBestImage(String relPath, int width, int height) {
        Image orig = getImage(relPath).getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        Image orig = getImage(relPath);

        // Create a new BufferedImage with the same dimensions
        BufferedImage optimizedImage = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
        );


        // Apply better quality settings
        Graphics2D g2d = optimizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw the original image onto the new buffer
        g2d.drawImage(orig, 0, 0, width, height, null);
        g2d.dispose(); // Release resources

        return optimizedImage;
    }

    public static File getFile(String relPath) {
        URL resourceUrl = Objects.requireNonNull(Resources.class.getResource(relPath));
        return new File(resourceUrl.getFile());
    }

    public static void loadAllResources(Runnable onComplete) throws IOException {
        // Read all directories in resource classpath recursively to get all resource paths
        List<String> imgPaths = new ArrayList<>();
        collectResourcePaths(imgPaths, new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp", ".svg"});

        System.out.println("Found " + imgPaths.size() + " resources to load");

        if (imgPaths.isEmpty()) {
            // No resources to load, just run the completion handler
            System.out.println("No resources found to load");
            return;
        }

        // Use a thread pool for parallel loading
        int numThreads = Math.max(1, Math.min(imgPaths.size(), Runtime.getRuntime().availableProcessors()));
        System.out.println("Using " + numThreads + " threads for resource loading");
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(imgPaths.size());

        // Submit tasks for each resource
        for (String path : imgPaths) {
            pool.submit(() -> {
                try {
                    loadImage(path);
                } catch (IOException e) {
                    System.err.println("Failed to load resource: " + path);
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Decrement the latch count
                }
            });
        }


        // Wait for all tasks to complete
        new Thread(() -> {
            try {
                latch.await();
                pool.shutdown();
                System.out.println("Resource loading complete");
                onComplete.run();
            } catch (InterruptedException ignored) {}
        }).start();


    }

    private static void loadImage(String path) throws IOException {
        URL resourceUrl = Objects.requireNonNull(Resources.class.getResource(path));
        BufferedImage img = Objects.requireNonNull(ImageIO.read(resourceUrl));

        images.put(path, img);
        System.out.println("Loaded: " + path);
    }


}