package jp.troter.seasar.extension.jdbc.options;

import org.seasar.extension.jdbc.ModuleCall;

public abstract class AbstractModuleCallOptions<S extends ModuleCall<S>> extends AbstractQueryOptions<S>
        implements ModuleCall<S> {

    protected int maxRows;
    protected boolean isNeedApplyMaxRows = false;

    protected int fetchSize;
    protected boolean isNeedApplyFetchSize = false;

    @Override
    public S apply(S applied) {
        applied = super.apply(applied);
        if (isNeedApplyMaxRows) { applied.maxRows(maxRows); }
        if (isNeedApplyFetchSize) { applied.fetchSize(fetchSize); }
        return applied;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S maxRows(int maxRows) {
        this.maxRows = maxRows;
        isNeedApplyMaxRows = true;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        isNeedApplyFetchSize = true;
        return (S) this;
    }

}
