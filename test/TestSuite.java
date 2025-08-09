// -----------------------------
// File: src/test/TestSuite.java
// -----------------------------
package test;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

/**
 * Test suite runner for Java Text Editor
 * Runs all unit tests and provides summary
 */
public class TestSuite {
    
    public static void main(String[] args) {
        System.out.println("=== Java Text Editor Test Suite ===");
        System.out.println("Running all unit tests...\n");
        
        // Create launcher discovery request
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(selectPackage("test"))
            .build();
        
        // Create launcher and summary listener
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        // Execute tests
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        
        // Print summary
        TestExecutionSummary summary = listener.getSummary();
        printSummary(summary);
        
        // Exit with appropriate code
        if (summary.getTestsFailedCount() > 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
    
    private static void printSummary(TestExecutionSummary summary) {
        System.out.println("\n=== Test Results Summary ===");
        System.out.println("Tests run: " + summary.getTestsStartedCount());
        System.out.println("Tests passed: " + summary.getTestsSucceededCount());
        System.out.println("Tests failed: " + summary.getTestsFailedCount());
        System.out.println("Tests skipped: " + summary.getTestsSkippedCount());
        System.out.println("Test execution completed");
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\n=== Failed Tests ===");
            summary.getFailures().forEach(failure -> {
                System.out.println("FAILED: " + failure.getTestIdentifier().getDisplayName());
                System.out.println("Reason: " + failure.getException().getMessage());
                System.out.println();
            });
        }
        
        if (summary.getTestsSucceededCount() == summary.getTestsStartedCount()) {
            System.out.println("\n✅ All tests passed!");
        } else {
            System.out.println("\n❌ Some tests failed.");
        }
    }
}