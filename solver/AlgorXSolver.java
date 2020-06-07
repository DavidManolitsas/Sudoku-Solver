/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver
        extends StdSudokuSolver {

    // Sudoku Grid details
    private int[][] layout;
    private int size;
    private int[] values;
    private final int EMPTY = 0;
    private Stack<Integer> partialSolution;
    private ArrayList<Integer> columns;
    private int sectorSize;
    //Cover Board
    private int[][] coverBoard;
    private final int COVER_START_INDEX = 1;


    public AlgorXSolver() {

    } // end of AlgorXSolver()

    private void initAlgorXSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getSize();
        this.values = ((StdSudokuGrid) grid).getValues();
        this.sectorSize = (int) Math.sqrt(size);
        this.partialSolution = new Stack<Integer>();
    }

    private void initColumns() {
        this.columns = new ArrayList<Integer>();
        for (int i = 0; i < coverBoard[0].length; i++) {
            columns.add(i);
        }
    }


    @Override
    public boolean solve(SudokuGrid grid) {
        if (layout == null) {
            initAlgorXSolver(grid);
        }

        coverBoard = initCoverBoard(layout);
        initColumns();
        solve(coverBoard, columns);
        layout = convertToSudokuBoard();
        ((StdSudokuGrid) grid).setLayout(layout);

        return grid.validate();
    } // end of solve()


    /**
     * The following method is based off the sudo code below, derived from Donald Knuth’s Algorithm X:
     * <p>
     * 1. If the matrix A has no columns, the current partial solution is a valid solution; terminate successfully.
     * 2. Otherwise, choose a column c (deterministically).
     * 3. Choose a row r such that A[r] = 1 (non-deterministically).
     * 4. Include row r in the partial solution.
     * 5. For each column j such that A[r][j] = 1,
     * for each row i such that A[i][j] = 1,
     * delete row i from matrix A.
     * delete column j from matrix A.
     * 6. Repeat this algorithm recursively on the reduced matrix A.
     *
     * @param coverBoard
     *         exact cover board used to solve the sudoku as an exact cover problem
     * @param columns
     *         a list of column indexs in the exact cover grid
     *
     * @return true/false depending on if the sudoku is solved
     */
    public boolean solve(int[][] coverBoard, ArrayList<Integer> columns) {

        if (columns.size() == 0) {
            return true;
        }

        int col = findMinCol(coverBoard, columns);
        for (int row = 0; row < coverBoard.length; row++) {
            if (coverBoard[row][col] == 1) {
                partialSolution.push(row);

                List<Integer> removedCols = new ArrayList<Integer>();
                for (int c = 0; c < coverBoard[0].length; c++) {
                    if (coverBoard[row][c] == 1) {
                        removedCols.add(c);
                    }
                }
                columns.removeAll(removedCols);

                List<int[]> cover = cover(coverBoard, row);

                if (solve(coverBoard, columns)) {
                    return true;
                }

                columns.addAll(removedCols);
                uncover(coverBoard, cover);
                partialSolution.pop();

            }
        }
        return false;
    }

    private List<int[]> cover(int[][] coverBoard, int row) {
        List<int[]> cover = new ArrayList<>();

        for (int col = 0; col < coverBoard[row].length; col++) {
            if (coverBoard[row][col] == 1) {

                for (int r = 0; r < coverBoard.length; r++) {
                    if (coverBoard[r][col] == 1) {

                        for (int c = 0; c < coverBoard[row].length; c++) {
                            if (coverBoard[r][c] == 1 && r != row) {
                                coverBoard[r][c] = 0;
                                int[] rowCol = {r, c};
                                cover.add(rowCol);
                            }
                        }
                    }
                }
                coverBoard[row][col] = 0;
                int[] rowCol = {row, col};
                cover.add(rowCol);

            }
        }

        return cover;
    }


    private void uncover(int[][] coverBoard, List<int[]> cover) {
        for (int[] rowCol : cover) {
            coverBoard[rowCol[0]][rowCol[1]] = 1;
        }
    }


    public int findMinCol(int[][] coverBoard, ArrayList<Integer> columns) {
        int min = Integer.MAX_VALUE;
        int chosenCol = -1;

        for (Integer col : columns) {
            int count = 0;
            for (int row = 0; row < coverBoard.length; row++) {

                if (coverBoard[row][col] == 1) {
                    count++;
                }
            }
            if (count < min) {
                min = count;
                chosenCol = col;
            }
        }
        return chosenCol;
    }


    //region create cover board
    private int[][] createCoverBoard() {
        int[][] coverBoard = new int[size * size * size][size * size * 4];

        int head = 0;
        head = createCellConstraints(coverBoard, head);
        head = createRowConstraints(coverBoard, head);
        head = createColumnConstraints(coverBoard, head);
        createBoxConstraints(coverBoard, head);

        return coverBoard;
    }

    private int createBoxConstraints(int[][] coverBoard, int head) {
        for (int row = COVER_START_INDEX; row <= size; row += sectorSize) {
            for (int col = COVER_START_INDEX; col <= size; col += sectorSize) {
                for (int valueIndex = 0; valueIndex < values.length; valueIndex++, head++) {
                    for (int rowDelta = 0; rowDelta < sectorSize; rowDelta++) {
                        for (int colDelta = 0; colDelta < sectorSize; colDelta++) {
                            int index = getIndex(row + rowDelta, col + colDelta, valueIndex);
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
            for (int valueIndex = 0; valueIndex < values.length; valueIndex++, head++) {
                for (int row = COVER_START_INDEX; row <= size; row++) {
                    int index = getIndex(row, col, valueIndex);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }


    private int createRowConstraints(int[][] coverBoard, int head) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int valueIndex = 0; valueIndex < values.length; valueIndex++, head++) {
                for (int col = COVER_START_INDEX; col <= size; col++) {
                    int index = getIndex(row, col, valueIndex);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }

    private int createCellConstraints(int[][] coverBoard, int head) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int col = COVER_START_INDEX; col <= size; col++, head++) {
                for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
                    int index = getIndex(row, col, valueIndex);
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
                    for (int valueIndex = 0; valueIndex < values.length; valueIndex++) {
                        if (values[valueIndex] != num) {
                            Arrays.fill(coverBoard[getIndex(row, col, valueIndex)], 0);
                        }
                    }
                }
            }
        }

        return coverBoard;
    }


    private int getIndex(int row, int column, int valueIndex) {
        return (row - 1) * size * size + (column - 1) * size + valueIndex;
    }
    //endregion

    private int[][] convertToSudokuBoard() {
        int[][] solution = new int[size][size];

        for (Integer index : partialSolution) {
            int row = index / (size * size);
            int col = (index / size) % size;
            int value = values[index % size];
            solution[row][col] = value;
        }

        return solution;
    }

    //TODO: for testing
    public void printCoverBoard() {
        System.out.println("\nExact Cover Board:");
        for (int i = 0; i < coverBoard.length; i++) {
            for (int j = 0; j < coverBoard[0].length; j++) {
                System.out.print(coverBoard[i][j] + " ");
            }

            System.out.println();
        }
    }

} // end of class AlgorXSolver
