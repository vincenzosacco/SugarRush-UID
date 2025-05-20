package model.game.entities;

import model.game.Constants;
import model.game.utils.Cell;

/**
 * Model for Creature entity
 */
public class Creature extends Entity{
    private int sugarCount = 0;

    @Override
    public Constants.Block blockType() {
        return Constants.Block.CREATURE;
    }

    public Creature(int row, int col){
        super(row,col);
    }

    public Creature(Cell coord) {
        super(coord);
    }

    @Override
    public void performAction() {

    }


    public void addSugar(){
        sugarCount++;
    }

    public int getSugarCount(){
        return sugarCount;
    }


}
