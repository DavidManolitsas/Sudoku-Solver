/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid
        extends SudokuGrid {

    private int[][] layout;
    private int size;
    private int sectorSize;
    private int[] values;
    private final int EMPTY = 0;

    public StdSudokuGrid() {
        super();

    } // end of StdSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename) throws FileNotFoundException, IOException {
        File inputFile = new File(filename);
        Scanner read = new Scanner(inputFile);
        int lineCount = 0;

        while (read.hasNextLine()) {
            lineCount++;

            if (lineCount == 1) {
                // initialize the size of the grid
                this.size = Integer.parseInt(read.nextLine());
                this.sectorSize = (int) Math.sqrt(size);
                layout = new int[size][size];
            } else if (lineCount == 2) {
                // assign the smallest and largest value of the grid
                String[] line = read.nextLine().split(" ");
                initValues(line);
            } else if (lineCount > 2) {
                // insert all values into the grid
                String[] line = read.nextLine().split("\\W");
                int row = Integer.parseInt(line[0]);
                int col = Integer.parseInt(line[1]);
                int value = Integer.parseInt(line[2]);
                // assign value to the sudoku layout
                layout[row][col] = value;
            }
        }

    } // end of initBoard()

    private void initValues(String[] line) {
        values = new int[size];
        for (int i = 0; i < line.length; i++) {
            values[i] = Integer.parseInt(line[i]);
        }

        values = selectionSort(values);

    }

    private int[] selectionSort(int[] values) {
        int length = values.length;

        for (int i = 0; i < length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (values[j] < values[minIndex]) {
                    minIndex = j;
                }
            }

            int temp = values[minIndex];
            values[minIndex] = values[i];
            values[i] = temp;
        }

        return values;
    }


    @Override
    public void outputGrid(String filename) throws FileNotFoundException, IOException {
        PrintWriter printWriter = new PrintWriter(filename);
        printWriter.print(toString());
        printWriter.close();

    } // end of outputBoard()


    @Override
    public String toString() {

        StringBuilder grid = new StringBuilder();

        int count = 0;
        for (int[] row : layout) {
            for (int value : row) {
                count++;
                if (count == size) {
                    grid.append(value);
                } else {
                    grid.append(value).append(",");
                }
            }
            count = 0;
            grid.append("\n");
        }
        // placeholder
        return String.valueOf(grid).trim();
    } // end of toString()


    @Override
    public boolean validate() {
        // Validate the sudoku grid
        for (int i = 0; i < size; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            Set<Integer> sectorSet = new HashSet<>();

            for (int j = 0; j < size; j++) {
                // Base case, check if the value is in the appropriate range
                if (layout[i][j] < values[0] || layout[i][j] > values[size - 1]) {
                    return false;
                }
                // Check if the value layout[i][j] can be added to the Row Set
                if(layout[i][j] == EMPTY && !rowSet.add(layout[i][j])) {
                    return false;
                }
                // Check if the value layout[i][j] can be added to the Column Set
                if(layout[j][i] == EMPTY && !colSet.add(layout[j][i])) {
                    return false;
                }
                int rowIndex = sectorSize * (i/sectorSize);
                int colIndex = sectorSize * (i % sectorSize);
                // Check if the value layout[i][j] can be added to the Sector Set
                if(layout[rowIndex + j/sectorSize][colIndex + j % sectorSize] != EMPTY &&
                        !sectorSet.add(layout[rowIndex + j/sectorSize][colIndex + j % sectorSize])) {
                    return false;
                }
            }
        }

        return true;
    } // end of validate()


    //getters
    public int[][] getLayout() {
        return layout;
    }

    public void setLayout(int[][] layout) {
        this.layout = layout;
    }

    public int[] getValues() {
        return values;
    }

    public int getSize() {
        return size;
    }


} // end of class StdSudokuGrid
