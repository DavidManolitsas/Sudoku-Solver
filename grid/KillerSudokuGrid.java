/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * Class implementing the grid for Killer Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task E and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {
    // TODO: Add your own attributes
    private int[][] layout;
    private int size;
    private int sectorSize;
    private int[] values;
    private int minValue;
    private int maxValue;
    private int cageCount;

    public KillerSudokuGrid() {
        super();

        // TODO: any necessary initialisation at the constructor
    } // end of KillerSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename) throws FileNotFoundException, IOException {
        // TODO
        File inputFile = new File(filename);
        Scanner read = new Scanner(inputFile);
        int lineCount = 0;

        while (read.hasNextLine()) {
            lineCount++;

            if (lineCount == 1) {
                // initialize the size of the grid
                size = Integer.parseInt(read.nextLine());
                sectorSize = (int) Math.sqrt(size);
                layout = new int[size][size];
            }
            else if(lineCount == 2) {
                // assign the smallest and largest value of the grid
                String[] line = read.nextLine().split(" ");
                initValues(line);
            }
            else if (lineCount == 3) {
                cageCount = Integer.parseInt(read.nextLine());
            }
            else if (lineCount > 3) {
                KillerCage cage = new KillerCage();

                String[] line = read.nextLine().split(" ");
                int cageTotal = Integer.parseInt(line[0]);
                cage.setTotal(cageTotal);

                for (int i = 1; i < line.length; i++) {
                    String[] rowCol = line[i].split(",");
                    int row = Integer.parseInt(rowCol[0]);
                    int col = Integer.parseInt(rowCol[1]);
                    int[] cagePos = {row, col};

                    cage.getPositions().add(cagePos);
                }
                System.out.println(cage.toString());
            }
        }

        System.out.println("\nSize: " + size + " Box Size: " + sectorSize + " Min: " + minValue + " Max: " + maxValue + " Cages: " + cageCount);

    } // end of initBoard()

    private void initValues(String[] line) {
        values = new int[size];
        for (int i = 0; i < line.length; i++) {
            values[i] = Integer.parseInt(line[i]);
        }

        // find max value
        maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
            }
        }

        // find min value
        minValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < minValue) {
                minValue = values[i];
            }
        }

        // TODO: testing, delete before submission
        System.out.print("Values: ");
        for (int val : values) {
            System.out.print(val + " ");
        }
        System.out.println();

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
        // TODO

        // placeholder
        return false;
    } // end of validate()


    public void setCell(int row, int col, int value) {
        layout[row][col] = value;
    }


    public int getCell(int row, int col) {
        return layout[row][col];
    }

    public int[][] getLayout() {
        return layout;
    }

    public void setLayout(int[][] layout) {
        this.layout = layout;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSectorSize() {
        return sectorSize;
    }

    public void setSectorSize(int sectorSize) {
        this.sectorSize = sectorSize;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getCageCount() {
        return cageCount;
    }

    public void setCageCount(int cageCount) {
        this.cageCount = cageCount;
    }
} // end of class KillerSudokuGrid
