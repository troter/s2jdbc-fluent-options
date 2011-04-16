package jp.troter.seasar.extension.jdbc.options;

import java.util.Arrays;
import java.util.Set;

import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * SQLを自動生成するバッチ挿入のためのOptionsです。<br>
 * <p>
 * includes, excludesなどのメソッドは引数を蓄積します。
 * </p>
 * @param <T>
 *            エンティティの型です。
 */
public class AutoBatchInsertOptions<T> extends
        AbstractAutoBatchUpdateOptions<T, AutoBatchInsert<T>> implements AutoBatchInsert<T> {

    protected Set<CharSequence> includes = CollectionsUtil.newLinkedHashSet();

    protected Set<CharSequence> excludes = CollectionsUtil.newLinkedHashSet();

    @Override
    public AutoBatchInsert<T> apply(AutoBatchInsert<T> applied) {
        applied = super.apply(applied);
        if (! includes.isEmpty()) { applied.includes(includes.toArray(new CharSequence[0])); }
        if (! excludes.isEmpty()) { applied.excludes(excludes.toArray(new CharSequence[0])); }
        return applied;
    }

    @Override
    public AutoBatchInsert<T> includes(CharSequence... propertyNames) {
        if (propertyNames != null) { includes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

    @Override
    public AutoBatchInsert<T> excludes(CharSequence... propertyNames) {
        if (propertyNames != null) { excludes.addAll(Arrays.asList(propertyNames)); }
        return this;
    }

}
