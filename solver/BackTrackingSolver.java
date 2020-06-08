/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
    private int size;
    private int[][] layout;
    private final int EMPTY = 0;
    private int[] values;
    private int sectorSize;

    public BackTrackingSolver() {

    } // end of BackTrackingSolver()

    private void initBackTrackingSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getSize();
        this.values = ((StdSudokuGrid) grid).getValues();
        this.sectorSize = (int) Math.sqrt(size);
    }

    @Override
    public boolean solve(SudokuGrid grid) {
        if (layout == null) {
            initBackTrackingSolver(grid);
        }

        // Iterate through the grid
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Check if the position is empty
                if (getCellValue(row, col) == EMPTY) {

                    //  Cycle through each potential value
                    for (int inputValue : values) {
                        // Check if the input value is valid
                        if (isValid(row, col, inputValue)) {
                            // if input value is valid, set input value to the layout
                            setCellValue(row, col, inputValue);
                            // Recurse
                            if (solve(grid)) {
                                return true;
                            } else {
                                // if it is an invalid position set the current position back to empty
                                setCellValue(row, col, EMPTY);
                            }
                        }
                    }
                    return false;
                }
            }
        }

        return true;

    } // end of solve()


    public boolean isValid(int row, int col, int value) {
        return (validateRow(row, value) && validateColumn(col, value) && validateSector(row, col, value));
    }

    private boolean validateRow(int row, int value) {
        // Validate the row
        for (int col = 0; col < size; col++) {
            // if the number we are trying to place is already present in that row, return false;
            if (getCellValue(row, col) == value) {
                return false;
            }
        }
        return true;
    }

    private boolean validateColumn(int col, int value) {
        // Validate column
        for (int row = 0; row < size; row++) {
            // if the number we are trying to place is already present in that column, return false;
            if (getCellValue(row, col) == value) {
                return false;
            }
        }
        return true;
    }

    private boolean validateSector(int row, int col, int value) {
        // Validate sub sector
        int sectorStartRow = row - row % sectorSize;
        int sectorEndRow = sectorStartRow + sectorSize;
        int sectorStartCol = col - col % sectorSize;
        int sectorEndCol = sectorStartCol + sectorSize;


        for (int r = sectorStartRow; r < sectorEndRow; r++) {
            for (int c = sectorStartCol; c < sectorEndCol; c++) {
                if (getCellValue(r, c) == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setCellValue(int row, int col, int value) {
        layout[row][col] = value;
    }

    private int getCellValue(int row, int col) {
        return layout[row][col];
    }

} // end of class BackTrackingSolver()
