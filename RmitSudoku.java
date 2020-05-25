/*
 * This is the main java file for assignment 2 of RMIT Algorithms & Analysis,
 * 2020 semester 1.
 *
 * The assignment is about implementing and exploring algorithms and
 * data structures for Sudoku and Killer Sudoku.
 *
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

import java.io.*;

import grid.*;
import solver.*;


/**
 * Main class for the Sudoku assignment.  Can run both Sudoku and Killer Sudoku
 * solvers.
 *
 * It implements the basic framework for the assignment code.
 * Generally no need to modify this, there are many abstract and non-abstract
 * classes to customise your implementation.
 */
public class RmitSudoku
{
    /**
     * Name of class, used in error messages.
     */
    protected static final String progName = "RmitSudoku";


    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // read command line arguments
	    if (args.length < 4) {
	        System.err.println("Incorrect number of arguments.\n");
	        usage(progName);
	    }

	    // grid filename
	    String initGridFilename = args[0];
        // type of game
        String gameType = args[1];
        // type of solver
        String solverType = args[2];
        // whether to visualise
        boolean bVisualise = false;
        if (args[3].compareTo("y") == 0) {
            bVisualise = true;
        }

        // optional argument.  If specified, then we also output solved grid
        // to file.
        String solvedGridOutFilename = null;
        if (args.length > 4) {
            solvedGridOutFilename = args[4];
        }

        //
        // Construct solver and load grid.
        //

        SudokuSolver solver = null;
        SudokuGrid grid = null;
        // used to return constructed solver and grid
        SolverGridPair pair = new SolverGridPair();

        // which type of game?
        // construct corresponding grid and solver
        switch (gameType) {
            case "sudoku":
                initSudokuSolver(solverType, pair);
                break;
            case "killer":
                initKillerSolver(solverType, pair);
                break;
            default:
                System.err.println("Unknown game type specified.\n");
                usage(progName);
        }

        // retrieve constructed grid and pair
        grid = pair.grid;
        solver = pair.solver;

        assert(grid != null);
        assert(solver != null);

        // start timer
        long startTime = System.nanoTime();

        // load grid
        try {
            grid.initGrid(initGridFilename);

            // visualise initial grid
            if (bVisualise) {
                System.out.println("Initial grid:");
                // this will call grid.toString()
                System.out.println(grid);
            }

            // run solver
            boolean bSolvedSuccess = solver.solve(grid);

            // stop timer
            long endTime = System.nanoTime();

            if (bSolvedSuccess) {
                System.out.println("Solution found!\n");
            }
            else {
                System.out.println("No solution found!\n");
            }

            // visualise solved grid
            if (bVisualise) {
                System.out.println("Solved grid:");
                // this will call grid.toString()
                System.out.println(grid);
            }

            // display time taken
            System.out.println("time taken = "
                + ((double)(endTime - startTime)) / Math.pow(10, 9) + " sec.\n");


            // Check and validate solution
            if (grid.validate()) {
                System.out.println("Valid solution.");
            }
            else {
                System.out.println("Invalid solution.");
            }


            // output solved grid to file
            if (solvedGridOutFilename != null) {
                grid.outputGrid(solvedGridOutFilename);
            }

        } // end of try-block
        catch (FileNotFoundException e) {
            System.err.println("File not found. " + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("IOExeption occurred. " + e.getMessage());
        }
    } // end of main()

    /* ************************************************* */

    /**
     * Print help/usage message.
     *
     * @param progName Name of the program.
     */
    public static void usage(String progName) {

        System.err.println(progName + ": [grid fileName] [game type] [solver type] [visualisation] <output filename>");
        System.err.println("[game type] = {sudoku | killer}");
        System.err.println("[solver type] (for Sudoku) = {backtracking | algorx | dancing}");
        System.err.println("[solver type] (for Killer Sudoku) = {backtracking | advanced}");
        System.err.println("<visualisation> = <y | n>");

	    System.exit(1);
    } // end of usage()


    /**
     * Contruct appropriate grid and solver for (standard) Sudoku.
     *
     * @param solverType Type of solver we want to use and construct.
     * @param pair Used to return the constructed grid and solver (return by
     *               reference).
     */
    private static void initSudokuSolver(String solverType, SolverGridPair pair) {
        SudokuGrid grid = null;
        SudokuSolver solver = null;

        // check with solver (and grid) to construct
        // currently all grids are the same for all solvers, but there are
        // possibility this might not be in the future iterations.
        switch(solverType) {
            case "backtracking":
                grid = new StdSudokuGrid();
                solver = new BackTrackingSolver();
                break;
            case "algorx":
                grid = new StdSudokuGrid();
                solver = new AlgorXSolver();
                break;
            case "dancing":
                grid = new StdSudokuGrid();
                solver = new DancingLinksSolver();
                break;
            default:
                System.err.println("Unknown solver type specified.\n");
                usage(progName);
        }

        pair.grid = grid;
        pair.solver = solver;
    } // end of initSudokuSolver()


    /**
     * Contruct appropriate grid and solver for Killer Sudoku.
     *
     * @param solverType Type of solver we want to use and construct.
     * @param pair Used to return the constructed grid and solver (return by
     *               reference).
     */
    public static void initKillerSolver(String solverType, SolverGridPair pair) {
        SudokuGrid grid = null;
        SudokuSolver solver = null;

        // check with solver (and grid) to construct
        // currently all grids all the same for all solvers, but there are
        // possibility this might not be in the future iterations.
        switch(solverType) {
            case "backtracking":
                grid = new KillerSudokuGrid();
                solver = new KillerBackTrackingSolver();
                break;
            case "advanced":
                grid = new KillerSudokuGrid();
                solver = new KillerAdvancedSolver();
                break;
            default:
                System.err.println("Unknown solver type specified.\n");
                usage(progName);
        }

        pair.grid = grid;
        pair.solver = solver;
    } // end of initKillerSolver()


    /**
     * Inner class used to return a pair of values.
     * Note on core teaching servers javafx.util.Pair isn't available.
     */
    protected static class SolverGridPair {
        public SudokuGrid grid;
        public SudokuSolver solver;
    } // end of class SolverGridPair

} // end of class RmitSudoku
