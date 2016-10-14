package com.github.sevntu.checkstyle.domain;

import com.github.sevntu.checkstyle.analysis.MethodCallDependenciesModuleTestSupport;
import com.github.sevntu.checkstyle.module.MethodCallDependencyCheckstyleModule;
import com.github.sevntu.checkstyle.ordering.Method;
import com.github.sevntu.checkstyle.ordering.MethodOrder;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MethodOrderTest extends MethodCallDependenciesModuleTestSupport {

    @Test
    public void testDependencies() throws Exception {
        final Configuration dc = createCheckConfig(MethodCallDependencyCheckstyleModule.class);
        final MethodOrder ord = invokeCheckAndGetOrdering(dc, "InputDependencies.java");
        final Method methodB = ord.getMethodByInitialIndex(1);
        final List<Method> dependencies = ord.getMethodDependenciesInAppearanceOrder(methodB);
        assertTrue(dependencies.size() == 1);
        assertTrue(dependencies.get(0).getSignature().equals("c()"));
        final List<Method> dependants = ord.getMethodDependants(methodB);
        assertTrue(dependants.size() == 1);
        assertTrue(dependants.get(0).getSignature().equals("a()"));
        final Method methodD = ord.getMethodByInitialIndex(3);
        assertFalse(ord.hasMethodDependants(methodD));
        assertFalse(ord.hasMethodDependencies(methodD));
        final Method methodA = ord.getMethodByInitialIndex(0);
        assertTrue(ord.isMethodDependsOn(methodA, methodB));
        assertFalse(ord.isMethodDependsOn(methodA, methodD));
    }

    @Test
    public void testTotalSumOfMethodDistances1() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesDistance1.java");
        assertEquals(12, ds.getTotalSumOfMethodDistances());
    }

    @Test
    public void testTotalSumOfMethodDistances2() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesDistance2.java");
        assertEquals(3, ds.getTotalSumOfMethodDistances());
    }

    @Test
    public void testDeclarationBeforeUsageCases() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesDeclarationBeforeUsage.java");
        assertEquals(2, ds.getDeclarationBeforeUsageCases());
    }

    @Test
    public void testOverloadSplit1() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesOverloadSplit1.java");
        assertEquals(5, ds.getOverloadGroupsSplitCases());
    }

    @Test
    public void testOverloadSplit2() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesOverloadSplit2.java");
        assertEquals(14, ds.getOverloadGroupsSplitCases());
    }

    @Test
    public void testOverrideSplit1() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesOverrideSplit1.java");
        assertEquals(3, ds.getOverrideGroupSplitCases());
    }

    @Test
    public void testOverrideSplit2() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesOverrideSplit2.java");
        assertEquals(0, ds.getOverrideGroupSplitCases());
    }

    @Test
    public void testOverrideSplit3() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesOverrideSplit3.java");
        assertEquals(0, ds.getOverrideGroupSplitCases());
    }

    @Test
    public void testRelativeOrderInconsistency() throws Exception {
        final Map<String, Integer> expected = new TreeMap<>();
        expected.put("InputDependenciesOrderInconsistency1.java", 0);
        expected.put("InputDependenciesOrderInconsistency2.java", 1);
        expected.put("InputDependenciesOrderInconsistency3.java", 1);
        expected.put("InputDependenciesOrderInconsistency4.java", 1);
        expected.put("InputDependenciesOrderInconsistency5.java", 1);
        expected.put("InputDependenciesOrderInconsistency6.java", 0);
        for(final Map.Entry<String,Integer> e : expected.entrySet()) {
            final String msg = String.format("Incorrect result for input \"%s\"", e.getKey());
            assertEquals(msg, e.getValue().longValue(), withDefaultConfigOrdering(e.getKey()).getRelativeOrderInconsistencyCases());
        }
    }

    @Test
    public void testAccessorsSplit() throws Exception {
        final MethodOrder ds = withDefaultConfigOrdering("InputDependenciesAccessorsSplit.java");
        assertEquals(3, ds.getAccessorsSplitCases());
    }

    @Test
    public void testCallsBetweenDistantMethods() throws Exception {
        final int screenLinesCount = 5;
        final Map<String, Integer> expected = new TreeMap<>();
        expected.put("InputDependenciesDistantMethodCall1.java", 1);
        expected.put("InputDependenciesDistantMethodCall2.java", 2);
        expected.put("InputDependenciesDistantMethodCall3.java", 1);
        expected.put("InputDependenciesDistantMethodCall4.java", 0);
        for(final Map.Entry<String, Integer> e : expected.entrySet()) {
            final MethodOrder dependencies = withDefaultConfigOrdering(e.getKey());
            final String msg = String.format("Incorrect result for input \"%s\"", e.getKey());
            assertEquals(msg, e.getValue().intValue(),
                dependencies.getDependenciesBetweenDistantMethodsCases(screenLinesCount));
        }
    }
}
