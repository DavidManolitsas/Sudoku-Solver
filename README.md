# rmitSudoku

This was a final year assignment as part of my Master of Information Technology degree in which we were tasked with creating a sudoku solver.
The sudoku solver reads in an input file, that outlines the grid size (which can be of various sizes) and the starting number postiions. 
The solver will then run with parammeters that will solve the sudoku with one of the three algorithms. 

## Compile
To compile the files, run the following command from the root directory:

javac *.java grid/*.java solver/*.java

To run the program from cmd line:

java RmitSudokuTester [puzzleFileName] [gameType] [solverType] [visualisation] <outputFileName>

**Where:**
puzzleFileName: name of file containing the input puzzle/grid to solve.
game type: type of sudoku game, one of {sudoku, killer}.
solverType: Type of solver to use, depends on the game type 
    - Standard Sudoku: (backtracking, algorx or dancing).
    - Killer Sudoku: (backtracking or advanced)
visualisation: whether to output grid before and another after solving, one of [Y,N]
output fileName: (optional) If specified, the solved grid will be outputted to this file.

