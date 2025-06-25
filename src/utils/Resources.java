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


    /**
     * Loads a resource as stream.
     * @param relPath the relative path to the resource, e.g. "/imgs/icons/coin.png"
     * @return InputStream of the resource
     * @apiNote remember to close the InputStream after use to avoid memory leaks
     */
    public static InputStream getResourceAsStream(String relPath) {
        InputStream inputStream = Resources.class.getResourceAsStream(relPath);
        if (inputStream == null) {
            throw new RuntimeException("Resource not found: " + relPath);
        }
        return inputStream;
        // InputStream will be closed by the caller
    }

    /**
     * Gets an image resource by its relative path.
     * @param relPath the relative path to the image resource, e.g. "/imgs/icons/coin.png"
     * @return Image object
     * @throws RuntimeException if the image resource is not found
     */
    public static Image getImage(String relPath) {
        Image ret = images.get(relPath);
        if (ret == null) {
            throw new RuntimeException("No such image resource at " + relPath );
        }
        return ret;
    }

    /**
     * Gets an image resource by its relative path and returns it as a BufferedImage.
     * The BufferedImage is created with the highest quality settings for rendering.
     * @param relPath the relative path to the image resource, e.g. "/imgs/icons/coin.png"
     * @param width the desired width of the image
     * @param height the desired height of the image
     * @return BufferedImage object with the specified dimensions
     */
    public static BufferedImage getBestImage(String relPath, int width, int height) {
        // TODO- non sono sicuro che sia davvero la qualità migliore, vedere dove sta il problema
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

    /**
     * Gets a file resource by its relative path.
     * This method is useful for accessing files in the classpath.
     * @param relPath the relative path to the file resource, e.g. "/configs/settings.json"
     * @return File object representing the resource
     */
    public static File getFile(String relPath) {
        URL resourceUrl = Objects.requireNonNull(Resources.class.getResource(relPath));
        return new File(resourceUrl.getFile());
    }


    private static boolean isLoaded = false;
    /**
     * Loads all resources from the classpath asynchronously.

     * @param onComplete a Runnable that will be executed if all resources are loaded without exceptions.
     * @throws IOException
     */
    public static void loadAllResources(Runnable onComplete) throws IOException {
        if (! isLoaded) {
            isLoaded = true; // Prevent multiple loads
        } else
            throw new IllegalStateException("Resources already loaded");

    // -------------------- SCAN RESOURCES CLASS PATH ----------------------------------------------------------------//

        // Read all directories in resource classpath recursively to get all resource paths
        List<String> imgPaths = new ArrayList<>();
        collectResourcePaths(imgPaths, new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp", ".svg"});

        System.out.println("Found " + imgPaths.size() + " resources to load");

        if (imgPaths.isEmpty()) {
            // No resources to load, just run the completion handler
            System.out.println("No resources found to load");
            return;
        }

    // -------------------- LOAD RESOURCES MULTITHREADING ------------------------------------------------------------//

        // Use a thread pool for parallel loading
        int numThreads = Math.max(1, Math.min(imgPaths.size(), Runtime.getRuntime().availableProcessors()));
        System.out.println("Using " + numThreads + " threads for resource loading");
        // TODO - vedere se è possibile velocizzare ulteriormente il caricamento delle immagini
        //  magari usando ExecutorService diverso.
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
            } catch (InterruptedException ignored) {
                System.err.println("Resource loading interrupted");
            }
        }).start();


    }

    private static void loadImage(String path) throws IOException {
        URL resourceUrl = Objects.requireNonNull(Resources.class.getResource(path));
        BufferedImage img = Objects.requireNonNull(ImageIO.read(resourceUrl));

        images.put(path, img);
        System.out.println("Loaded: " + path);
    }


}