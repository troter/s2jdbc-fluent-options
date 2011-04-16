package jp.troter.seasar.extension.jdbc.options;

import java.util.Arrays;
import java.util.Set;

import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * SQLを自動生成するバッチ更新のためのOptionsです。<br>
 * <p>
 * includes, excludesなどのメソッドは引数を蓄積します。
 * </p>
 * @param <T>
 *            エンティティの型です。
 */
public class AutoBatchUpdateOptions<T> extends
        AbstractAutoBatchUpdateOptions<T, AutoBatchUpdate<T>> implements
        AutoBatchUpdate<T> {

    /** includesVersionの適応が必要か */
    protected boolean isNeedApplyIncludesVersion = false;

    protected Set<CharSequence> includes = CollectionsUtil.newLinkedHashSet();

    protected Set<CharSequence> excludes = CollectionsUtil.newLinkedHashSet();

    /** suppresOptimisticLockExceptionの適応が必要か */
    protected boolean isNeedApplySuppresOptimisticLockException = false;

    @Override
    public AutoBatchUpdate<T> apply(AutoBatchUpdate<T> applied) {
        applied = super.apply(applied);
        if (isNeedApplyIncludesVersion) { applied.includesVersion(); }
        if (! includes.isEmpty()) { applied.includes(includes.toArray(new CharSequence[0])); }
        if (! excludes.isEmpty()) { applied.excludes(excludes.toArray(new CharSequence[0])); }
        if (isNeedApplySuppresOptimisticLockException) {applied.suppresOptimisticLockException(); }
        return applied;
    }

    @Override
    public AutoBatchUpdate<T> includesVersion() {
        isNeedApplyIncludesVersion = true;
        return this;
    }

    @Override
    public AutoBatchUpdate<T> includes(CharSequence... propertyNames) {
        if (propertyNames != null) { includes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

    @Override
    public AutoBatchUpdate<T> excludes(CharSequence... propertyNames) {
        if (propertyNames != null) { excludes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

    @Override
    public AutoBatchUpdate<T> suppresOptimisticLockException() {
        this.isNeedApplySuppresOptimisticLockException = true;
        return this;
    }

}
