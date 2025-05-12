package Model.game.utils;

/**
 * Represent a cell in a matrix
 */
public class Cell {
    private Integer row;
    private Integer col;

    public Cell(int row, int col) {
        setCoord(row,col);
    }
    public Cell(Integer row, Integer col) {
        setCoord(row,col);
    }

    /**
     * Default constructor for Cell. Initializes all fields to null to prevent usage of uninitialized Cells.
     */
    public Cell() {
        row = null;
        col = null;
    }

    public void setCoord(int row, int col){
        setRow(row);
        setCol(col);
    }
    public void setCoord(Integer row, Integer col){
        setRow(row);
        setCol(col);
    }

    /**
     * Get the row index of the cell.
     * @return an integer representing the row index of the cell
     * @throws AssertionError if the cell is uninitialized( instantiated by {@code new Cell()} )
     * @see Cell#Cell()
     */
    public int getRow() {
        assert row != null : "Cell row is null, cannot getRow() from uninitialized cell";
        return row;
    }

    /**
     * Get the column index of the cell.
     * @return an integer representing the column index of the cell
     * @throws AssertionError if the cell is uninitialized( instantiated by {@code new Cell()} )
     * @see Cell#Cell()
     */
    public int getCol() {
        assert col != null : "Cell col is null, cannot getCol() from uninitialized cell";
        return col;
    }


    public void setRow(int row) {
        assert row >= 0;
        this.row = row;
    }
    public void setCol(int col) {
        assert col >= 0;
        this.col = col;
    }


}
