package spec;


public class TestExecutionObserver implements TestObserver {

    @Override
    public void onTestStarted(String testName){
        System.out.println("Test started: " + testName);
    }

    @Override
    public void onTestFinished(String testName, boolean passed){
        System.out.println("Test finished: " + testName + ", Passed " + passed);
    }
}
