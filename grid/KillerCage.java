package grid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Manolitsas
 * @project rmitSudoku
 * @date 2020-05-29
 */
public class KillerCage {

    private int total;
    List<int[]> positions;

    public KillerCage() {
        positions = new ArrayList<int[]>();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<int[]> getPositions() {
        return positions;
    }

    public void setPositions(List<int[]> positions) {
        this.positions = positions;
    }

    public int getCurrentSum(int[][] layout) {
        int sum = 0;
        for(int[] rowCol : positions) {
            int value = layout[rowCol[0]][rowCol[1]];
            sum += value;
        }

        return sum;
    }

    public boolean isFull(int[][] layout) {
        return true;
    }


    //TODO: this is for testing, delete before submission
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append(total).append(" ");

        for (int[] rowCol : positions) {
            string.append(rowCol[0]).append(",").append(rowCol[1]).append(" ");
        }

        return string.toString();
    }
}
