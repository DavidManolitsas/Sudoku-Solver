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
    // TODO: Add attributes as needed.
    private int[][] layout;
    private int size;
    private final int EMPTY = 0;
    private int minValue;
    private int maxValue;
    private int sectorSize;

    private int[][] matrix;

    private final int COVER_START_INDEX = 1;

    public AlgorXSolver() {
        // TODO: any initialisation you want to implement.
    } // end of AlgorXSolver()

    private void initAlgorXSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getRows();
        this.minValue = ((StdSudokuGrid) grid).getMinValue();
        this.maxValue = ((StdSudokuGrid) grid).getMaxValue();
        this.sectorSize = (int) Math.sqrt(size);
    }

    @Override
    public boolean solve(SudokuGrid grid) {
        if (layout == null) {
            initAlgorXSolver(grid);
        }
        // TODO: your implementation of the Algorithm X solver for standard Sudoku.
        matrix = initCoverMatrix(layout);


        // placeholder
        return true;
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





//    private int getIndex(int row, int column, int value) {
//        return (row - 1) * size * size + (column - 1) * size + (value - 1);
//    }
//
//    private boolean[][] createExactCoverMatrix() {
//
//        int numOfConstraints = 4;
//
//        boolean[][] matrix = new boolean[size * size * maxValue][size * size * numOfConstraints];
//
//        int hBase = 0;
//        hBase = checkCellConstraint(matrix, hBase);
//        hBase = checkRowConstraint(matrix, hBase);
//        hBase = checkColumnConstraint(matrix, hBase);
//        checkSubsectionConstraint(matrix, hBase);
//
//        return matrix;
//    }
//
//    private int checkSubsectionConstraint(boolean[][] coverMatrix, int hBase) {
//        for (int row = 1; row <= size; row += sectorSize) {
//            for (int column = 1; column <= size; column += sectorSize) {
//                for (int n = 1; n <= size; n++, hBase++) {
//                    for (int rowDelta = 0; rowDelta < sectorSize; rowDelta++) {
//                        for (int columnDelta = 0; columnDelta < sectorSize; columnDelta++) {
//                            int index = getIndex(row + rowDelta, column + columnDelta, n);
//                            coverMatrix[index][hBase] = true;
//                        }
//                    }
//                }
//            }
//        }
//        return hBase;
//    }
//
//    private int checkColumnConstraint(boolean[][] coverMatrix, int hBase) {
//        for (int column = 1; column <= size; column++) {
//            for (int n = 1; n <= size; n++, hBase++) {
//                for (int row = 1; row <= size; row++) {
//                    int index = getIndex(row, column, n);
//                    coverMatrix[index][hBase] = true;
//                }
//            }
//        }
//        return hBase;
//    }
//
//    private int checkRowConstraint(boolean[][] coverMatrix, int hBase) {
//        for (int row = 1; row <= size; row++) {
//            for (int n = 1; n <= size; n++, hBase++) {
//                for (int column = 1; column <= size; column++) {
//                    int index = getIndex(row, column, n);
//                    coverMatrix[index][hBase] = true;
//                }
//            }
//        }
//        return hBase;
//    }
//
//    private int checkCellConstraint(boolean[][] coverMatrix, int hBase) {
//        for (int row = 1; row <= size; row++) {
//            for (int column = 1; column <= size; column++, hBase++) {
//                for (int n = 1; n <= size; n++) {
//                    int index = getIndex(row, column, n);
//                    coverMatrix[index][hBase] = true;
//                }
//            }
//        }
//        return hBase;
//    }
//
//    private boolean[][] initExactCoverMatrix(int[][] layout) {
//        boolean[][] matrix = createExactCoverMatrix();
//        for (int row = 1; row <= size; row++) {
//            for (int column = 1; column <= size; column++) {
//                int n = layout[row - 1][column - 1];
//                if (n != EMPTY) {
//                    for (int num = minValue; num <= maxValue; num++) {
//                        if (num != n) {
//                            Arrays.fill(matrix[getIndex(row, column, num)], false);
//                        }
//                    }
//                }
//            }
//        }
//        return matrix;
//    }
//
    public void printMatrix() {
        System.out.println("\nExact Cover Matrix:");
//
//        StringBuilder constraints = new StringBuilder();
//        String name = "      Cell Constraints                Row Constraints                 Column Constraints              Block Constraints               \n";
//        String vals = "      1       2       3       4       1       2       3       4       1       2       3       4       1       2       3       4       \n";
//        String nums = "      1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4 \n";
//        String keys = "r c # - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
//        constraints.append(name).append(vals).append(nums).append(keys);
//        System.out.println(constraints);
//        int row = 0;


        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }

            System.out.println();
        }
    }

} // end of class AlgorXSolver
