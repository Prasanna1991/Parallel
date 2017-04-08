This is the demonstration of running WaterJug Problem in parallel version using ParallelJava2. This is cluster level parallelism.

The documentation of parallel java can be found [here] (https://www.cs.rit.edu/~ark/pj2.shtml).

To compile and run:

A. Seq version
1. javac *.java
2. java Test

B. Parallel version
1. export CLASSPATH=.:<location of Pj2.jar>/pj2.jar
2. javac WaterJugParallel.java
3. jar cf test.jar *.class
4. java pj2 jar=test.jar WaterJugParallel

Dependiencies:
pj2.jar
