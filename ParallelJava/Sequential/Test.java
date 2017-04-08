/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.Math;

/**
 *
 * @author prasanna
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        int[] array_ = {3,5,7,13,19,29,31,37,39,41};
        int total_quant = 40;

        long startTime = System.currentTimeMillis();
        WaterJug_pair w = new WaterJug_pair(array_, total_quant);
        w.calcGoal();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time = " + totalTime);

    }

}

