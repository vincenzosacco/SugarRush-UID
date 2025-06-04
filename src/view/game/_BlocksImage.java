package view.game;

import config.ViewConfig;
import model.game.Constants;
import utils.Resources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class _BlocksImage {
    private static final String BLOCKS_PATH = "/imgs/game/blocks/";
    private static final StringBuilder keyBuilder = new StringBuilder(BLOCKS_PATH);
    private static final List<Constants.Block> staticBlocks = Arrays.asList(
            Constants.Block.WALL,
            Constants.Block.THORNS,
            Constants.Block.SUGAR,
            Constants.Block.SPACE
    );
    private final HashMap<Constants.Block, List<BufferedImage>> blockImages = new HashMap<>();

    private _BlocksImage() {
        // TODO trova un modo per poter usare tutte le estensioni di file immagine. Probabilmente dovrai
        //  cambiare completamente il metodo
        ArrayList<BufferedImage> tmp_imgs = new ArrayList<>();
        String key;

        // Remove the SPACE from iteration since it has no image
        List<Constants.Block> blocks = new ArrayList<>(List.of(Constants.Block.values()));
        blocks.remove(Constants.Block.SPACE);

        for (Constants.Block block : blocks) {

            if (block.equals(Constants.Block.SPACE)) continue;

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
                for (Constants.Direction direction : Constants.Direction.values()){
                    key = parseKey(block, direction);
                    tmp_imgs.add(Resources.getBestImage(key, ViewConfig.TILE_SIZE, ViewConfig.TILE_SIZE));
                }

            // 2- ADD THE IMAGES TO THE MAP //
            blockImages.put(block, new ArrayList<>(tmp_imgs));
            tmp_imgs.clear();
        }
    }

    // SINGLETON //
    private static _BlocksImage instance = null;

    static _BlocksImage getInstance() {
        if (instance == null) {
            instance = new _BlocksImage();
        }
        return instance;
    }

    // UTILS //
    public BufferedImage getStaticBlockImg(Constants.Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }

        if (block == Constants.Block.SPACE) {
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

    BufferedImage getDynamicBlockImg(Constants.Block block, Constants.Direction direction) {
        if (block == null || direction == null) {
            throw new IllegalArgumentException("Block and direction cannot be null");
        }
        if (staticBlocks.contains(block)) {
            throw new IllegalArgumentException("Cannot get dynamic image for static block: " + block +
                    ".\nUse getStaticBlockImg() method instead.");
        }


        assert blockImages.containsKey(block);
        assert blockImages.get(block).size() == Constants.Direction.values().length : "Dynamic block should have images for all directions";
        assert direction.ordinal() < blockImages.get(block).size() : "Direction index out of bounds for block: " + block;
        // Return the image for the specified direction
        return blockImages.get(block).get(direction.ordinal());

    }


    private static String parseKey(Constants.Block block, Constants.Direction direction) {
        // IF BLOCK IS STATIC
        if (staticBlocks.contains(block)) {
            if (!block.equals(Constants.Block.SPACE)) {
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

}
