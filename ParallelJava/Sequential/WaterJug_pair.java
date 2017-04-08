/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.*;

/**
 *
 * @author prasanna
 */
public class WaterJug_pair {

    int magicNum;
    int[] bottleCap;
    int N;
    int[] bottleState;
    int goal;
    List<int[]> visitedNodes = new ArrayList<int[]>();
    List<int[]> currentState = new ArrayList<int[]>();
    List<int[]> nextState = new ArrayList<int[]>();

    public WaterJug_pair(int[] bottleCap_, int quantity) {
        System.out.println("inside the constructor");
        bottleCap = bottleCap_;
        N = bottleCap.length;
        bottleState = new int[N];
        goal = quantity;
        currentState.add(new int[N]);
        visitedNodes.add(new int[N]);
        magicNum = 2 * N + N * (N - 1);
    }

    void calcGoal() {
        int fin = 0;
        int layer = 1;
        while (fin != 1) {
            System.out.println("***********");
            //nextState = getNextStates(currentState);
            for (int i = 0; i < currentState.size(); i++) {
                for (int j = 0; j < N; j++) {
                    if (currentState.get(i)[j] == goal) {
                        fin = 1;
                        System.exit(0);
                    }
                    if (currentState.get(i)[j] == 0) {
                        fill(currentState, i, j);
                    }
                    if (currentState.get(i)[j] > 0) {
                        empty(currentState, i, j);
                    }
                }
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k < N; k++) {
                        if ((j != k) && (this.currentState.get(i)[j] > 0) && (this.currentState.get(i)[k] != this.bottleCap[k])) {
                            transfer(currentState, i, j, k);
                        }
                    }
                }
            }

            for(int [] temp2:nextState){
                for (int n = 0; n < temp2.length; n++){
//                    System.out.print(temp2[n] + ",");
                    if (temp2[n] == goal){
                        System.out.print("Goal found");                 
                        fin = 1;
                    }
                }
//                System.out.println("");
            }
            currentState.clear(); //clearing all the currentState
            
            List<int []> tempList = new ArrayList<>();
            
            for (int[] temp:nextState){
                tempList.add(temp.clone());
            }
            for (int [] temp1:tempList){
                currentState.add(temp1);
            }
            nextState.clear();
            layer += 1;
        }
    }

    void fill(List<int[]> currentList, int i, int j) {
        List<int[]> tempList = new ArrayList<>();

        for (int[] temp:currentList){
            tempList.add(temp.clone());
        }
        int [] fillArray  = tempList.get(i);
        fillArray[j] = bottleCap[j];
        
        //check and add
        int flag = 0;
        for (int[] temp1:visitedNodes){
            if (Arrays.equals(fillArray, temp1)){
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            nextState.add(fillArray);
            visitedNodes.add(fillArray);
        }
    }

    void empty(List<int[]> currentList, int i, int j) {
        List<int[]> tempList = new ArrayList<>();
        for (int[] temp : currentList) {
            tempList.add(temp.clone());
        }
        int [] fillArray  = tempList.get(i);
        fillArray[j] = 0;
        
        //check and add
        int flag = 0;
        for (int[] temp1:visitedNodes){
            if (Arrays.equals(fillArray, temp1)){
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            nextState.add(fillArray);
            visitedNodes.add(fillArray);
        }
    }

    void transfer(List<int[]> currentList, int i, int from_id, int to_id) {
        List<int[]> tempList = new ArrayList<>();
        for (int[] temp : currentList) {
            tempList.add(temp.clone());
        }   
        
        int [] fillArray  = tempList.get(i);
        int fillVal = bottleCap[to_id] - fillArray[to_id];
        if (fillArray[from_id] > fillVal){
            fillArray[from_id] = fillArray[from_id] - fillVal;
            fillArray[to_id] = bottleCap[to_id];
        } else if (fillArray[from_id] <= fillVal){
            fillArray[to_id] = fillArray[to_id] + fillArray[from_id];
            fillArray[from_id] = 0;
        }
        
        //check and add
        int flag = 0;
        for (int[] temp1:visitedNodes){
            if (Arrays.equals(fillArray, temp1)){
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            nextState.add(fillArray);
            visitedNodes.add(fillArray);
        }
    }
}

