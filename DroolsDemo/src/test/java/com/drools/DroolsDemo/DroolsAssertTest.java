package com.drools.DroolsDemo;

import com.drools.DroolsDemo.models.Order;
import com.drools.DroolsDemo.models.DummyFact;
import org.drools.models.DroolsEvent;
import org.drools.models.TimedFact;
import org.drools.utils.PrettyPrintUtil;
import org.droolsassert.DroolsSession;
import org.droolsassert.TestRules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@DroolsSession(value = "classpath:order_copy.drl")
public class DroolsAssertTest {

    @RegisterExtension
    DroolsAssert drools = new DroolsAssert();


    @Test
    @TestRules()
    public void testRulesChronoListener() throws IOException {

        int i = 300;

        DummyFact dummyFact = new DummyFact();

        Order order = new Order();
        order.setAmount(i * 10);
        order.setTotalAmount(i * 10);
        drools.setGlobal("minimumOrderPurchaseValue", (i * (int) (Math.random() % 10)));

        TimedFact timedFact = new TimedFact(new HashMap<>(), new ArrayList<>());
        drools.setGlobal("clock", timedFact);

        drools.insertAndFire(order, dummyFact);

        System.out.println("Time to match condition for Order -- " + PrettyPrintUtil.prettyPrint(timedFact.getLHSExecutionTimeForFactByRules(Order.class)));
        System.out.println("Time to match condition for Second Fact -- " + PrettyPrintUtil.prettyPrint(timedFact.getLHSExecutionTimeForFactByRules(DummyFact.class)));
        System.out.println("Time to match condition -- " + PrettyPrintUtil.prettyPrint(timedFact.getTotalLHSExecutionTimeByRule()));
        System.out.println("Time to execute action -- " + PrettyPrintUtil.prettyPrint(drools.getPerformanceMonitoringListener().getRHSExecutionTimeSummary()));
        System.out.println("Total execution time for condition matching is " + timedFact.getTotalLHSExecutionTime() + " ms.");
        System.out.println("Total execution time for rules action is " + drools.getPerformanceMonitoringListener().getTotalRHSExecutionTime() + " ms.");
        System.out.println(DroolsEvent.getEventsSummary(timedFact.getLhsExecutionEvents(), drools.getPerformanceMonitoringListener().getRHSExecutionEvents()));

        drools.getSession().dispose();

    }


    @Test
    @TestRules()
    public void testRulesChronoListener2() throws IOException {

        DummyFact dummyFact = new DummyFact();

        int i = 1;
        Order order = new Order();
        order.setAmount(i * 10);
        order.setTotalAmount(i * 10);
        drools.setGlobal("minimumOrderPurchaseValue", (i * (int) (Math.random() % 10)));

        TimedFact timedFact = new TimedFact(new HashMap<>(), new ArrayList<>());
        drools.setGlobal("clock", timedFact);

        drools.insertAndFire(order, dummyFact);

        System.out.println("Time to match condition for Order -- " + PrettyPrintUtil.prettyPrint(timedFact.getLHSExecutionTimeForFactByRules(Order.class)));
        System.out.println("Time to match condition for Second Fact -- " + PrettyPrintUtil.prettyPrint(timedFact.getLHSExecutionTimeForFactByRules(DummyFact.class)));
        System.out.println("Time to match condition -- " + PrettyPrintUtil.prettyPrint(timedFact.getTotalLHSExecutionTimeByRule()));
        System.out.println("Time to execute action -- " + PrettyPrintUtil.prettyPrint(drools.getPerformanceMonitoringListener().getRHSExecutionTimeSummary()));
        System.out.println("Total execution time for condition matching is " + timedFact.getTotalLHSExecutionTime() + " ms.");
        System.out.println("Total execution time for rules action is " + drools.getPerformanceMonitoringListener().getTotalRHSExecutionTime() + " ms.");
        System.out.println(DroolsEvent.getEventsSummary(timedFact.getLhsExecutionEvents(), drools.getPerformanceMonitoringListener().getRHSExecutionEvents()));

        drools.getSession().dispose();
        drools.getSession().destroy();

    }


    public int randomFunction(int i) {
        return new Random().nextInt(i) * 10;
    }

}