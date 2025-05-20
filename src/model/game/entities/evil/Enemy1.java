package model.game.entities.evil;

import model.game.Constants;
import model.game.utils.Cell;

/**
 * The Enemy1 has the following behavior:
 * <p>
 * on each game state update:
 *     - move one cell forward
 * </p>
 */
public class Enemy1 extends Enemy {

    @Override
    public Constants.Block blockType() {
        return Constants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {
        super(row,col);
    }
    public Enemy1(Cell coord) {
        super(coord);
    }


    @Override
    public void performAction() {
//        this.coord.incrCol();
    }


}
