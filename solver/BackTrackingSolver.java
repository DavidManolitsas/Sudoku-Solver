/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.stream.IntStream;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
    // TODO: Add attributes as needed.
    private int size;
    private int[][] layout;
    private final int EMPTY = 0;
    private int minValue;
    private int maxValue;
    private int sectorSize;

    public BackTrackingSolver() {
        // TODO: any initialisation you want to implement.
    } // end of BackTrackingSolver()

    private void initBackTrackingSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getRows();
        this.minValue = ((StdSudokuGrid) grid).getMinValue();
        this.maxValue = ((StdSudokuGrid) grid).getMaxValue();
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
                if (layout[row][col] == EMPTY) {
                    //  Cycle through each potential value
                    for (int inputValue = minValue; inputValue <= maxValue; inputValue++) {
                        // Trial and Error the new value
                        layout[row][col] = inputValue;
                        if (isValid(row, col) && solve(grid)) {
                            return true;
                        }
                        // if it is an invalid position set the current position back to empty
                        layout[row][col] = EMPTY;
                    }
                    return false;
                }
            }
        }

        return true;
    } // end of solve()

    private boolean isValid(int row, int col) {
        return (validateRow(row) && validateCol(col) && validateSector(row, col));
    }

    // Check the row constraints
    private boolean validateRow(int row) {
        boolean[] validate = new boolean[size];
        return IntStream.range(0, size).allMatch(column -> checkConstraint(row, column, validate));
    }

    // Check column constraints
    private boolean validateCol(int col) {
        boolean[] validate = new boolean[size];
        return IntStream.range(0, size).allMatch(row -> checkConstraint(row, col, validate));
    }

    // Check constraints of each sub section
    private boolean validateSector(int row, int col) {
        boolean[] validate = new boolean[size];
        int sectorStartRow = (row / sectorSize) * sectorSize;
        int sectorEndRow = sectorStartRow + sectorSize;

        int sectorStartCol = (col / sectorSize) * sectorSize;
        int sectorEndCol = sectorStartCol + sectorSize;

        for (int r = sectorStartRow; r < sectorEndRow; r++) {
            for (int c = sectorStartCol; c < sectorEndCol; c++) {
                if (!checkConstraint(r, c, validate)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkConstraint(int row, int column, boolean[] validate) {
        if (layout[row][column] != EMPTY) {
            if (!validate[layout[row][column] - 1]) {
                validate[layout[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    // For Testing
    @Override
    public String toString() {
        return "size: " + size + "x" +size + ", Min Value: " + minValue + ", Max Value: " + maxValue + ", Sector Size: " + sectorSize + "x" + sectorSize;
    }



} // end of class BackTrackingSolver()
