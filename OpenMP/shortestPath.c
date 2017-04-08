#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>
#include <string.h>

//Define the number of nodes in the graph
#define N 1200

//Define minimum function that will be used later on to calcualte minimum values between two numbers
int getMin(a,b)
{
   return (((a) < (b)) ? (a) : (b));
}

//Initializing all distance value to 0
int dist_mat[N][N] = {0};

int main(int argc, char *argv[])
{

  int rowFlag = 1; //default row slicing
  int colFlag;
  if (argc == 2){
     if (strcmp(argv[1], "1") == 0){
	rowFlag = 1;
        printf("Row slicing is selected!\n");
     }
     else if (strcmp(argv[1], "2") == 0){
	colFlag = 1;
	rowFlag = 0;
        printf("Column slicing is selected!\n");
     }
     else{
	printf("No proper arguments supplied!\n");
        printf("Correct order: ./run <mode>\n");
        printf("mode = 1 or 2 for row or column slicing respectively \n");
        exit(1);
     }
  }
  else if (argc > 2) {
     printf("Too many arguments supplied!\n");
     printf("Correct order: ./run <mode>\n");
     printf("mode = 1 or 2 for row or column slicing respectively \n");
     exit(1);
  }
  else {
     printf("No arguments provided. Hence going for row slicing \n");
  }

  int nthreads;
  int i, j, k;
  
  //Initialize the graph with random distances
  for (i = 0; i < N; i++)
  {
    for (j = 0; j < N; j++)
    {
      if(i != j) {
        //Distances are generated to be between 0 and 50
        dist_mat[i][j] = rand() % 50;
      }
    }
  }
  
  //Define time variable to record start time for execution of program
  double start_time = omp_get_wtime();
  
  for (k = 0; k < N; k++)
  {   
    int * dm=dist_mat[k];
    //#pragma omp parallel for private(i, j) schedule(dynamic)
    for (i = 0; i < N; i++)
    {   
      int * ds=dist_mat[i];
      for (j = 0; j < N; j++)
      {
        ds[j]=getMin(ds[j],ds[k]+dm[j]);
      }
    }
  }
  double time = omp_get_wtime() - start_time;
  printf("Total time for sequential (in sec):%.2f\n", time);
  
  for(nthreads=1; nthreads <= 10; nthreads++) {
    omp_set_num_threads(nthreads);
    
    //Define time variable to record start time for execution of program
    double start_time = omp_get_wtime();
    
    #pragma omp parallel shared(dist_mat)
    for (k = 0; k < N; k++)
    {   
      int * dm=dist_mat[k];
      if (rowFlag == 1) {
	#pragma omp parallel for private(i, j) schedule(dynamic)
        for (i = 0; i < N; i++)
        {   
           int * ds=dist_mat[i];
           for (j = 0; j < N; j++)
           {
             ds[j]=getMin(ds[j],ds[k]+dm[j]);
           }
        }
      }
      if (colFlag == 1) {
        for (i = 0; i < N; i++)
        {   
           int * ds=dist_mat[i];
           #pragma omp parallel for private(j) schedule(dynamic)
           for (j = 0; j < N; j++)
           {
             ds[j]=getMin(ds[j],ds[k]+dm[j]);
           }
        }
      }
    }
    
    double time = omp_get_wtime() - start_time;
    printf("Total time for thread %d (in sec):%.2f\n", nthreads, time);
  }
  return 0;
  
}
