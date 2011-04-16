package jp.troter.seasar.extension.jdbc.options;

import org.seasar.extension.jdbc.BatchUpdate;

public class AbstractAutoBatchUpdateOptions<T, S extends BatchUpdate<S>> extends AbstractQueryOptions<S>
        implements BatchUpdate<S> {

    protected int batchSize;
    protected boolean isNeedApplyBatchSize = false;

    @Override
    public S apply(S applied) {
        applied = super.apply(applied);
        if (isNeedApplyBatchSize) { applied.batchSize(batchSize); }
        return applied;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S batchSize(int batchSize) {
        this.batchSize = batchSize;
        isNeedApplyBatchSize = true;
        return (S) this;
    }

    @Override
    public int[] execute() {
        throw new UnsupportedOperationException();
    }

}
