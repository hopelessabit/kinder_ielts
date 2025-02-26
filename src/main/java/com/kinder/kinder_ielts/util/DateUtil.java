package com.kinder.kinder_ielts.util;

import com.kinder.kinder_ielts.constant.DateOfWeek;

import java.util.List;

public class DateUtil {
    public static int[] convertToStep(List<DateOfWeek> dateOfWeeks){
        int[] steps = new int[dateOfWeeks.size()-1];
        for(int i=0; i < steps.length; i++){
            steps[i] = dateOfWeeks.get(i+1).getValue() - dateOfWeeks.get(i).getValue();
        }
        return steps;
    }
}
