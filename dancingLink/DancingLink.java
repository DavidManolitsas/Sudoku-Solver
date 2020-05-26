package dancingLink;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author David Manolitsas
 * @project rmitSudoku
 * @date 2020-05-26
 */
public class DancingLink {

    private ColumnNode header;
    private List<DancingNode> answer;
    private int[][] solvedSudoku;
    private int size;

    public DancingLink(int[][] cover, int size) {
        this.size = size;
        this.header = makeDLXBoard(cover);
        solvedSudoku = new int[size][size];
    }


    public void solve() {
        answer = new LinkedList<DancingNode>();
        search(0);
    }

    private void search(int k) {
        if (header.right == header) {
            handleSolution(answer);
        } else {
            ColumnNode c = selectColumnNodeHeuristic();
            c.cover();

            for (DancingNode r = c.bottom; r != c; r = r.bottom) {
                answer.add(r);

                for (DancingNode j = r.right; j != r; j = j.right) {
                    j.column.cover();
                }

                search(k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.column;

                for (DancingNode j = r.left; j != r; j = j.left) {
                    j.column.uncover();
                }
            }
            c.uncover();
        }
    }

    private ColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for (ColumnNode c = (ColumnNode) header.right; c != header; c = (ColumnNode) c.right) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    private ColumnNode makeDLXBoard(int[][] grid) {
        final int COLS = grid[0].length;

        ColumnNode headerNode = new ColumnNode("header");
        List<ColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.linkRight(n);
        }
        headerNode = headerNode.right.column;

        for (int[] aGrid : grid) {
            DancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (aGrid[j] == 1) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);
                    if (prev == null)
                        prev = newNode;
                    col.top.linkDown(newNode);
                    prev = prev.linkRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;

        return headerNode;
    }


    private void handleSolution(List<DancingNode> answer) {
        int[][] result = convertToSudokuBoard(answer);
        printSolution(result);
    }

    private void printSolution(int[][] result) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                solvedSudoku[i][j] = result[i][j];
            }
        }
    }

    private int[][] convertToSudokuBoard(List<DancingNode> answer) {
        int[][] result = new int[size][size];

        for (DancingNode n : answer) {
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.column.name);

            for (DancingNode temp = n.right; temp != n; temp = temp.right) {
                int val = Integer.parseInt(temp.column.name);
                if (val < min) {
                    min = val;
                    rcNode = temp;
                }
            }

            int ans1 = Integer.parseInt(rcNode.column.name);
            int ans2 = Integer.parseInt(rcNode.right.column.name);
            int r = ans1 / size;
            int c = ans1 % size;
            int num = (ans2 % size) + 1;
            result[r][c] = num;
        }

        return result;
    }

    public int[][] getSolvedSudoku() {
        return solvedSudoku;
    }
}
