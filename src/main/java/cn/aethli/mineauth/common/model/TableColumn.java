package cn.aethli.mineauth.common.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * utils for database
 *
 * @author selcaNyan
 */
public class TableColumn {
  protected String alias;
  protected String columnName;
  protected ColumnType sqlType;
  protected boolean nullAble;
  protected boolean primaryKey;
  protected String javaType;

  public TableColumn(String alias, String columnName, ColumnType sqlType, boolean nullAble,String javaType) {
    this(alias, columnName, sqlType, nullAble, false, javaType);
  }

  public TableColumn(
          String alias, String columnName, ColumnType sqlType, boolean nullAble, boolean primaryKey,String javaType) {
    this.alias = alias;
    this.columnName = columnName;
    this.sqlType = sqlType;
    this.nullAble = nullAble;
    this.primaryKey = primaryKey;
    this.javaType = javaType;
  }

  public String getAlias() {
    return alias;
  }

  public String getColumnName() {
    return columnName;
  }

  public ColumnType getSqlType() {
    return sqlType;
  }

  public boolean isNullAble() {
    return nullAble;
  }

  public boolean isPrimaryKey() {
    return primaryKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    TableColumn that = (TableColumn) o;

    return new EqualsBuilder().append(alias, that.alias).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(alias).toHashCode();
  }
}
