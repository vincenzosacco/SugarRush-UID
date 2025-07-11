package view.impl.game;

import config.ViewConfig;
import model.game.GameConstants;
import model.profile.ProfileManager;
import utils.Resources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton class that manages the images for game blocks.
 * It loads static and dynamic block images from the resources.
 */
public class BlocksImages {
    private static final String BLOCKS_PATH = "/imgs/game/blocks/";
    private static final String[] SUPPORTED_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".bmp"};
    private static final List<GameConstants.Block> staticBlocks = Arrays.asList(
            GameConstants.Block.WALL,
            GameConstants.Block.THORNS,
            GameConstants.Block.SUGAR,
            GameConstants.Block.SPACE,
            GameConstants.Block.CANDY
    );
    private final HashMap<GameConstants.Block, List<BufferedImage>> blockImages = new HashMap<>();

    // Load existing image using one of the supported extensions
    private BlocksImages() {
        ArrayList<BufferedImage> tmp_imgs = new ArrayList<>();

        // Remove the SPACE from iteration since it has no image
        List<GameConstants.Block> blocks = new ArrayList<>(List.of(GameConstants.Block.values()));
        blocks.remove(GameConstants.Block.SPACE);

        for (GameConstants.Block block : blocks) {
            if (block.equals(GameConstants.Block.SPACE)) continue;

            if (staticBlocks.contains(block)) {
                String key = findExistingImagePath(block, null);
                tmp_imgs.add(Resources.getBestImage(key, ViewConfig.TILE_SIZE, ViewConfig.TILE_SIZE));
            }else if (block == GameConstants.Block.CREATURE) {
                for (GameConstants.Direction direction : GameConstants.Direction.values()) {
                    tmp_imgs.add(getPlayerCharacterImg(direction));
                }
            }
            else {
                for (GameConstants.Direction direction : GameConstants.Direction.values()) {
                    String key = findExistingImagePath(block, direction);
                    tmp_imgs.add(Resources.getBestImage(key, ViewConfig.TILE_SIZE, ViewConfig.TILE_SIZE));
                }
            }

            blockImages.put(block, new ArrayList<>(tmp_imgs));
            tmp_imgs.clear();
        }
    }

    // SINGLETON //
    private static BlocksImages instance = null;

    public static BlocksImages getInstance() {
        if (instance == null) {
            instance = new BlocksImages();
        }
        return instance;
    }

    // PACKAGE METHODS //
    BufferedImage getStaticBlockImg(GameConstants.Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }

        if (block == GameConstants.Block.SPACE) {
            // Return null for SPACE block
            return null;
        }

        if (!staticBlocks.contains(block)) {
            throw new IllegalArgumentException("Cannot get static image for dynamic block: " + block +
                    ".\nUse getDynamicBlockImg() method instead.");
        }

        assert blockImages.containsKey(block);
        assert blockImages.get(block).size() == 1 : "Static block should have exactly one image";

        return blockImages.get(block).getFirst();
    }

    BufferedImage getDynamicBlockImg(GameConstants.Block block, GameConstants.Direction direction) {
        if (block == null || direction == null) {
            throw new IllegalArgumentException("Block and direction cannot be null");
        }
        if (staticBlocks.contains(block)) {
            throw new IllegalArgumentException("Cannot get dynamic image for static block: " + block +
                    ".\nUse getStaticBlockImg() method instead.");
        }


        assert blockImages.containsKey(block);
        assert blockImages.get(block).size() == GameConstants.Direction.values().length : "Dynamic block should have images for all directions";
        assert direction.ordinal() < blockImages.get(block).size() : "Direction index out of bounds for block: " + block;
        // Return the image for the specified direction
        return blockImages.get(block).get(direction.ordinal());

    }

    private static String buildBaseKey(GameConstants.Block block, GameConstants.Direction direction) {
        if (staticBlocks.contains(block)) {
            if (!block.equals(GameConstants.Block.SPACE)) {
                // ex: "/imgs/game/blocks/wall"
                return BLOCKS_PATH + block.name().toLowerCase();
            }
        } else {
            assert direction != null;
            // ex: "/imgs/game/blocks/creature/creature-l"
            return BLOCKS_PATH + block.name().toLowerCase() + "/" + block.name().toLowerCase() + "-" + Character.toLowerCase(direction.name().charAt(0));
        }
        return null;
    }

    private static String findExistingImagePath(GameConstants.Block block, GameConstants.Direction direction) {
        String baseKey = buildBaseKey(block, direction);
        if (baseKey == null) {
            throw new IllegalArgumentException("Invalid block or direction for image key");
        }
        for (String ext : SUPPORTED_EXTENSIONS) {
            String candidate = baseKey + ext;
            if (Resources.class.getResource(candidate) != null) {
                return candidate;
            }
        }
        throw new RuntimeException("Image not found for block " + block +
                (direction != null ? " direction " + direction : "") +
                ". Tried paths: " + Arrays.toString(Arrays.stream(SUPPORTED_EXTENSIONS)
                .map(ext -> baseKey + ext).toArray()));
    }

    // PUBLIC METHODS //
    /**
     * Returns all blocks image in a single array. For dynamic blocks (which have multiple images for each direction),
     * returns the {@code Direction.RIGHT} image.
     * @return An array of BufferedImage containing all default images for blocks.
     * @see GameConstants.Block
     * @see GameConstants.Direction
     * @see BlocksImages#getStaticBlockImg
     * @see BlocksImages#getDynamicBlockImg
     */
    public BufferedImage[] getAllDefaultImgs(){
        BufferedImage[] allImgs = new BufferedImage[GameConstants.Block.values().length - 1]; // -1 for SPACE block
        int index = 0;

        for (GameConstants.Block block : GameConstants.Block.values()) {
            if (block == GameConstants.Block.SPACE) continue; // Skip SPACE block

            if (staticBlocks.contains(block)) {
                allImgs[index++] = getStaticBlockImg(block);
            } else {
                if (block == GameConstants.Block.CREATURE) {
                    allImgs[index++] = getPlayerCharacterImg(GameConstants.Direction.NONE);
                } else {
                    allImgs[index++] = getDynamicBlockImg(block, GameConstants.Direction.NONE);
                }
            }
        }

        return allImgs;
    }

    // Get the current character image
    public BufferedImage getPlayerCharacterImg(GameConstants.Direction direction) {
        int characterIndex = ProfileManager.getLastProfile().getCurrentCharacterIndex();
        String folderName = "creature" + characterIndex;

        String basePath = "/imgs/game/blocks/creature/" + folderName + "/" + folderName + "-" + Character.toLowerCase(direction.name().charAt(0));

        for (String ext : SUPPORTED_EXTENSIONS) {
            String fullPath = basePath + ext;
            if (Resources.class.getResource(fullPath) != null) {
                return Resources.getBestImage(fullPath, ViewConfig.TILE_SIZE, ViewConfig.TILE_SIZE);
            }
        }
        throw new RuntimeException("Image not found for character " + characterIndex + " direction " + direction);
    }


    public boolean isStaticBlock(GameConstants.Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }
        return staticBlocks.contains(block);
    }
}
