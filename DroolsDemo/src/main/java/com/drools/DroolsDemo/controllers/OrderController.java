package com.drools.DroolsDemo.controllers;

import com.drools.DroolsDemo.models.Order;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final KieContainer kieContainer;

    @PostMapping("/order")
    public Order submitOrder(@RequestBody Order order){
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("minimumOrderPurchaseValue", 3);
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();
        return order;
    }

}