package jp.troter.seasar.extension.jdbc.options;

import static org.easymock.EasyMock.expect;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.AutoUpdate;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.util.tiger.CollectionsUtil;

@RunWith(Seasar2.class)
public class AutoUpdateOptionsTest {

    public static class Entity {}
    
    AutoUpdateOptions<Entity> s;
    AutoUpdateOptions<Entity> o;

    @EasyMock(EasyMockType.STRICT)
    private AutoUpdate<Entity> applied;

    private Entity changedFromEntity;
    private Map<String, ? extends Object> changedFromMap;

    @Before
    public void before() {
        s = new AutoUpdateOptions<Entity>();
        o = new AutoUpdateOptions<Entity>();
    }

    public void recordSmoke() throws Exception {
        changedFromEntity = new Entity();
        changedFromMap = CollectionsUtil.newHashMap();
        expect(applied.callerClass(Entity.class)).andReturn(applied);
        expect(applied.callerMethodName("callerMethodName")).andReturn(applied);
        expect(applied.queryTimeout(10)).andReturn(applied);
        expect(applied.includesVersion()).andReturn(applied);
        expect(applied.excludesNull()).andReturn(applied);
        expect(applied.includes("one", "two", "three", "four", "five")).andReturn(applied);
        expect(applied.excludes("six", "seven", "eight", "nine", "ten")).andReturn(applied);
        expect(applied.changedFrom(changedFromEntity)).andReturn(applied);
        expect(applied.changedFrom(changedFromMap)).andReturn(applied);
        expect(applied.suppresOptimisticLockException()).andReturn(applied);
        expect(applied.execute()).andReturn(0);
    }

    @Test
    public void smoke() {

        s.callerClass(Entity.class);
        s.callerMethodName("callerMethodName");
        s.queryTimeout(10);
        s.includesVersion();
        s.excludesNull();
        s.includes("one", "two", "three"); s.includes("four", "five");
        s.excludes("six", "seven"); s.excludes("eight", "nine", "ten");
        s.changedFrom(changedFromEntity);
        s.changedFrom(changedFromMap);
        s.suppresOptimisticLockException();

        s.apply(o);
        o.apply(applied).execute();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void unsuppotedException() {
        s.execute();
    }

    public void recordExecuteOnly() throws Exception {
        expect(applied.execute()).andReturn(0);
    }

    @Test
    public void executeOnly() {
        s.apply(o);
        o.apply(applied).execute();
    }

}
