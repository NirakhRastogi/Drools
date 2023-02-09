package com.drools.DroolsITRCalculator.controller;

import com.drools.DroolsITRCalculator.models.Tax;
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

    @PostMapping("/income")
    public Tax submitOrder(@RequestBody Tax tax){
        tax.setTotalIncome(Math.max(tax.getTotalIncome()-50000,0));
        tax.setPendingIncome(tax.getTotalIncome());
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("taxExceptionAmount", 700000.0);
        kieSession.insert(tax);
        kieSession.fireAllRules();
        kieSession.dispose();
        return tax;
    }

}