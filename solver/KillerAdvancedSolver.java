/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.List;

import grid.KillerCage;
import grid.KillerSudokuGrid;
import grid.SudokuGrid;


/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver {
    private int size;
    private int[][] layout;
    private List<KillerCage> cages;
    private final int EMPTY = 0;
    private int[] values;
    private int sectorSize;

    public KillerAdvancedSolver() {
        // TODO: any initialisation you want to implement.
    } // end of KillerAdvancedSolver()

    private void initVariables(SudokuGrid grid) {
        this.layout = ((KillerSudokuGrid) grid).getLayout();
        this.cages = ((KillerSudokuGrid) grid).getCages();
        this.size = ((KillerSudokuGrid) grid).getSize();
        this.values =((KillerSudokuGrid) grid).getValues();
        this.sectorSize = (int) Math.sqrt(size);
    }

    @Override
    public boolean solve(SudokuGrid grid) {
        // initialise the variables of the sudoku grid
        if (layout == null) {
            initVariables(grid);
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
                            // Recurse, start backtracking
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
        // Check constraints of the sudoku board
        return (validateRow(row, value) && validateColumn(col, value) && validateSector(row, col, value) && validateCage(row, col, value));
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

    private boolean validateCage(int row, int col, int value) {
        // Validate Cage
        int sum = 0;
        KillerCage cage = getCage(row, col);
        // Loop through every position
        for (int[] pos : cage.getPositions()) {
            if (pos[0] == row && pos[1] == col) {
                // increment sum
                sum += value;
            } else if (getCellValue(pos[0], pos[1]) == EMPTY) {
                // The cage has an empty space
                return true;
            } else {
                sum += getCellValue(pos[0], pos[1]);
            }

        }

        return sum == cage.getTotal();

    }


    public KillerCage getCage(int row, int col) {
        for (KillerCage cage : cages) {
            for (int[] pos : cage.getPositions()) {
                if (pos[0] == row && pos[1] == col) {
                    return cage;
                }
            }
        }
        return null;
    }

    public void setCellValue(int row, int col, int value) {
        layout[row][col] = value;
    }


    public int getCellValue(int row, int col) {
        return layout[row][col];
    }
} // end of class KillerAdvancedSolver
