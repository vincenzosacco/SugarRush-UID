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
    private Direction direction = Direction.NONE;

    public Creature() {
        super();
    }

    public Creature(int row, int col){
        super(row,col);
    }

    public Creature(Cell coord) {
        super(coord);
    }

    @Override
    public Constants.Block blockType() {
        return Constants.Block.CREATURE;
    }

    // Do nothing beacause the creature is moved by the user
    @Override
    public void performAction() {
        // MOVE BY ONE BLOCK IN DIRECTION
        moveCreature(direction);

    }


    private void moveCreature(Direction direction) {
         int newRow = this.coord.getRow();
         int newCol = this.coord.getCol();

         switch (direction) {
             case Direction.UP -> --newRow; // going up means decrementing the row index by 1
             case Direction.DOWN -> ++newRow; // going down means incrementing the row index by 1
             case Direction.LEFT -> --newCol; // going left means decrementing the col index by 1
             case Direction.RIGHT -> ++newCol; // going right means incrementing the col index by 1
             case Direction.NONE -> {
                 return;
             }
             default -> throw new IllegalArgumentException("Invalid direction: " + direction);
         }

        List<List<Constants.Block>> gameMat = Model.getInstance().getGame().getState();
        if (gameMat.get(newRow).get(newCol) != Constants.Block.WALL){
            this.coord = new Cell(newRow,newCol);
        }
     }

    public void addSugar(){
        sugarCount++;
    }

    // GETTERS & SETTERS //
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getSugarCount(){
        return sugarCount;
    }


}
