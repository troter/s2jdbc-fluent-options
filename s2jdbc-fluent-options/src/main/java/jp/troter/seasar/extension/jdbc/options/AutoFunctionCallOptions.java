package jp.troter.seasar.extension.jdbc.options;

import org.seasar.extension.jdbc.AutoFunctionCall;

/**
 * SQLを自動生成するファンクション呼び出しのためのOptionsです。
 *
 * @param <T>
 *            ファンクションの戻り値の型。戻り値が結果セットの場合は<code>List</code>の要素の型
 */
public class AutoFunctionCallOptions<T> extends AbstractFunctionCallOptions<T, AutoFunctionCall<T>>
        implements AutoFunctionCall<T> {

}
