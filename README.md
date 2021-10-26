# Sudoku Solver

The Sudoku Solver utilises various algorithms to solve both a traditional 
[sudoku](https://sudoku.com/how-to-play/sudoku-rules-for-complete-beginners/) 
and a [killer sudoku](https://killersudoku.com/pages/how-to-play-killer-sudoku) 
puzzle. The algorithms used to solve both these types of sudoku's include the
[Backtracking Algorithm](https://www.geeksforgeeks.org/backtracking-algorithms/),
[Knuth's Algorithm X](https://www.geeksforgeeks.org/exact-cover-problem-algorithm-x-set-1/),
and the [Knuth's Dancing Link (DLX) Algorithm](https://www.geeksforgeeks.org/exact-cover-problem-algorithm-x-set-2-implementation-dlx/?ref=lbp).

## About the Algorithms

### Backtracking Algorithm

The Backtracking Algorithm is an algorithmic-technique for solving problems 
recursively by trying to build a solution incrementally, one piece at a time, 
removing those solutions that fail to satisfy the constraints of the problem at
any point of time.

### Knuth's Algorithm X

The exact cover problem and algorithm x are synonymous. It is described as a 
given a collection S of subsets of set X, an exact cover is the subset S* of S
such that each element of X is contained is exactly one subset of S*.

It should satisfy following two conditions:
* The Intersection of any two subsets in S* should be empty. That is, each 
  element of X should be contained in at most one subset of S*
* Union of all subsets in S* is X. That means union should contain all the 
  elements in set X, so we can say that S* covers X.


### Knuth's Dancing Link Algorithm

The Dancing Link algorithm is an iteration on Knuth's Algorithm X, Dancing link
technique relies on the idea of doubly circular linked list. Each row of the 
matrix is thus a circular linked list linked to each other with left and right 
pointers and each column of the matrix will also be circular linked list linked
to each above with up and down pointer.


## Compile

To compile the files, run the following command line:

```bash
javac src/main/java/com/**/*.java
```

Alternatively, if your shell does not support `**`: 

```bash
cd src/main/java/com/manolitsas/david/
javac *.java grid/*.java solver/*.java
```

## Run

To run the program from command line:

```bash
java -cp ./src/main/java/ com.manolitsas.david.RmitSudoku [file name] [game type] [solver type] [visualisation] [outputFileName]
```

Standard Sudoku Backtracking example:
```bash
java -cp ./src/main/java/ com.manolitsas.david.RmitSudoku input/easy-std-44-01.in sudoku backtracking Y output/output.txt
```

How to Run?

* `file name` select a file from [input](input) with `.in` extension. The file names are structured 
  as `[difficulty]-[type]-[size]-[id].in` e.g. easy-std-44-01.in
* `game type` select between standard or killer sudoku  
    * `sudoku` = standard sudoku  
    * `killer` = killer sudoku
* `solver type` select which algorithm to use
    * Standard Sudoku
        * `backtracking` = Backtracking Algorithm
        * `algorx` = Algorithm X
        * `dancing` = Dancing Link Algorithm
    * Killer Sudoku
        * `backtracking` = Backtracking Algorithm
        * `advanced` = An optimised Backtracking Algorithm solution
* `visualisation` whether to print the solution or not use `Y` yes or `N` no
* `outputFile` (optional) set an optional output file e.g. `output/output.txt`

