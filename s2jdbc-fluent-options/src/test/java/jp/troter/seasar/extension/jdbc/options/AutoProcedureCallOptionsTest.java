package jp.troter.seasar.extension.jdbc.options;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.AutoProcedureCall;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;

@RunWith(Seasar2.class)
public class AutoProcedureCallOptionsTest {

    public static class Entity {}

    AutoProcedureCallOptions s;
    AutoProcedureCallOptions o;

    @EasyMock(EasyMockType.STRICT)
    private AutoProcedureCall applied;

    @Before
    public void before() {
        s = new AutoProcedureCallOptions();
        o = new AutoProcedureCallOptions();
    }

    public void recordSmoke() throws Exception {
        expect(applied.callerClass(Entity.class)).andReturn(applied);
        expect(applied.callerMethodName("callerMethodName")).andReturn(applied);
        expect(applied.queryTimeout(10)).andReturn(applied);
        expect(applied.maxRows(100)).andReturn(applied);
        expect(applied.fetchSize(1000)).andReturn(applied);
        applied.execute();
        expectLastCall().atLeastOnce();
    }

    @Test
    public void smoke() {
        s.callerClass(Entity.class);
        s.callerMethodName("callerMethodName");
        s.queryTimeout(10);
        s.maxRows(100);
        s.fetchSize(1000);

        s.apply(o);
        o.apply(applied).execute();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void unsuppotedException() {
        s.execute();
    }

    public void recordExecuteOnly() throws Exception {
        applied.execute();
        expectLastCall().atLeastOnce();
    }

    @Test
    public void executeOnly() {
        s.apply(o);
        o.apply(applied).execute();
    }

}
