package org.drools.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DroolsEvent implements Comparable<DroolsEvent> {
    private String packageName;
    private String ruleName;
    private String factName;
    private EventType event;
    private Long occurredAt;
    private String eventDetail;

    @Override
    public int compareTo(DroolsEvent o) {
        return occurredAt.compareTo(o.occurredAt);
    }

    public String printEvent(){
        return Instant.ofEpochMilli(occurredAt) + ": " + eventDetail;
    }

    @SafeVarargs
    private static List<DroolsEvent> mergeEventsSequence(List<DroolsEvent>... events){
        List<DroolsEvent> result = new ArrayList<>();
        for(List<DroolsEvent> event: events){
            result.addAll(event);
        }
        Collections.sort(result);
        return result;
    }

    public static String getEventsSummary(List<DroolsEvent> events){
        StringBuilder sb = new StringBuilder();

        for(DroolsEvent event: events){
            sb.append(event.printEvent()).append("\n");
        }

        return sb.toString();
    }

    @SafeVarargs
    public static String getEventsSummary(List<DroolsEvent>... events){

        List<DroolsEvent> _events = mergeEventsSequence(events);

        StringBuilder sb = new StringBuilder();

        for(DroolsEvent event: _events){
            sb.append(event.printEvent()).append("\n");
        }

        return sb.toString();
    }

    public enum EventType{
        CONDITION_MATCH_STARTED,
        CONDITION_MATCH_COMPLETED,
        EXECUTION_STARTED,
        FACT_DATA_INSERTED,
        EXECUTION_COMPLETED,
        MATCH_CREATED,
        RULE_PICKED_TO_EXECUTE,
        MATCH_EXECUTED,
        MATCH_CANCELLED
    }

}
