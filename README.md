# rmitSudoku

This was a final year assignment as part of my Master of Information Technology degree in which we were tasked with creating a sudoku solver.
The sudoku solver reads in an input file, that outlines the grid size (which can be of various sizes) and the starting number postiions. 
The solver will then run with parammeters that will solve the sudoku with one of the three algorithms. 

## Compile
To compile the files, run the following command from the root directory:

javac *.java grid/*.java solver/*.java

To run the program from cmd line:

java RmitSudokuTester [puzzleFileName] [gameType] [solverType] [visualisation] <outputFileName>

Note: outputFileName is optional.
