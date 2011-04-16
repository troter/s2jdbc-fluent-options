package jp.troter.seasar.extension.jdbc.options;

import static org.easymock.EasyMock.expect;

import java.util.Collections;

import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.AutoFunctionCall;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;

@RunWith(Seasar2.class)
public class AutoFunctionCallOptionsTest {

    public static class Entity {}

    AutoFunctionCallOptions<Entity> s;
    AutoFunctionCallOptions<Entity> o;

    @EasyMock(EasyMockType.STRICT)
    private AutoFunctionCall<Entity> applied;

    private Entity result;

    @Before
    public void before() {
        s = new AutoFunctionCallOptions<Entity>();
        o = new AutoFunctionCallOptions<Entity>();
    }

    public void recordSmoke() throws Exception {
        result = new Entity();
        expect(applied.callerClass(Entity.class)).andReturn(applied);
        expect(applied.callerMethodName("callerMethodName")).andReturn(applied);
        expect(applied.queryTimeout(10)).andReturn(applied);
        expect(applied.maxRows(100)).andReturn(applied);
        expect(applied.fetchSize(1000)).andReturn(applied);
        expect(applied.lob()).andReturn(applied);
        expect(applied.temporal(TemporalType.DATE)).andReturn(applied);
        expect(applied.getSingleResult()).andReturn(result);
    }

    @Test
    public void smoke() {
        s.callerClass(Entity.class);
        s.callerMethodName("callerMethodName");
        s.queryTimeout(10);
        s.maxRows(100);
        s.fetchSize(1000);
        s.lob();
        s.temporal(TemporalType.DATE);

        s.apply(o);
        o.apply(applied).getSingleResult();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void unsuppotedException_getSingleResult() {
        s.getSingleResult();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void unsuppotedException_getResultList() {
        s.getResultList();
    }

    public void recordExecuteOnly() throws Exception {
        result = new Entity();
        expect(applied.getSingleResult()).andReturn(result);
        expect(applied.getResultList()).andReturn(Collections.<Entity>emptyList());
    }

    @Test
    public void executeOnly() {
        s.apply(o);
        o.apply(applied).getSingleResult();
        o.apply(applied).getResultList();
    }

}
