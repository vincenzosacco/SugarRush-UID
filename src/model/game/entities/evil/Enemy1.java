package model.game.entities.evil;

import model.Model;
import model.game.Constants;
import model.game.Game;
import model.game.utils.Cell;
import model.game.Constants.Direction;

import java.util.List;

/**
 * The Enemy1 has the following behavior:
 * <p>
 * on each game state update:
 *     - move one cell forward
 * </p>
 */
public class Enemy1 extends Enemy {
    private static Direction DIRECTION = Direction.LEFT; // remove this line when the enemy is implemented

    @Override
    public Constants.Block blockType() {
        return Constants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {

        super(row,col);
        //setMoveDelay(20);
    }
    public Enemy1(Cell coord) {
        super(coord);
    }

    @Override
    public Cell computeAction() {
        Game game = Model.getInstance().getGame();
        List<List<Constants.Block>> state = game.getState(); // current game map

        int attempts = 0;
        Cell target;

        do {
            // Change direction after a few attempts
            if (attempts == 0){
                DIRECTION = Direction.values()[(int) (Math.random() * Direction.values().length)];
            }

            int newRow = coord.getRow();
            int newCol = coord.getCol();

            switch (DIRECTION) {
                case UP -> newRow--;
                case DOWN -> newRow++;
                case LEFT -> newCol--;
                case RIGHT -> newCol++;
            }

            target = new Cell(newRow, newCol);

            // Check if it's off the map
            boolean outOfBounds = newRow < 0 || newCol < 0 ||
                    newRow >= state.size() ||
                    newCol >= state.get(0).size();

            // if BLOCK is not SPACE or CREATURE or is out of map -> change direction
            if (!outOfBounds && (state.get(newRow).get(newCol) == Constants.Block.SPACE || state.get(newRow).get(newCol) == Constants.Block.CREATURE)){
                return target; // valid direction found
            }

            attempts++;
        } while (attempts < 10); // avoid infinite loop

        // He hasn't found a valid direction: he remains still
        DIRECTION = Direction.NONE;
        return getCoord();
    }

    @Override
    public boolean manageCollision(Constants.Block block) {
        if (DIRECTION == Direction.NONE) return false;

        return block == Constants.Block.SPACE;
    }

    @Override
    public void performAction(Cell newCoord) {
        this.coord = newCoord;
    }






}
