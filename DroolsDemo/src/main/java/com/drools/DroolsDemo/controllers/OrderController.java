package com.drools.DroolsDemo.controllers;

import com.drools.DroolsDemo.models.DummyFact;
import com.drools.DroolsDemo.models.Order;
import lombok.RequiredArgsConstructor;
import org.drools.listeners.PerformanceMonitoringListener;
import org.drools.models.DroolsEvent;
import org.drools.models.TimedFact;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final KieContainer kieContainer;

    PerformanceMonitoringListener performanceMonitoringListener = new PerformanceMonitoringListener();

    @PostMapping("/order")
    public Order submitOrder(@RequestBody Order order){

        KieSession kieSession = kieContainer.newKieSession();

        /* --- Extra added code */
        TimedFact timedFact = new TimedFact(new HashMap<>(), new ArrayList<>());

        kieSession.addEventListener((AgendaEventListener) performanceMonitoringListener);
        kieSession.addEventListener((RuleRuntimeEventListener) performanceMonitoringListener);

        kieSession.setGlobal("clock", timedFact);
        /*-----------------*/
        kieSession.setGlobal("minimumOrderPurchaseValue", 3);




        kieSession.insert(timedFact);
        kieSession.insert(order);
        kieSession.insert(new DummyFact());
        kieSession.fireAllRules();
        kieSession.dispose();

        // -- Metrics to print
        System.out.println("Time to match condition - " + timedFact.getTotalLHSExecutionTime());
        System.out.println("Time to match RHS - " + performanceMonitoringListener.getTotalRHSExecutionTime());
        System.out.println("Rule wise division for condition - " + timedFact.getTotalLHSExecutionTimeByRule());
        System.out.println("Rule wise division for action - " + performanceMonitoringListener.getRHSExecutionTimeSummary());
        System.out.println("Process \n " + DroolsEvent.getEventsSummary(timedFact.getLhsExecutionEvents(), performanceMonitoringListener.getRHSExecutionEvents()));

        return order;
    }

}