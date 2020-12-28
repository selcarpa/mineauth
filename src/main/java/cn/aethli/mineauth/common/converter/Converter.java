package cn.aethli.mineauth.common.converter;

/** @author 93162 */
public interface Converter<T> {
  T valueOf(String src);

  // never used
  String parse(Object o);
}
