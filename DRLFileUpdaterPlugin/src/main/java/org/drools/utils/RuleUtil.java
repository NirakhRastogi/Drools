package org.drools.utils;

import org.drools.models.DroolsEvent;
import org.drools.models.TimedFact;

import java.util.Map;

public class RuleUtil {
    public static boolean timedInput(String ruleName, String factName, TimedFact timedFact ) {
        timedFact.getClock().put(TimedFact.RuleFactCombination.builder()
                        .factName(factName)
                        .ruleName(ruleName)
                .build(), System.currentTimeMillis());
        timedFact.getLhsExecutionEvents().add(DroolsEvent.builder().occurredAt(System.currentTimeMillis()).event(DroolsEvent.EventType.CONDITION_MATCH_STARTED).factName(factName).ruleName(ruleName).eventDetail("L.H.S : Condition Matching started for `" + ruleName + "` and `" + factName + "`").build());
        return true;
    }

    public static boolean timedExit(String ruleName, String factName, TimedFact timedFact ) {
        timedFact.getClock().put(TimedFact.RuleFactCombination.builder()
                .factName(factName)
                .ruleName(ruleName)
                .build(), System.currentTimeMillis() - (Long)timedFact.getClock().get(TimedFact.RuleFactCombination.builder()
                .factName(factName)
                .ruleName(ruleName)
                .build()));
        timedFact.getLhsExecutionEvents().add(DroolsEvent.builder().occurredAt(System.currentTimeMillis()).event(DroolsEvent.EventType.CONDITION_MATCH_COMPLETED).factName(factName).ruleName(ruleName).eventDetail("L.H.S : Condition Matching Completed for `" + ruleName + "` and `" + factName + "`").build());
        return true;
    }

    public static Map<TimedFact.RuleFactCombination, Long> getClock(TimedFact timedFact){
        return timedFact.getClock();
    }

}
