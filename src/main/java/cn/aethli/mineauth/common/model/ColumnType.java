package cn.aethli.mineauth.common.model;

/** @author selcaNyan */
public class ColumnType {
  private final String type;
  private Integer length;

  public ColumnType(String type, int length) {
    this.type = type;
    this.length = length;
  }

  public ColumnType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return type + (length == null ? "" : "(" + length + ")");
  }
}
