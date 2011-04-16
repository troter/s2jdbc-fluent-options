package jp.troter.seasar.extension.jdbc.options;

import jp.troter.seasar.extension.jdbc.Options;

import org.seasar.extension.jdbc.Query;

public abstract class AbstractQueryOptions<S extends Query<S>> implements Query<S>, Options<S> {

    /**
     * クエリを呼び出すクラスです。
     */
    protected Class<?> callerClass;

    /**
     * クエリを呼び出すメソッド名です。
     */
    protected String callerMethodName;

    /**
     * クエリタイムアウトの秒数です。
     */
    protected int queryTimeout;

    /**
     * クエリタイムアウトの秒数の適応が必要か
     */
    protected boolean isNeedApplyQueryTimeout = false;

    @Override
    public S apply(S applied) {
        if (callerClass != null) { applied.callerClass(callerClass); }
        if (callerMethodName != null) { applied.callerMethodName(callerMethodName); }
        if (isNeedApplyQueryTimeout) { applied.queryTimeout(queryTimeout); }
        return applied;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S callerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S callerMethodName(String callerMethodName) {
        this.callerMethodName = callerMethodName;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        isNeedApplyQueryTimeout = true;
        return (S) this;
    }

}
