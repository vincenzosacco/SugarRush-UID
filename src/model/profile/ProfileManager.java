package model.profile;

import java.io.*;

public class ProfileManager {
    private static final String SAVES_PATH = "resources/profiles";
    static {
        try {
            // Create profiles directory on startup
            File savesDir = new File(SAVES_PATH);
            if (!savesDir.exists()) {
                if (!savesDir.mkdirs()) {
                    throw new IOException("Failed to create saves directory: " + SAVES_PATH);
                }
            }

            // Create default profile if no profiles exist
            File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".dat"));
            if (files == null || files.length == 0) {
                Profile defaultProfile = createProfile();
                saveProfile(defaultProfile);
                System.out.println("Created default profile in: " + savesDir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error initializing ProfileManager: " + e.getMessage());
        }
    }
    private static final Profile lastProfile = loadLastProfile(); // Load the last profile on startup

    private static Profile createProfile() {
        return new Profile();
    }

    public static void saveProfile(Profile profile) {
        try {
            // Use simple File for saving
            File profileFile = new File(SAVES_PATH, profile.getName() + ".dat");
            profileFile.getParentFile().mkdirs();

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(profileFile))) {
                oos.writeObject(profile);
                System.out.println("Profile saved to: " + profileFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error saving profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Profile loadProfile(String profileName) {
        // Use same path construction as saving
        File file = new File(SAVES_PATH, profileName + ".dat");

        if (!file.exists() || file.length() == 0) {
            return createProfile();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (Profile) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading profile '" + profileName + "': " + e.getMessage());
            return createProfile();
        }
    }

    public static Profile[] getProfiles() {
        File savesDir = new File(SAVES_PATH);
        if (!savesDir.exists() || !savesDir.isDirectory()) {
            return new Profile[]{createProfile()};
        }

        File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files == null || files.length == 0) {
            return new Profile[]{createProfile()};
        }

        Profile[] profiles = new Profile[files.length];
        for (int i = 0; i < files.length; i++) {
            profiles[i] = loadProfile(files[i].getName().replace(".dat", ""));
        }
        return profiles;
    }

    /**
     * Returns the last profile based on the last modified time of the profile files.
     * If no profiles exist, it creates a new default profile.
     *
     * @return the last profile or a new default profile if none exist
     */
    public static Profile getLastProfile() {
        assert lastProfile != null;
        return lastProfile;
    }

    // PRIVATE UTILS //
    /**
     * Loads the last profile based on the last modified time of the profile files.
     * If no profiles exist, it creates a new default profile.
     *
     * @return the last profile or a new default profile if none exist
     */
    private static Profile loadLastProfile() {
        // Use the same path construction as saving
        File savesDir = new File(SAVES_PATH);
        if (!savesDir.exists() || !savesDir.isDirectory()) {
            return createProfile();
        }

        File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files == null || files.length == 0) {
            System.err.println("No last profile found in: " + savesDir.getAbsolutePath() +
                               ". Creating a new default profile.");
            return createProfile();
        }

        // Load the last profile based on the last modified time
        File lastFile = files[0];
        for (File file : files) {
            if (file.lastModified() > lastFile.lastModified()) {
                lastFile = file;
            }
        }

        return loadProfile(lastFile.getName().replace(".dat", ""));
    }
}