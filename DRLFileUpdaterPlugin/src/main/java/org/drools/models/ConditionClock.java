package org.drools.models;

import lombok.Data;
import lombok.ToString;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;


@ToString
@Data
public class ConditionClock {
    public HashMap<String, Long> conditionEvaluatorClock = new HashMap<>();
    public List<DroolsEvent> sequenceMap = new ArrayList<>();

    private static ObjectMapper mapper = new ObjectMapper();

    public boolean timedInput(String ruleName, String factName) {
        conditionEvaluatorClock.put(ruleName, System.currentTimeMillis());
        sequenceMap.add(DroolsEvent.builder()
                        .occurredAt(System.currentTimeMillis())
                        .event(DroolsEvent.EventType.CONDITION_MATCH_STARTED)
                        .factName(factName)
                        .ruleName(ruleName)
                        .eventDetail("L.H.S : Condition Matching started for `" + ruleName + "` and `" + factName + "`")
                .build());
        return true;
    }

    public boolean timedExit(String ruleName, String factName) {
        conditionEvaluatorClock.put(ruleName,System.currentTimeMillis() -  conditionEvaluatorClock.get(ruleName));
        sequenceMap.add(DroolsEvent.builder()
                .occurredAt(System.currentTimeMillis())
                .event(DroolsEvent.EventType.CONDITION_MATCH_COMPLETED)
                .factName(factName)
                .ruleName(ruleName)
                .eventDetail("L.H.S : Condition Matching Completed for `" + ruleName + "` and `" + factName + "`")
                .build());
        return true;
    }

    public static List<DroolsEvent> getLHSExecutionEvents(ConditionClock... facts){
        List<DroolsEvent> result = new ArrayList<>();
        for(ConditionClock fact: facts){
            result.addAll(fact.getSequenceMap());
        }
        Collections.sort(result);
        return result;
    }

    public static Map<String, Long> getLHSExecutionTimeSummary(ConditionClock... facts){
        Map<String, Long> result = new HashMap<>();
        for(ConditionClock fact: facts){
            for(Map.Entry<String, Long> entry: fact.getConditionEvaluatorClock().entrySet()){
                result.put(entry.getKey(), result.getOrDefault(entry.getKey(), 0L) + entry.getValue());
            }
        }

        return result;
    }

    public static Long getTotalLHSExecutionTime(ConditionClock... facts){
        return getTotalLHSExecutionTime(getLHSExecutionTimeSummary(facts));
    }

    private static Long getTotalLHSExecutionTime(Map<String, Long> timeSummary){
        return timeSummary.values().stream().reduce(0L, Long::sum);
    }

}
