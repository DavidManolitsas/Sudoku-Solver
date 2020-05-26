/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.Arrays;

import dancingLink.DancingLink;
import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{
    // TODO: Add attributes as needed.
    private int[][] layout;
    private int size;
    private final int EMPTY = 0;
    private int minValue;
    private int maxValue;
    private int sectorSize;

    private int[][] matrix;

    private final int COVER_START_INDEX = 1;

    public DancingLinksSolver() {
        // TODO: any initialisation you want to implement.
    } // end of DancingLinksSolver()

    private void initDancingLinkSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getRows();
        this.minValue = ((StdSudokuGrid) grid).getMinValue();
        this.maxValue = ((StdSudokuGrid) grid).getMaxValue();
        this.sectorSize = (int) Math.sqrt(size);
    }

    @Override
    public boolean solve(SudokuGrid grid) {
        if (layout == null) {
            initDancingLinkSolver(grid);
        }

        // TODO: your implementation of the dancing links solver for Killer Sudoku.
        //create cover board
        matrix = initCoverMatrix(layout);
        DancingLink dlx = new DancingLink(matrix, size);
        dlx.solve();
        ((StdSudokuGrid) grid).setLayout(dlx.getSolvedSudoku());

        return grid.validate();

    } // end of solve()


    private int getIndexInMatrix(int row, int column, int value) {
        return (row - 1) * size * size + (column - 1) * size + (value - 1);
    }

    private int[][] createCoverMatrix() {
        int[][] coverMatrix = new int [size * size * maxValue][size * size * 4];

        int header  = 0;
        header = createCellConstraints(coverMatrix, header);
        header = createRowConstraints(coverMatrix, header);
        header = createColumnConstraints(coverMatrix, header);
        createBoxConstraints(coverMatrix, header);

        return coverMatrix;
    }

    private int createBoxConstraints(int[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= size; row += sectorSize) {
            for (int column = COVER_START_INDEX; column <= size; column += sectorSize) {
                for (int n = COVER_START_INDEX; n <= size; n++, header++) {
                    for (int rowDelta = 0; rowDelta < sectorSize; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < sectorSize; columnDelta++) {
                            int index = getIndexInMatrix(row + rowDelta, column + columnDelta, n);
                            matrix[index][header] = 1;
                        }
                    }
                }
            }
        }

        return header;
    }

    private int createColumnConstraints(int[][] matrix, int header) {
        for (int column = COVER_START_INDEX; column <= size; column++) {
            for (int n = COVER_START_INDEX; n <= size; n++, header++) {
                for (int row = COVER_START_INDEX; row <= size; row++) {
                    int index = getIndexInMatrix(row, column, n);
                    matrix[index][header] = 1;
                }
            }
        }

        return header;
    }

    private int createRowConstraints(int[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int n = COVER_START_INDEX; n <= size; n++, header++) {
                for (int column = COVER_START_INDEX; column <= size; column++) {
                    int index = getIndexInMatrix(row, column, n);
                    matrix[index][header] = 1;
                }
            }
        }

        return header;
    }

    private int createCellConstraints(int[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int column = COVER_START_INDEX; column <= size; column++, header++) {
                for (int n = COVER_START_INDEX; n <= size; n++) {
                    int index = getIndexInMatrix(row, column, n);
                    matrix[index][header] = 1;
                }
            }
        }

        return header;
    }

    private int[][] initCoverMatrix(int[][] layout) {
        int[][] coverMatrix = createCoverMatrix();

        // Taking into account the values already entered in Sudoku's grid instance
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int column = COVER_START_INDEX; column <= size; column++) {
                int n = layout[row - 1][column - 1];

                if (n != EMPTY) {
                    for (int num = minValue; num <= maxValue; num++) {
                        if (num != n) {
                            Arrays.fill(coverMatrix[getIndexInMatrix(row, column, num)], 0);
                        }
                    }
                }
            }
        }

        return coverMatrix;
    }

} // end of class DancingLinksSolver
