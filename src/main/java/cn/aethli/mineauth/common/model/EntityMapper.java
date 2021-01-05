package cn.aethli.mineauth.common.model;

import java.lang.reflect.Field;
import java.util.Set;

/** @author SelcaNyan */
public class EntityMapper {
  private Set<TableColumn> tableColumns;
  private Set<Field> fields;
  private String className;
  private String tableName;

  public Set<TableColumn> getTableColumns() {
    return tableColumns;
  }

  public void setTableColumns(Set<TableColumn> tableColumns) {
    this.tableColumns = tableColumns;
  }

  public Set<Field> getFields() {
    return fields;
  }

  public void setFields(Set<Field> fields) {
    this.fields = fields;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
