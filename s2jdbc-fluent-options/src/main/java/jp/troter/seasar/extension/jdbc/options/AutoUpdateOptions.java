package jp.troter.seasar.extension.jdbc.options;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.seasar.extension.jdbc.AutoUpdate;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class AutoUpdateOptions<T> extends AbstractAutoUpdateOptions<T, AutoUpdate<T>>
        implements AutoUpdate<T> {

    /** includesVersionの適応が必要か */
    protected boolean isNeedApplyIncludesVersion = false;

    /** excludesNullの適応が必要か */
    protected boolean isNeedApplyExcludesNull = false;
    
    protected Set<CharSequence> includes = CollectionsUtil.newLinkedHashSet();

    protected Set<CharSequence> excludes = CollectionsUtil.newLinkedHashSet();

    protected T changedFromEntity;

    protected Map<String, ? extends Object>  changedFromMap;

    /** suppresOptimisticLockExceptionの適応が必要か */
    protected boolean isNeedApplySuppresOptimisticLockException = false;

    @Override
    public AutoUpdate<T> apply(AutoUpdate<T> applied) {
        applied = super.apply(applied);
        if (isNeedApplyIncludesVersion) { applied.includesVersion(); }
        if (isNeedApplyExcludesNull) { applied.excludesNull(); }
        if (! includes.isEmpty()) { applied.includes(includes.toArray(new CharSequence[0])); }
        if (! excludes.isEmpty()) { applied.excludes(excludes.toArray(new CharSequence[0])); }
        if (changedFromEntity != null) { applied.changedFrom(changedFromEntity); }
        if (changedFromMap != null) { applied.changedFrom(changedFromMap); }
        if (isNeedApplySuppresOptimisticLockException) {applied.suppresOptimisticLockException(); }
        return applied;
    }

    @Override
    public AutoUpdate<T> includesVersion() {
        isNeedApplyIncludesVersion = true;
        return this;
    }

    @Override
    public AutoUpdate<T> excludesNull() {
        isNeedApplyExcludesNull = true;
        return this;
    }

    @Override
    public AutoUpdate<T> includes(CharSequence... propertyNames) {
        if (propertyNames != null) { includes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

    @Override
    public AutoUpdate<T> excludes(CharSequence... propertyNames) {
        if (propertyNames != null) { excludes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

    @Override
    public AutoUpdate<T> changedFrom(T before) {
        this.changedFromEntity = before;
        return this;
    }

    @Override
    public AutoUpdate<T> changedFrom(Map<String, ? extends Object> before) {
        this.changedFromMap = before;
        return this;
    }

    @Override
    public AutoUpdate<T> suppresOptimisticLockException() {
        this.isNeedApplySuppresOptimisticLockException = true;
        return this;
    }

}
