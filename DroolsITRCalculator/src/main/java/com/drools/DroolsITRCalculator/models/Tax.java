package com.drools.DroolsITRCalculator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Tax {

    private double totalIncome;
    private double pendingIncome;
    private double tax;

    public void addTax(double tax){
        this.tax += tax;
    }

}