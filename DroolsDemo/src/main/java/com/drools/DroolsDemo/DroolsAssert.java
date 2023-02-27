package com.drools.DroolsDemo;

import org.drools.listeners.PerformanceMonitoringListener;
import org.droolsassert.DroolsSession;
import org.droolsassert.TestRules;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;

public class DroolsAssert extends org.droolsassert.DroolsAssert {

    private PerformanceMonitoringListener performanceMonitoringListener;


    @Override
    public void init(DroolsSession droolsSessionMeta, TestRules testRulesMeta) {
        super.init(droolsSessionMeta, testRulesMeta);
        this.performanceMonitoringListener = new PerformanceMonitoringListener();
        this.session.addEventListener((AgendaEventListener) performanceMonitoringListener);
        this.session.addEventListener((RuleRuntimeEventListener) performanceMonitoringListener);
    }

    public PerformanceMonitoringListener getPerformanceMonitoringListener() {
        return performanceMonitoringListener;
    }

}
