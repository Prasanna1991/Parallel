The code implements Floyd-Warshall algorithm for finding the shortest paths in weighted graph with positive or negative weight. 

Using OpenMp, the code is implemented for demonstrating thread level parallelism. 

To run the code:
1. gcc -fopenmp shortestPath.c -o run
2. ./run <mode>
3. mode represents the flag for row or column slicing. 1 for row and 2 for column slicing.


Dependencies:
1. GCC compiler with OpenMP
