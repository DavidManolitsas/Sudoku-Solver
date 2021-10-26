
package com.manolitsas.david.grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 *
 * Class implementing the grid for Killer Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task E and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {

    private int[][] layout;
    private final int EMPTY = 0;
    private List<KillerCage> cages;
    private int size;
    private int sectorSize;
    private int[] values;
//    private int minValue;
//    private int maxValue;
    private int cageCount;

    public KillerSudokuGrid() {
        super();

    } // end of KillerSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename) throws FileNotFoundException, IOException {
        File inputFile = new File(filename);
        Scanner read = new Scanner(inputFile);
        int lineCount = 0;
        cages = new ArrayList<KillerCage>();

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

                // Add all the positions to the cage
                for (int i = 1; i < line.length; i++) {
                    String[] rowCol = line[i].split(",");
                    int row = Integer.parseInt(rowCol[0]);
                    int col = Integer.parseInt(rowCol[1]);
                    int[] cagePos = {row, col};

                    cage.getPositions().add(cagePos);
                }
                cages.add(cage);
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
            for (int j = i+1; j < length; j++) {
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
        // Validate each cage adds up to the target sum
        for (KillerCage cage : cages) {
            if(!validateCage(cage)) {
                return false;
            }
        }

        // Validate the sudoku grid
        for(int i = 0; i < size; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();
            Set<Integer> sectorSet = new HashSet<>();

            for(int j = 0; j < size; j++) {
                // Base case, check if the value is in the appropriate range
                if(layout[i][j] < values[0] || layout[i][j] > values[size - 1]) {
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
                if(layout[rowIndex + j/sectorSize][colIndex + j % sectorSize] != EMPTY && !sectorSet.add(layout[rowIndex + j/sectorSize][colIndex + j % sectorSize])) {
                    return false;
                }

            }
        }

        return true;

    } // end of validate()

    /**
     * Check if the sum of all values in the cage add to the target Total of the cage
     * @param cage the cage that is being validated
     * @return true if the values in the cage equal the cage total, otherwise return false
     */
    public boolean validateCage(KillerCage cage) {
        int sum = 0;
        for(int[] rowCol : cage.getPositions()) {
            int value = layout[rowCol[0]][rowCol[1]];
            sum += value;
        }

        return sum == cage.getTotal();
    }


    public int[][] getLayout() {
        return layout;
    }

    public List<KillerCage> getCages() {
        return cages;
    }

    public int getSize() {
        return size;
    }

    public int[] getValues() {
        return values;
    }

} // end of class KillerSudokuGrid
