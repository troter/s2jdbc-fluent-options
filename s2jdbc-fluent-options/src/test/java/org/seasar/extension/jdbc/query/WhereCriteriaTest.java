package org.seasar.extension.jdbc.query;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.util.tiger.CollectionsUtil;

@RunWith(Seasar2.class)
public class WhereCriteriaTest {

    public static interface IJdbcManagerImpl extends JdbcManager, JdbcManagerImplementor {}

    @javax.persistence.Entity
    public static class Entity {
        @Id
        public Integer id;

        public String name;

        @Temporal(TemporalType.TIMESTAMP)
        public Date created;
    }

    AutoSelectImpl<Entity> a;
    AutoSelectImpl<Entity> b;
    Date now;
    Where andEmpty;
    Where orEmpty;
    Where eq;
    Where and;
    Where or;
    Where orAnd;
    Where fixed;
    Where fixedOrAnd;

    Map<String, Object> conditionsAnd;

    @EasyMock(value=EasyMockType.DEFAULT)
    IJdbcManagerImpl jdbcManager;
    EntityMetaFactoryImpl entityMetaFactoryImpl;
    TableMetaFactoryImpl tableMetaFactoryImpl;
    PropertyMetaFactoryImpl propertyMetaFactoryImpl;
    ColumnMetaFactoryImpl columnMetaFactoryImpl;
    PersistenceConventionImpl persistenceConventionImpl;

    @Before
    public void before() {
        now = new Date();

        andEmpty = Operations.and();
        orEmpty = Operations.or();
        eq = Operations.eq(new PropertyName<String>("name"), "troter");
        and = Operations.and(
                Operations.eq(new PropertyName<String>("name"), "troter"),
                Operations.in(new PropertyName<Integer>("id"), 1, 2, 3, 4)
        );
        or = Operations.or(
                Operations.eq(new PropertyName<String>("name"), "troter"),
                Operations.in(new PropertyName<Integer>("id"), 1, 2, 3, 4)
        );
        orAnd = Operations.or(
                Operations.and(
                        Operations.eq(new PropertyName<String>("name"), "troter"),
                        Operations.in(new PropertyName<Integer>("id"), 1, 2, 3, 4)
                ),
                Operations.gt(new PropertyName<Date>("created"), now)
        );
        fixed = new Where() {
            public String getCriteria() {
                return "name = 'troter'";
            }
            public Object[] getParams() {
                return new Object[0];
            }
            public String[] getPropertyNames() {
                return new String[0];
            }
        };
        fixedOrAnd = Operations.and(fixed, orAnd);

        conditionsAnd = CollectionsUtil.newHashMap();
        conditionsAnd.put("name_EQ", "troter");
        conditionsAnd.put("id_IN", Arrays.asList(1, 2, 3, 4));
    }

    @Test
    public void spec_where() {
        assertThat(andEmpty.getCriteria(), is(""));
        assertThat(andEmpty.getParams(), is(os()));
        assertThat(andEmpty.getPropertyNames(), is(ss()));

        assertThat(orEmpty.getCriteria(), is(""));
        assertThat(orEmpty.getParams(), is(os()));
        assertThat(orEmpty.getPropertyNames(), is(ss()));

        assertThat(eq.getCriteria(), is("name = ?"));
        assertThat(eq.getParams(), is(os("troter")));
        assertThat(eq.getPropertyNames(), is(ss("name")));

        assertThat(and.getCriteria(), is("(name = ?) and (id in (?, ?, ?, ?))"));
        assertThat(and.getParams(), is(os("troter", 1, 2, 3, 4)));
        assertThat(and.getPropertyNames(), is(ss("name", "id", "id", "id", "id")));

        assertThat(or.getCriteria(), is("(name = ?) or (id in (?, ?, ?, ?))"));
        assertThat(or.getParams(), is(os("troter", 1, 2, 3, 4)));
        assertThat(or.getPropertyNames(), is(ss("name", "id", "id", "id", "id")));

        assertThat(orAnd.getCriteria(), is("((name = ?) and (id in (?, ?, ?, ?))) or (created > ?)"));
        assertThat(orAnd.getParams(), is(os("troter", 1, 2, 3, 4, now)));
        assertThat(orAnd.getPropertyNames(), is(ss("name", "id", "id", "id", "id", "created")));
    }
    
    public void setupEntityMetaFactory() {
        entityMetaFactoryImpl = new EntityMetaFactoryImpl();
        persistenceConventionImpl = new PersistenceConventionImpl();

        tableMetaFactoryImpl = new TableMetaFactoryImpl();
        tableMetaFactoryImpl.setPersistenceConvention(persistenceConventionImpl);

        columnMetaFactoryImpl = new ColumnMetaFactoryImpl();
        columnMetaFactoryImpl.setPersistenceConvention(persistenceConventionImpl);

        propertyMetaFactoryImpl = new PropertyMetaFactoryImpl();
        propertyMetaFactoryImpl.setPersistenceConvention(persistenceConventionImpl);
        propertyMetaFactoryImpl.setColumnMetaFactory(columnMetaFactoryImpl);

        entityMetaFactoryImpl.setTableMetaFactory(tableMetaFactoryImpl);
        entityMetaFactoryImpl.setPropertyMetaFactory(propertyMetaFactoryImpl);
    }

    public void recordSmoke() {
        setupEntityMetaFactory();

        expect(jdbcManager.getDialect()).andReturn(new StandardDialect());
        expectLastCall().anyTimes();

        expect(jdbcManager.getEntityMetaFactory()).andReturn(entityMetaFactoryImpl);
        expectLastCall().anyTimes();

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        b = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
    }

    @Test
    public void smoke() {
        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(andEmpty);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(eq);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where (T1_.NAME = ?)"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(and);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where ((T1_.NAME = ?) and (T1_.ID in (?, ?, ?, ?)))"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(eq);
        a.where("name = ?", "troter");
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where (T1_.NAME = ?)"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(and);
        a.where("name = ?", "troter");
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where (T1_.NAME = ?)"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where("name = ?", "troter");
        a.where(and);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where ((T1_.NAME = ?) and (T1_.ID in (?, ?, ?, ?)))"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(conditionsAnd);
        a.where("name = ?", "troter");
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where (T1_.ID in (?, ?, ?, ?) and T1_.NAME = ?) and (T1_.NAME = ?)"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where("name = ?", "troter");
        a.where(conditionsAnd);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where (T1_.ID in (?, ?, ?, ?) and T1_.NAME = ?) and (T1_.NAME = ?)"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(fixed);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where (T1_.NAME = 'troter')"));

        a = new AutoSelectImpl<Entity>(jdbcManager, Entity.class);
        a.where(fixedOrAnd);
        a.prepare("getResultList");
        assertThat(a.executedSql, is("select T1_.ID as C1_, T1_.NAME as C2_, T1_.CREATED as C3_ from WHERE_CRITERIA_TEST$_ENTITY T1_ where ((T1_.NAME = 'troter') and (((T1_.NAME = ?) and (T1_.ID in (?, ?, ?, ?))) or (T1_.CREATED > ?)))"));
    }

    public Object[] os(Object ...objects) { return objects; }
    public String[] ss(String ...strings) { return strings; }

}
