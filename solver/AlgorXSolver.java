/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {
    // TODO: Add attributes as needed.
    private int size;
    private int[][] layout;
    private boolean[][] coverBoard;
    private final int EMPTY = 0;
    private int minValue;
    private int maxValue;
    private int sectorSize;

    private ColumnNode header;
    private List<DancingNode> answer;



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


        // placeholder
        return true;
    } // end of solve()

    private int getIndex(int row, int column, int value) {
        return (row - 1) * size * size + (column - 1) * size + (value - 1);
    }

    private boolean[][] createExactCoverBoard() {

        int numOfConstraints = 4;

        boolean[][] coverBoard = new boolean[size * size * maxValue][size * size * numOfConstraints];

        int hBase = 0;
        hBase = checkCellConstraint(coverBoard, hBase);
        hBase = checkRowConstraint(coverBoard, hBase);
        hBase = checkColumnConstraint(coverBoard, hBase);
        checkSubsectionConstraint(coverBoard, hBase);

        return coverBoard;
    }

    private int checkSubsectionConstraint(boolean[][] coverBoard, int hBase) {
        for (int row = 1; row <= size; row += sectorSize) {
            for (int column = 1; column <= size; column += sectorSize) {
                for (int n = 1; n <= size; n++, hBase++) {
                    for (int rowDelta = 0; rowDelta < sectorSize; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < sectorSize; columnDelta++) {
                            int index = getIndex(row + rowDelta, column + columnDelta, n);
                            coverBoard[index][hBase] = true;
                        }
                    }
                }
            }
        }
        return hBase;
    }

    private int checkColumnConstraint(boolean[][] coverBoard, int hBase) {
        for (int column = 1; column <= size; column++) {
            for (int n = 1; n <= size; n++, hBase++) {
                for (int row = 1; row <= size; row++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][hBase] = true;
                }
            }
        }
        return hBase;
    }

    private int checkRowConstraint(boolean[][] coverBoard, int hBase) {
        for (int row = 1; row <= size; row++) {
            for (int n = 1; n <= size; n++, hBase++) {
                for (int column = 1; column <= size; column++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][hBase] = true;
                }
            }
        }
        return hBase;
    }

    private int checkCellConstraint(boolean[][] coverBoard, int hBase) {
        for (int row = 1; row <= size; row++) {
            for (int column = 1; column <= size; column++, hBase++) {
                for (int n = 1; n <= size; n++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][hBase] = true;
                }
            }
        }
        return hBase;
    }

    private boolean[][] initializeExactCoverBoard(int[][] board) {
        boolean[][] coverBoard = createExactCoverBoard();
        for (int row = 1; row <= size; row++) {
            for (int column = 1; column <= size; column++) {
                int n = board[row - 1][column - 1];
                if (n != EMPTY) {
                    for (int num = minValue; num <= maxValue; num++) {
                        if (num != n) {
                            Arrays.fill(coverBoard[getIndex(row, column, num)], false);
                        }
                    }
                }
            }
        }
        return coverBoard;
    }

    private ColumnNode makeDLXBoard(boolean[][] grid) {
        int COLS = grid[0].length;

        ColumnNode headerNode = new ColumnNode("header");
        List<ColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.hookRight(n);
        }
        headerNode = headerNode.R.C;

        for (boolean[] aGrid : grid) {
            DancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (aGrid[j]) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);
                    if (prev == null) prev = newNode;
                    col.U.hookDown(newNode);
                    prev = prev.hookRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;

        return headerNode;
    }


    private ColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for (
                ColumnNode c = (ColumnNode) header.R;
                c != header;
                c = (ColumnNode) c.R) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    private void search(int k) {
        if (header.R == header) {
//            handler.handleSolution(answer);
        } else {
            ColumnNode c = selectColumnNodeHeuristic();
            c.cover();

            for (DancingNode r = c.D; r != c; r = r.D) {
                answer.add(r);

                for (DancingNode j = r.R; j != r; j = j.R) {
                    j.C.cover();
                }

                search(k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.C;

                for (DancingNode j = r.L; j != r; j = j.L) {
                    j.C.uncover();
                }
            }
            c.uncover();
        }
    }



    /**
     * Dancing Node Class
     */
    class DancingNode {
        DancingNode L, R, U, D;
        ColumnNode C;

        DancingNode hookDown(DancingNode node) {
            assert (this.C == node.C);
            node.D = this.D;
            node.D.U = node;
            node.U = this;
            this.D = node;
            return node;
        }

        DancingNode hookRight(DancingNode node) {
            node.R = this.R;
            node.R.L = node;
            node.L = this;
            this.R = node;
            return node;
        }

        void unlinkLR() {
            this.L.R = this.R;
            this.R.L = this.L;
        }

        void relinkLR() {
            this.L.R = this.R.L = this;
        }

        void unlinkUD() {
            this.U.D = this.D;
            this.D.U = this.U;
        }

        void relinkUD() {
            this.U.D = this.D.U = this;
        }

        DancingNode() {
            L = R = U = D = this;
        }

        DancingNode(ColumnNode c) {
            this();
            C = c;
        }


    }


    /**
     * Column Node Class
     */
    class ColumnNode extends DancingNode {
        int size;
        String name;

        ColumnNode(String n) {
            super();
            size = 0;
            name = n;
            C = this;
        }

        void cover() {
            unlinkLR();
            for (DancingNode i = this.D; i != this; i = i.D) {
                for (DancingNode j = i.R; j != i; j = j.R) {
                    j.unlinkUD();
                    j.C.size--;
                }
            }
        }

        void uncover() {
            for (DancingNode i = this.U; i != this; i = i.U) {
                for (DancingNode j = i.L; j != i; j = j.L) {
                    j.C.size++;
                    j.relinkUD();
                }
            }
            relinkLR();
        }


    }




} // end of class AlgorXSolver
