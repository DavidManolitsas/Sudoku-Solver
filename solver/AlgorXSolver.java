/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.Arrays;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {

    // Sudoku Grid details
    private int[][] layout;
    private int size;
    private final int EMPTY = 0;
    private int minValue;
    private int maxValue;
    private int sectorSize;
    //Cover Board
    private int[][] coverBoard;
    private final int COVER_START_INDEX = 1;


    public AlgorXSolver() {
        // TODO: any initialisation you want to implement.
    } // end of AlgorXSolver()

    private void initAlgorXSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getSize();
        this.sectorSize = (int) Math.sqrt(size);
    }

    @Override
    public boolean solve(SudokuGrid grid) {
        if (layout == null) {
            initAlgorXSolver(grid);
        }
        // TODO: your implementation of the Algorithm X solver for standard Sudoku.
        coverBoard = initCoverBoard(layout);
        printCoverBoard();


        // placeholder
        return true;
    } // end of solve()


    //region create cover board
    private int[][] createCoverBoard() {
        int[][] coverBoard = new int [size * size * maxValue][size * size * 4];

        int head  = 0;
        head = createCellConstraints(coverBoard, head);
        head = createRowConstraints(coverBoard, head);
        head = createColumnConstraints(coverBoard, head);
        createBoxConstraints(coverBoard, head);

        return coverBoard;
    }

    private int createBoxConstraints(int[][] coverBoard, int head) {
        for (int row = COVER_START_INDEX; row <= size; row += sectorSize) {
            for (int col = COVER_START_INDEX; col <= size; col += sectorSize) {
                for (int value = COVER_START_INDEX; value <= size; value++, head++) {
                    for (int rowDelta = 0; rowDelta < sectorSize; rowDelta++) {
                        for (int colDelta = 0; colDelta < sectorSize; colDelta++) {
                            int index = getIndex(row + rowDelta, col + colDelta, value);
                            coverBoard[index][head] = 1;
                        }
                    }
                }
            }
        }

        return head;
    }

    private int createColumnConstraints(int[][] coverBoard, int head) {
        for (int col = COVER_START_INDEX; col <= size; col++) {
            for (int value = COVER_START_INDEX; value <= size; value++, head++) {
                for (int row = COVER_START_INDEX; row <= size; row++) {
                    int index = getIndex(row, col, value);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }


    private int createRowConstraints(int[][] coverBoard, int head) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int value = COVER_START_INDEX; value <= size; value++, head++) {
                for (int col = COVER_START_INDEX; col <= size; col++) {
                    int index = getIndex(row, col, value);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }

    private int createCellConstraints(int[][] coverBoard, int head) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int col = COVER_START_INDEX; col <= size; col++, head++) {
                for (int value = COVER_START_INDEX; value <= size; value++) {
                    int index = getIndex(row, col, value);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }

    private int[][] initCoverBoard(int[][] layout) {

        int[][] coverBoard = createCoverBoard();

        // Taking into account the values already entered in Sudoku's grid instance
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int col = COVER_START_INDEX; col <= size; col++) {
                int num = layout[row - 1][col - 1];

                if (num != EMPTY) {
                    for (int value = minValue; value <= maxValue; value++) {
                        if (value != num) {
                            Arrays.fill(coverBoard[getIndex(row, col, value)], 0);
                        }
                    }
                }
            }
        }

        return coverBoard;
    }

    private int getIndex(int row, int column, int value) {
        return (row - 1) * size * size + (column - 1) * size + (value - 1);
    }
    //endregion


    public void printCoverBoard() {
        System.out.println("\nExact Cover Board:");
        for(int i = 0; i < coverBoard.length; i++) {
            for (int j = 0; j < coverBoard.length; j++) {
                System.out.print(coverBoard[i][j] + " ");
            }

            System.out.println();
        }
    }

} // end of class AlgorXSolver
