package view.impl.game;

import config.ViewConfig;
import model.game.GameConstants;
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
    private static final StringBuilder keyBuilder = new StringBuilder(BLOCKS_PATH);
    private static final List<GameConstants.Block> staticBlocks = Arrays.asList(
            GameConstants.Block.WALL,
            GameConstants.Block.THORNS,
            GameConstants.Block.SUGAR,
            GameConstants.Block.SPACE,
            GameConstants.Block.CANDY
    );
    private final HashMap<GameConstants.Block, List<BufferedImage>> blockImages = new HashMap<>();

    private BlocksImages() {
        // TODO trova un modo per poter usare tutte le estensioni di file immagine. Probabilmente dovrai
        //  cambiare completamente il metodo
        ArrayList<BufferedImage> tmp_imgs = new ArrayList<>();
        String key;

        // Remove the SPACE from iteration since it has no image
        List<GameConstants.Block> blocks = new ArrayList<>(List.of(GameConstants.Block.values()));
        blocks.remove(GameConstants.Block.SPACE);

        for (GameConstants.Block block : blocks) {

            if (block.equals(GameConstants.Block.SPACE)) continue;

            // 1- PARSE THE KEY FOR THE BLOCK //

            // NOTE keyBuilder is reset in parseKey() method
            // NOTE img cannot be null due to the way Resources.getBestImage() works

            // IF BLOCK IS STATIC
            if (staticBlocks.contains(block)){
                // file name is in the format: <blockname>.jpg. Example: "wall.jpg"
                key = parseKey(block, null);
                tmp_imgs.add(Resources.getBestImage(key, ViewConfig.TILE_SIZE, ViewConfig.TILE_SIZE));

            }
            // IF BLOCK IS DYNAMIC
            else
                for (GameConstants.Direction direction : GameConstants.Direction.values()){
                    key = parseKey(block, direction);
                    tmp_imgs.add(Resources.getBestImage(key, ViewConfig.TILE_SIZE, ViewConfig.TILE_SIZE));
                }

            // 2- ADD THE IMAGES TO THE MAP //
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
                allImgs[index++] = getDynamicBlockImg(block, GameConstants.Direction.RIGHT);
            }
        }

        return allImgs;
    }
    // UTILS //
    private static String parseKey(GameConstants.Block block, GameConstants.Direction direction) {
        // IF BLOCK IS STATIC
        if (staticBlocks.contains(block)) {
            if (!block.equals(GameConstants.Block.SPACE)) {
                // file name is in the format: <blockname>.jpg. Example: "wall.jpg"
                keyBuilder.append(block.name().toLowerCase()).append(".jpg");
            }
        }
        // IF BLOCK IS DYNAMIC
        else {
            assert direction != null : "Direction cannot be null for dynamic blocks";
            keyBuilder.append(
                    // dir name is in the format: <blockname>
                    block.name().toLowerCase()).append("/")
                    // file name is in the format: <blockname>-<direction>.jpg. Example: "creature-l.jpg"
                    .append(block.name().toLowerCase()).append("-").append(Character.toLowerCase(direction.name()
                    .charAt(0))).append(".jpg");
        }

        // RETURN THE KEY AS A STRING
        String key = keyBuilder.toString();
        // Reset the keyBuilder for the next call
        keyBuilder.setLength(0);
        keyBuilder.append(BLOCKS_PATH); // Reset to the initial path

        return key;
    }

    public boolean isStaticBlock(GameConstants.Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }
        return staticBlocks.contains(block);
    }
}
