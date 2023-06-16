package spec;

public interface TestObserver {
    void onTestStarted(String testName);
    void onTestFinished(String testName, boolean passed);
}
