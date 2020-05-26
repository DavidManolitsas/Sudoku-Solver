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
public class StdSudokuGrid extends SudokuGrid {
    // TODO: Add your own attributes
    private int [][] layout;
    private int rows;
    private int cols;
    private int sectorSize;
    private int minValue;
    private int maxValue;
    private final int EMPTY = 0;

    public StdSudokuGrid() {
        super();
        // TODO: any necessary initialisation at the constructor
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
                int size = Integer.parseInt(read.nextLine());
                this.rows = size;
                this.cols = size;
                this.sectorSize = (int) Math.sqrt(size);
                layout = new int[size][size];
            }
            else if(lineCount == 2) {
                // assign the smallest and largest value of the grid
                String[] line = read.nextLine().split(" ");
                for (int i = 0; i < line.length; i++){
                    if (i == 0){
                        minValue = Integer.parseInt(line[i]);
                    }
                    else if (i == line.length - 1) {
                        maxValue = Integer.parseInt(line[i]);
                    }
                }
            }
            else if (lineCount > 2) {
                // insert all values into the grid
                String[] line = read.nextLine().split("\\W");
                int row = Integer.parseInt(line[0]);
                int col = Integer.parseInt(line[1]);
                int value = Integer.parseInt(line[2]);
                // assign value to the sudoku layout
                layout[row][col] = value;
            }
        }

        //TODO: testing
        System.out.println("size of the grid is: " + rows + "x" + cols + "\nMin value: " + minValue + " Max value: " + maxValue  + " , Sectors are " + sectorSize + "x" + sectorSize);
    } // end of initBoard()


    @Override
    public void outputGrid(String filename) throws FileNotFoundException, IOException {
            PrintWriter printWriter = new PrintWriter(filename);
            printWriter.print(toString());
            printWriter.close();

    } // end of outputBoard()


    @Override
    public String toString() {

        StringBuilder grid = new StringBuilder();

        for (int[] row : layout) {
            for (int value : row) {
                if(value == EMPTY) {
                    grid.append(". ");
                }
                else {
                    grid.append(value).append(" ");
                }
            }
            grid.append("\n");
        }

        // placeholder
        return String.valueOf(grid).trim();
    } // end of toString()


    @Override
    public boolean validate() {
        for(int i = 0; i < rows; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            Set<Integer> sectorSet = new HashSet<>();

            for(int j = 0; j < cols; j++) {
                // Base case, check if the value is in the appropriate range
                if(layout[i][j] < minValue || layout[i][j] > maxValue) {
                    return false;
                }
                // check if the value layout[i][j] is in a Row Set
                if(layout[i][j] == EMPTY && !rowSet.add(layout[i][j])) {
                    return false;
                }
                // check if the value layout[i][j] is in a Column Set
                if(layout[j][i] == EMPTY && !colSet.add(layout[j][i])) {
                    return false;
                }
                int rowIndex = sectorSize * (i/sectorSize);
                int colIndex = sectorSize * (i % sectorSize);
                if(layout[rowIndex + j/sectorSize][colIndex + j % sectorSize] != EMPTY && !sectorSet.add(layout[rowIndex + j/sectorSize][colIndex + j % sectorSize])) {
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

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }


} // end of class StdSudokuGrid
