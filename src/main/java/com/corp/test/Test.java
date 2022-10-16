package com.corp.test;

import java.math.BigDecimal;

/**
 * Created by mycomputer on 2022-08-17.
 */
public class Test {

    public int solution(int a, int b) {
        // 在这⾥写代码
//        BigDecimal bd1 = new BigDecimal(a);
//        BigDecimal bd2 = new BigDecimal(b);
        BigDecimal bd1 = new BigDecimal(Double.toString(a));
        BigDecimal bd2 = new BigDecimal(Double.toString(a));
        BigDecimal bd = bd1.add(bd2);
        bd1.subtract(bd2);
        bd1.multiply(bd2);
        bd1.divide(bd2);
        return  bd.intValue();
    }

    public static void main(String[] args){
        Test t = new Test();
        int i = t.solution(1,2);
        System.out.println(i);
    }

}
