/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;


/**
 * Abstract class of a Sudoku solver.  Defines the interface for all solvers.
 */
public abstract class SudokuSolver
{

    /**
     * Solves the input grid.  Different solvers should override this to implement
     * different solving strategies.  The solver will fill the grid.
     *
     * @param grid Input grid to solve.  The solver will write the solution to grid.
     *
     * @return True if successfully solved the grid; otherwise false if there are no
     *          solutions found.
     */
    public abstract boolean solve(SudokuGrid grid);

} // end of class SudokuSolver
