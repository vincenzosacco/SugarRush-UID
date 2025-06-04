package utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class for resource management
 */
class _ResUtils {


    /**
     * Collects resource paths from classpath
     */
    static void collectResourcePaths(List<String> paths, String[] extensions) {
        try {
            URL dirUrl = Resources.class.getResource("/");
            if (dirUrl == null) {
                System.err.println("Resource directory not found");
                return;
            }

            System.out.println("Resource directory: " + dirUrl);

            if ("jar".equals(dirUrl.getProtocol())) {
                // Running from JAR file
                collectResourcesFromJar(paths, extensions);
            } else {
                // Running from file system
                collectResourcesFromFilesystem(paths, extensions);
            }
        } catch (Exception e) {
            System.err.println("Error collecting resource paths: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Collects resources when running from a JAR
     */
    private static void collectResourcesFromJar(List<String> paths, String[] extensions) throws IOException {
        URL dirUrl = Resources.class.getResource("/");
        String jarPath = dirUrl.getPath();
        int separator = jarPath.indexOf("!");
        String jarFilePath = URLDecoder.decode(jarPath.substring(5, separator), StandardCharsets.UTF_8);

        System.out.println("Scanning JAR file: " + jarFilePath);

        try (JarFile jar = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!entry.isDirectory()) {
                    for (String ext : extensions) {
                        if (name.toLowerCase().endsWith(ext.toLowerCase())) {
                            paths.add("/" + name);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Collects resources when running from filesystem
     */
    private static void collectResourcesFromFilesystem(List<String> paths, String[] extensions) throws IOException {
        // Look in standard resource directories
        String[] resourceDirs = {"/", "/images", "/resources", "/assets"};

        for (String resourceDir : resourceDirs) {
            URL dirUrl = Resources.class.getResource(resourceDir);
            if (dirUrl != null) {
                try {
                    File dir = new File(dirUrl.toURI());
                    if (dir.exists() && dir.isDirectory()) {
                        System.out.println("Scanning directory: " + dir.getAbsolutePath());
                        scanDirectory(dir, resourceDir, paths, extensions);
                    }
                } catch (Exception e) {
                    System.err.println("Error scanning directory " + resourceDir + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Recursively scans a directory for files with specified extensions
     */
    private static void scanDirectory(File dir, String basePath, List<String> paths, String[] extensions) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, basePath + "/" + file.getName(), paths, extensions);
            } else {
                String fileName = file.getName().toLowerCase();
                for (String ext : extensions) {
                    if (fileName.endsWith(ext.toLowerCase())) {
                        String resourcePath = basePath + "/" + file.getName();
                        // Normalize path to avoid double slashes
                        resourcePath = resourcePath.replace("//", "/");
                        paths.add(resourcePath);
                        break;
                    }
                }
            }
        }
    }




}
