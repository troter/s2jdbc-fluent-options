package jp.troter.seasar.extension.jdbc.options;

import org.seasar.extension.jdbc.ProcedureCall;

public abstract class AbstractProcedureCallOptions<S extends ProcedureCall<S>> extends AbstractModuleCallOptions<S>
        implements ProcedureCall<S> {

    @Override
    public S apply(S applied) {
        applied = super.apply(applied);
        return applied;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
    }

}
