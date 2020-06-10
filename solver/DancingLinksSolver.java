/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import dancingLinkNodes.ColumnNode;
import dancingLinkNodes.DancingNode;
import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver {

    // Sudoku Grid details
    private int[][] layout;
    private int size;
    private final int EMPTY = 0;
    private int sectorSize;
    private int[] values;
    //Cover Board
    private int[][] coverBoard;
    //Dancing Link
    private ColumnNode head;
    private List<DancingNode> partialSolution;
    private int[][] solution;

    public DancingLinksSolver() {

    } // end of DancingLinksSolver()

    private void initDancingLinkSolver(SudokuGrid grid) {
        this.layout = ((StdSudokuGrid) grid).getLayout();
        this.size = ((StdSudokuGrid) grid).getSize();
        this.values = ((StdSudokuGrid) grid).getValues();
        this.sectorSize = (int) Math.sqrt(size);
    }

    @Override
    public boolean solve(SudokuGrid grid) {
        if (layout == null) {
            initDancingLinkSolver(grid);
        }

        //create cover board
        coverBoard = initCoverBoard(layout);
        //Initialise Dancing Link
        initDancingLink(coverBoard);
        //Solve the sudoku board and set the solved layout to the Sudoku Grid
        solve();
        ((StdSudokuGrid) grid).setLayout(getSolution());

        return grid.validate();

    } // end of solve()

    private void initDancingLink(int[][] coverBoard) {
        this.head = makeDLXBoard(coverBoard);
    }

    //region start solve sudoku
    public void solve() {
        partialSolution = new LinkedList<DancingNode>();
        search(0);
    }

    private void search(int key) {
        // Solution found
        if (head.getRight() == head) {
            setSolution(partialSolution);
        } else {
            // Find column with the least amount of values
            ColumnNode col = findMinCol();
            col.cover();

            for (DancingNode row = col.getDown(); row != col; row = row.getDown()) {
                // Add row to the partial solution
                partialSolution.add(row);

                // Cover columns in the row
                for (DancingNode rowNode = row.getRight(); rowNode != row; rowNode = rowNode.getRight()) {
                    rowNode.getColNode().cover();
                }

                // Backtrack
                search(key + 1);

                // No solution found
                row = partialSolution.remove(partialSolution.size() - 1);
                col = row.getColNode();
                // Uncover the board
                for (DancingNode rowNode = row.getLeft(); rowNode != row; rowNode = rowNode.getLeft()) {
                    rowNode.getColNode().uncover();
                }
            }

            col.uncover();
        }
    }

    private ColumnNode findMinCol() {
        int min = Integer.MAX_VALUE;
        ColumnNode colNode = null;
        for (ColumnNode col = (ColumnNode) head.getRight(); col != head; col = (ColumnNode) col.getRight()) {
            if (col.getSize() < min) {
                min = col.getSize();
                colNode = col;
            }
        }
        return colNode;
    }

    private ColumnNode makeDLXBoard(int[][] layout) {
        int columns = layout[0].length;

        ColumnNode headNode = new ColumnNode("head");
        List<ColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < columns; i++) {
            ColumnNode node = new ColumnNode(Integer.toString(i));
            columnNodes.add(node);
            headNode = (ColumnNode) headNode.linkRight(node);
        }
        headNode = headNode.getRight().getColNode();

        for (int[] row : layout) {
            DancingNode prevNode = null;

            for (int j = 0; j < columns; j++) {

                if (row[j] == 1) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);

                    if (prevNode == null) {
                        prevNode = newNode;
                    }

                    col.getUp().linkDown(newNode);
                    prevNode = prevNode.linkRight(newNode);
                    col.incrementSize();
                }
            }
        }

        headNode.setSize(columns);

        return headNode;
    }
    //endregion

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
        for (int row = 1; row <= size; row += sectorSize) {
            for (int col = 1; col <= size; col += sectorSize) {
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
        for (int col = 1; col <= size; col++) {
            for (int valueIndex = 0; valueIndex < values.length; valueIndex++, head++) {
                for (int row = 1; row <= size; row++) {
                    int index = getIndex(row, col, valueIndex);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }


    private int createRowConstraints(int[][] coverBoard, int head) {
        for (int row = 1; row <= size; row++) {
            for (int valueIndex = 0; valueIndex < values.length; valueIndex++, head++) {
                for (int col = 1; col <= size; col++) {
                    int index = getIndex(row, col, valueIndex);
                    coverBoard[index][head] = 1;
                }
            }
        }

        return head;
    }

    private int createCellConstraints(int[][] coverBoard, int head) {
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++, head++) {
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
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
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

    private int[][] convertToSudokuBoard(List<DancingNode> answer) {
        int[][] solution = new int[size][size];

        for (DancingNode node : answer) {
            DancingNode rowColNode = node;
            int min = Integer.parseInt(rowColNode.getColNode().getName());

            for (DancingNode temp = node.getRight(); temp != node; temp = temp.getRight()) {
                int val = Integer.parseInt(temp.getColNode().getName());
                if (val < min) {
                    min = val;
                    rowColNode = temp;
                }
            }

            int ans1 = Integer.parseInt(rowColNode.getColNode().getName());
            int ans2 = Integer.parseInt(rowColNode.getRight().getColNode().getName());
            int row = ans1 / size;
            int col = ans1 % size;
            int value = values[ans2 % size];
            solution[row][col] = value;
        }

        return solution;
    }

    private void setSolution(List<DancingNode> answer) {
        solution = convertToSudokuBoard(answer);
    }

    private int[][] getSolution() {
        return solution;
    }

} // end of class DancingLinksSolver
