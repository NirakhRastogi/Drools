package org.drools.listeners;

import lombok.extern.slf4j.Slf4j;
import org.drools.models.DroolsEvent;
import org.kie.api.event.rule.*;

import java.util.*;

@Slf4j
public class PerformanceMonitoringListener extends DefaultAgendaEventListener implements RuleRuntimeEventListener {

    protected String testName;
    protected final List<DroolsEvent> events;
    protected final HashMap<String, Long> ruleExecutorClock = new HashMap<>();

    public PerformanceMonitoringListener() {
        this.events = Collections.synchronizedList(new ArrayList<>());
        this.testName = "";
    }

    @Override
    public void matchCreated(MatchCreatedEvent event) {
        String rule = event.getMatch().getRule().getPackageName() + "#" + event.getMatch().getRule().getName();
        this.events.add(DroolsEvent.builder()
                .packageName(event.getMatch().getRule().getPackageName())
                .ruleName(event.getMatch().getRule().getName())
                .occurredAt(System.currentTimeMillis())
                .event(DroolsEvent.EventType.MATCH_CREATED)
                .eventDetail("R.H.S - Match created for rule: `" + rule + "` and pushed to queue.")
                .build());
    }

    @Override
    public void matchCancelled(MatchCancelledEvent event) {
        String rule = event.getMatch().getRule().getPackageName() + "#" + event.getMatch().getRule().getName();
        this.events.add(DroolsEvent.builder()
                .packageName(event.getMatch().getRule().getPackageName())
                .ruleName(event.getMatch().getRule().getName())
                .occurredAt(System.currentTimeMillis())
                .event(DroolsEvent.EventType.MATCH_CANCELLED)
                .eventDetail("R.H.S - Match cancelled for rule: `" + rule + "` and removed to queue.")
                .build());
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        String rule = event.getMatch().getRule().getPackageName() + "#" + event.getMatch().getRule().getName();
        ruleExecutorClock.put(event.getMatch().getRule().getName(), System.currentTimeMillis());
        this.events.add(DroolsEvent.builder()
                .packageName(event.getMatch().getRule().getPackageName())
                .ruleName(event.getMatch().getRule().getName())
                .occurredAt(System.currentTimeMillis())
                .event(DroolsEvent.EventType.RULE_PICKED_TO_EXECUTE)
                .eventDetail("R.H.S - Rule: `" + rule + "` has been picked from queue to execute.")
                .build());
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        String rule = event.getMatch().getRule().getPackageName() + "#" + event.getMatch().getRule().getName();
        ruleExecutorClock.put(event.getMatch().getRule().getName(), System.currentTimeMillis() - ruleExecutorClock.getOrDefault(event.getMatch().getRule().getName(), 0L));
        this.events.add(DroolsEvent.builder()
                .packageName(event.getMatch().getRule().getPackageName())
                .ruleName(event.getMatch().getRule().getName())
                .occurredAt(System.currentTimeMillis())
                .event(DroolsEvent.EventType.MATCH_EXECUTED)
                .eventDetail("R.H.S - Rule: `" + rule + "` execution completed.")
                .build());
    }

    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        this.events.add(DroolsEvent.builder()
                .event(DroolsEvent.EventType.FACT_DATA_INSERTED)
                .occurredAt(System.currentTimeMillis())
                .factName(event.getObject().getClass().getSimpleName())
                        .eventDetail("FACT : Fact of type: `" + event.getObject().getClass().getSimpleName() + "` inserted to memory.")
                .build());
    }

    public List<DroolsEvent> getRHSExecutionEvents() {
        return this.events;
    }

    public Map<String, Long> getRHSExecutionTimeSummary(){
        return this.ruleExecutorClock;
    }

    public Long getTotalRHSExecutionTime(){
        return this.ruleExecutorClock.values().stream().reduce(0L, Long::sum);
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        /*Left blank intentionally*/
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        /*Left blank intentionally*/
    }
}
