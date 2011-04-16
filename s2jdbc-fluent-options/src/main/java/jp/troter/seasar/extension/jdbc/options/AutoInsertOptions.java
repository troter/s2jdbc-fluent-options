package jp.troter.seasar.extension.jdbc.options;

import java.util.Arrays;
import java.util.Set;

import org.seasar.extension.jdbc.AutoInsert;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * SQLを自動生成する挿入のためのOptionsです。<br>
 * <p>
 * includes, excludesなどのメソッドは引数を蓄積します。
 * </p>
 * @param <T>
 *            エンティティの型です。
 */
public class AutoInsertOptions<T> extends AbstractAutoUpdateOptions<T, AutoInsert<T>>
        implements AutoInsert<T> {

    /** excludesNullの適応が必要か */
    protected boolean isNeedApplyExcludesNull = false;

    protected Set<CharSequence> includes = CollectionsUtil.newLinkedHashSet();

    protected Set<CharSequence> excludes = CollectionsUtil.newLinkedHashSet();

    @Override
    public AutoInsert<T> apply(AutoInsert<T> applied) {
        applied = super.apply(applied);
        if (isNeedApplyExcludesNull) { applied.excludesNull(); }
        if (! includes.isEmpty()) { applied.includes(includes.toArray(new CharSequence[0])); }
        if (! excludes.isEmpty()) { applied.excludes(excludes.toArray(new CharSequence[0])); }
        return applied;
    }

    @Override
    public AutoInsert<T> excludesNull() {
        isNeedApplyExcludesNull = true;
        return this;
    }

    @Override
    public AutoInsert<T> includes(CharSequence... propertyNames) {
        if (propertyNames != null) { includes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

    @Override
    public AutoInsert<T> excludes(CharSequence... propertyNames) {
        if (propertyNames != null) { excludes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

}
