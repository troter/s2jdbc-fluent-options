package jp.troter.seasar.extension.jdbc.options;

import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;

@RunWith(Seasar2.class)
public class AutoBatchInsertOptionsTest {

    public static class Entity {}

    AutoBatchInsertOptions<Entity> s;
    AutoBatchInsertOptions<Entity> o;

    @EasyMock(EasyMockType.STRICT)
    private AutoBatchInsert<Entity> applied;

    @Before
    public void before() {
        s = new AutoBatchInsertOptions<Entity>();
        o = new AutoBatchInsertOptions<Entity>();
    }

    public void recordSmoke() throws Exception {
        expect(applied.callerClass(Entity.class)).andReturn(applied);
        expect(applied.callerMethodName("callerMethodName")).andReturn(applied);
        expect(applied.queryTimeout(10)).andReturn(applied);
        expect(applied.includes("one", "two", "three", "four", "five")).andReturn(applied);
        expect(applied.excludes("six", "seven", "eight", "nine", "ten")).andReturn(applied);
        expect(applied.execute()).andReturn(new int[0]);
    }

    @Test
    public void smoke() {
        s.callerClass(Entity.class);
        s.callerMethodName("callerMethodName");
        s.queryTimeout(10);
        s.includes("one", "two", "three"); s.includes("four", "five");
        s.excludes("six", "seven"); s.excludes("eight", "nine", "ten");

        s.apply(o);
        o.apply(applied).execute();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void unsuppotedException() {
        s.execute();
    }

    public void recordExecuteOnly() throws Exception {
        expect(applied.execute()).andReturn(new int[0]);
    }

    @Test
    public void executeOnly() {
        s.apply(o);
        o.apply(applied).execute();
    }

}
