/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.List;

import grid.KillerCage;
import grid.KillerSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {
    // TODO: Add attributes as needed.
    private int size;
    private int[][] layout;
    private List<KillerCage> cages;
    private final int EMPTY = 0;
    private int minValue;
    private int maxValue;
    private int sectorSize;

    public KillerBackTrackingSolver() {
        // TODO: any initialisation you want to implement.
    } // end of KillerBackTrackingSolver()

    private void initVariables(SudokuGrid grid) {
        this.layout = ((KillerSudokuGrid) grid).getLayout();
        this.cages = ((KillerSudokuGrid) grid).getCages();
        this.size = ((KillerSudokuGrid) grid).getSize();
        this.minValue = ((KillerSudokuGrid) grid).getMinValue();
        this.maxValue = ((KillerSudokuGrid) grid).getMaxValue();
        this.sectorSize = (int) Math.sqrt(size);
    }


    @Override
    public boolean solve(SudokuGrid grid) {
        // TODO: your implementation of a backtracking solver for Killer Sudoku.
        if (layout == null) {
            initVariables(grid);
        }

        solve();
        ((KillerSudokuGrid) grid).setLayout(layout);
        return grid.validate();

    } // end of solve()


    public boolean solve() {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                if (layout[r][c] == EMPTY) {
                    row = r;
                    col = c;
                    // We still have some remaining missing values in Sudoku
                    isEmpty = false;
                    break;
                }
            }

            if (!isEmpty) {
                break;
            }
        }

        // no empty space left
        if (isEmpty) {
            return true;
        }

        // else for each-row backtrack
        for (int value = 1; value <= size; value++) {
            if (isValid(row, col, value)) {
                setCellValue(row, col, value);
                /**
                 * print grid
                 */
                System.out.println("Inserting... " + value);
                System.out.println(printSudoku());
                /**
                 * end print grid
                 */

                if (solve()) {
                    return true;
                }
                else {
                    // replace it
                    setCellValue(row, col, EMPTY);
                }
            }
        }
        return false;
    }

    public boolean isValid(int row, int col, int value) {
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
        setCellValue(row, col, value);
        KillerCage cage = getCage(row, col);
        int sum = cage.getCurrentSum(layout);
        System.out.println(row + "," + col + " " + sum);


        if (layout[row][col] == EMPTY) {
            setCellValue(row, col, EMPTY);
            return true;
        }
        else {
            if (sum <= cage.getTotal()) {
                setCellValue(row, col, EMPTY);
                return true;
            }
            else {
                setCellValue(row, col, EMPTY);
                return false;
            }
        }
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

    private String printSudoku() {
        StringBuilder grid = new StringBuilder();

        int count = 0;
        for (int[] rw : layout) {
            for (int v : rw) {
                count++;
                if (count == size) {
                    grid.append(v);
                } else {
                    grid.append(v).append(",");
                }
            }
            count = 0;
            grid.append("\n");
        }

        return grid.toString();
    }

} // end of class KillerBackTrackingSolver()
