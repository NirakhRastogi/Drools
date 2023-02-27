package com.drools.DroolsMetricLoggers;

import org.droolsassert.DroolsAssert;
import org.droolsassert.DroolsSession;
import org.droolsassert.TestRules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.util.Random;

@DroolsSession("classpath:/org/droolsassert/chrono.drl")
public class RulesChronoTest {

    @RegisterExtension
    public DroolsAssert drools = new DroolsAssert();

    @Test
    @TestRules(expected = { "sleep method", "more than 200" })
    public void testRulesChronoListener() {
        for (int i = 1; i <= 50; i++)
            drools.insertAndFire(randomFunction(i));
        drools.printPerformanceStatistic();
    }

    public int randomFunction(int i) {
        return new Random().nextInt(i) * 10;
    }
}