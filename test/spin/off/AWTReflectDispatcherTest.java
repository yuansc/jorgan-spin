package spin.off;

import javax.swing.SwingUtilities;

import junit.framework.TestCase;

public class AWTReflectDispatcherTest extends TestCase {

    public void test() throws Exception {
        AWTReflectDispatcherFactory factory = new AWTReflectDispatcherFactory();

        final Dispatcher dispatcher = factory.createDispatcher();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    dispatcher.start();
                } catch (Throwable throwable) {
                    fail(throwable.getMessage());
                }
            }
        });
        
        Thread.sleep(2000);
        
        dispatcher.stop();
    }
}
