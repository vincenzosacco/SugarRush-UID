package model.game.entities;

import model.Model;
import model.game.Constants;
import model.game.Game;
import model.game.utils.Cell;
import model.game.Constants.Direction;

import java.util.List;


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

    // Do nothing beacause the creature is moved by the user
    @Override
    public void performAction() {

    }


    public void moveCreature(Direction direction) {
         int newRow = this.coord.getRow();
         int newCol = this.coord.getCol();

         switch (direction) {
             case Direction.UP -> --newRow; // going up means decrementing the row index by 1
             case Direction.DOWN -> ++newRow; // going down means incrementing the row index by 1
             case Direction.LEFT -> --newCol; // going left means decrementing the col index by 1
             case Direction.RIGHT -> ++newCol; // going right means incrementing the col index by 1
             default -> throw new IllegalArgumentException("Invalid direction: " + direction);
         }

        List<List<Constants.Block>> gameMat = Model.getInstance().getGame().getState();
        if (gameMat.get(newRow).get(newCol) != Constants.Block.WALL){
            this.coord = new Cell(newRow,newCol);
        }
     }


//        // CHECK IF MOVEMENT IS LEGAL //
//        // if it's in the matrix range:
//        if (newRow >= 0 && newRow < this.size() && newCol >= 0 && newCol < this.get(newRow).size()){
//            // if it's a THORNS:
//            if(this.get(newRow).get(newCol) == Block.THORNS){
//                //you die
//                //temporarily if you touch it the application closes
//                System.exit(0);
//            }
//
//            // if it's a SPACE or SUGAR :
//            if (this.get(newRow).get(newCol) != Block.WALL){
//                // here move is legal
//                // MOVE CREATURE //
//                this.get(newRow).set(newCol, Block.CREATURE); // set the new creature coordinates
//
//                return this.get(newRow).get(newCol) ; // return the new block type
//            }
//        }
//        return null;




    public void addSugar(){
        sugarCount++;
    }

    public int getSugarCount(){
        return sugarCount;
    }


}
