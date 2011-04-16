package jp.troter.seasar.extension.jdbc.options;

import java.util.List;

import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.FunctionCall;

public abstract class AbstractFunctionCallOptions<T, S extends FunctionCall<T, S>>
        extends AbstractModuleCallOptions<S> implements FunctionCall<T, S> {

    protected boolean isNeedApplyLob = false;

    protected TemporalType temporalType;

    @Override
    public S apply(S applied) {
        applied = super.apply(applied);
        if (isNeedApplyLob) { applied.lob(); }
        if (temporalType != null) { applied.temporal(temporalType); }
        return applied;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S lob() {
        isNeedApplyLob = true;
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S temporal(TemporalType temporalType) {
        this.temporalType = temporalType;
        return (S) this;
    }

    @Override
    public T getSingleResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> getResultList() {
        throw new UnsupportedOperationException();
    }

}
