import org.junit.jupiter.api.extension.*;
public class TestLoggerExtension implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context){
        System.out.println("TEST PASSED "+context.getDisplayName()+"\n");
    }
    @Override
    public void testFailed(ExtensionContext context,Throwable cause){
        System.out.println("TEST FAILED "+context.getDisplayName()+"\n");
    }
}
