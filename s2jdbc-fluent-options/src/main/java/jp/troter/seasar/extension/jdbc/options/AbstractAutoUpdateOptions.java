package jp.troter.seasar.extension.jdbc.options;

import org.seasar.extension.jdbc.Update;

public abstract class AbstractAutoUpdateOptions<T, S extends Update<S>> extends
        AbstractQueryOptions<S> implements Update<S> {

    @Override
    public S apply(S applied) {
        applied = super.apply(applied);
        return applied;
    }

    @Override
    public int execute() {
        throw new UnsupportedOperationException();
    }

}
