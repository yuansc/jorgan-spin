package spin;


import junit.framework.TestCase;
import spin.Evaluator;
import spin.Invocation;
import spin.ProxyFactory;

public abstract class AbstractProxyFactoryTest extends TestCase {

    protected abstract ProxyFactory getFactory();

    public void testRun() {

        final RunnableBean runnable = new RunnableBean();

        Evaluator evaluator = new Evaluator() {
            public void evaluate(Invocation invocation) throws Throwable {
                assertEquals(false, runnable.evaluated);
                assertEquals(false, runnable.run);

                runnable.evaluated = true;

                invocation.evaluate();

                assertEquals(true, runnable.run);
            }
        };

        Runnable proxy = (Runnable) getFactory().createProxy(runnable,
                evaluator);

        proxy.run();
    }

    public void testReflexive() throws Exception {

        Runnable runnable = new RunnableBean();

        Runnable proxy = (Runnable) getFactory().createProxy(runnable,
                createEvaluator());

        assertTrue("equals() is not reflexive", proxy.equals(proxy));
    }

    public void testTwoProxiesOfSameBeanAreEqual() throws Exception {

        Runnable runnable = new RunnableBean();

        Runnable proxy1 = (Runnable) getFactory().createProxy(runnable,
                createEvaluator());
        Runnable proxy2 = (Runnable) getFactory().createProxy(runnable,
                createEvaluator());

        assertTrue("two proxies of same bean are not equal", proxy1
                .equals(proxy2));
    }

    public void testTwoProxiesOfDifferentBeansAreNotEqual() throws Exception {

        Runnable runnable1 = new RunnableBean();

        Runnable runnable2 = new RunnableBean();

        Runnable proxy1 = (Runnable) getFactory().createProxy(runnable1,
                createEvaluator());
        Runnable proxy2 = (Runnable) getFactory().createProxy(runnable2,
                createEvaluator());

        assertTrue("two proxies of different beans are equal", !proxy1
                .equals(proxy2));
    }

    public static class RunnableBean implements Runnable {

        public boolean evaluated = false;

        public boolean run = false;

        public void run() {
            run = true;
        }
    }

    private Evaluator createEvaluator() {
        return new Evaluator() {
            public void evaluate(Invocation invocation) throws Throwable {
                invocation.evaluate();
            };
        };
    }
}