package org.drools.models;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TimedFact {

    private HashMap<RuleFactCombination, Long> clock;
    private List<DroolsEvent> lhsExecutionEvents;

    public long getTotalLHSExecutionTime(){
        return clock.values().stream().reduce(0L, Long::sum);
    }

    public HashMap<String, Long> getTotalLHSExecutionTimeByRule(){
        HashMap<String, Long> result = new HashMap<>();

        for(Map.Entry<RuleFactCombination, Long> entry: clock.entrySet()){
            result.put(entry.getKey().getRuleName(), result.getOrDefault(entry.getKey().getRuleName(), 0L) + entry.getValue());
        }

        return result;
    }

    public HashMap<String, Long> getTotalLHSExecutionTimeByFact(){
        HashMap<String, Long> result = new HashMap<>();

        for(Map.Entry<RuleFactCombination, Long> entry: clock.entrySet()){
            result.put(entry.getKey().getFactName(), result.getOrDefault(entry.getKey().getFactName(), 0L) + entry.getValue());
        }

        return result;
    }

    public Map<String, Long> getLHSExecutionTimeForRuleByFacts(String rulename){
        Map<String, Long> result = new HashMap<>();
        for(Map.Entry<RuleFactCombination, Long> entry: clock.entrySet()){
            if(entry.getKey().getRuleName().equalsIgnoreCase(rulename)){
                result.put(entry.getKey().getFactName(), result.getOrDefault(entry.getKey().getFactName(), 0L) + entry.getValue());
            }
        }
        return result;
    }

    public Map<String, Long> getLHSExecutionTimeForFactByRules(Class<?> fact){
        Map<String, Long> result = new HashMap<>();
        for(Map.Entry<RuleFactCombination, Long> entry: clock.entrySet()){
            if(entry.getKey().getFactName().equalsIgnoreCase(fact.getSimpleName())){
                result.put(entry.getKey().getRuleName(), result.getOrDefault(entry.getKey().getRuleName(), 0L) + entry.getValue());
            }
        }
        return result;
    }

    public Long getLHSExecutionTimeForRule(String rulename){
        long result = 0L;
        for(Map.Entry<RuleFactCombination, Long> entry: clock.entrySet()){
            if(entry.getKey().getRuleName().equalsIgnoreCase(rulename)){
                result += entry.getValue();
            }
        }
        return result;
    }

    public Long getLHSExecutionTimeForFact(Class<?> fact){
        long result = 0L;
        for(Map.Entry<RuleFactCombination, Long> entry: clock.entrySet()){
            if(entry.getKey().getFactName().equalsIgnoreCase(fact.getSimpleName())){
                result += entry.getValue();
            }
        }
        return result;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @EqualsAndHashCode
    @ToString
    public static class RuleFactCombination{
        public String ruleName;
        public String factName;
    }

}
