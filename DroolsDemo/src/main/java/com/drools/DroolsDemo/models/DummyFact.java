package com.drools.DroolsDemo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.drools.models.ConditionClock;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DummyFact {

    private int id;
    private double amount;
    private double tax;
    private double totalAmount;
    private Status orderStatus;

    public void calculateTotalAmount(){
        this.totalAmount += amount + amount*tax/100.0;
    }

    public void modifyOrderStatus(int mov, double tov){
        this.orderStatus = tov < mov ? Status.MIN_PURCHASE_CRITERIA_FAILED : Status.ALLOWED;
    }



}