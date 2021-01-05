package cn.aethli.mineauth.common.converter;

/** @author SelcaNyan */
public class BooleanConverter implements Converter<Boolean> {
  @Override
  public Boolean valueOf(String src) {
    return Boolean.valueOf(src);
  }

  @Override
  public String parse(Object o) {
    return o.toString();
  }
}
