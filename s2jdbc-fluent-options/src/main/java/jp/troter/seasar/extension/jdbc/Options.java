package jp.troter.seasar.extension.jdbc;

/**
 * オプションのベースとなるインターフェースです。
 *
 * @param <T>
 */
public interface Options<T> {

    T apply(T applied);
}
