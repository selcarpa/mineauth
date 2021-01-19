package cn.aethli.mineauth.common.utils;

import cn.aethli.mineauth.Mineauth;
import cn.aethli.mineauth.common.converter.BooleanConverter;
import cn.aethli.mineauth.common.converter.Converter;
import cn.aethli.mineauth.common.converter.LocalDateTimeConverter;
import cn.aethli.mineauth.common.converter.StringConverter;
import cn.aethli.mineauth.common.model.ColumnType;
import cn.aethli.mineauth.common.model.EntityMapper;
import cn.aethli.mineauth.common.model.TableColumn;
import cn.aethli.mineauth.config.MineauthConfig;
import cn.aethli.mineauth.datasource.ExpansionAbleConnectionPool;
import cn.aethli.mineauth.entity.AuthPlayer;
import cn.aethli.mineauth.entity.BaseEntity;
import cn.aethli.mineauth.exception.DataRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

/**
 * utils for database
 *
 * @author selcaNyan
 */
public class DataUtils {
  private static final Map<String, Converter<?>> CONVERTER_MAP = new HashMap<>();
  private static final Logger LOGGER = LogManager.getLogger(DataUtils.class);

  static {
    CONVERTER_MAP.put(Boolean.class.getTypeName(), new BooleanConverter());
    CONVERTER_MAP.put(String.class.getTypeName(), new StringConverter());
    CONVERTER_MAP.put(LocalDateTime.class.getTypeName(), new LocalDateTimeConverter());
  }

  public static void databaseInit() throws SQLException {

    MetadataUtils.initMetadata();
    tableColumnInit();

    ExpansionAbleConnectionPool connectionPool = ExpansionAbleConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();

    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet tableResultSet =
        metaData.getTables(
            connection.getCatalog(),
            null,
            MineauthConfig.databaseConfig.table.get(),
            new String[] {"TABLE"});

    EntityMapper playerEntityMapper =
        MetadataUtils.getEntityMapperByTypeName(AuthPlayer.class.getTypeName());
    if (playerEntityMapper == null || playerEntityMapper.getFields() == null) {
      throw new DataRuntimeException(
          "Class metadata has not been initialized: " + AuthPlayer.class.getTypeName());
    }
    Set<TableColumn> playerTableColumn = playerEntityMapper.getTableColumns();

    List<String> needColumns =
        playerTableColumn.stream().map(TableColumn::getColumnName).collect(Collectors.toList());

    if (needColumns.size() > needColumns.stream().distinct().count()) {
      throw new DataRuntimeException(
          "Got duplicate column name in mineauth-server.toml, please check it");
    }

    if (tableResultSet.isBeforeFirst() && tableResultSet.next()) {
      // column test
      ResultSet columnResultSet =
          metaData.getColumns(
              connection.getCatalog(), null, MineauthConfig.databaseConfig.table.get(), "%");
      List<String> columnNames = new ArrayList<>();
      while (columnResultSet.next()) {
        columnNames.add(columnResultSet.getString("COLUMN_NAME"));
      }
      needColumns.removeAll(columnNames);
      if (!needColumns.isEmpty()) {
        throw new DataRuntimeException(
            "Specified table exists, but the following column does not exists:"
                + String.join(",", needColumns));
      }
    } else {
      String createTableSql =
          "create table "
              + playerEntityMapper.getTableName()
              + " ("
              + playerTableColumn.stream()
                  .map(
                      tableColumn -> {
                        StringBuilder rowSqlBuilder = new StringBuilder();
                        rowSqlBuilder
                            .append(tableColumn.getColumnName())
                            .append(" ")
                            .append(tableColumn.getSqlType())
                            .append(" ");
                        if (tableColumn.isPrimaryKey()) {
                          rowSqlBuilder.append("PRIMARY KEY").append(" ");
                        } else if (!tableColumn.isNullAble()) {
                          rowSqlBuilder.append("NOT NULL").append(" ");
                        }
                        return rowSqlBuilder.toString();
                      })
                  .collect(Collectors.joining(","))
              + ");";
      LogManager.getLogger().debug(FORGEMOD, "createTableSql:");
      LogManager.getLogger().debug(FORGEMOD, createTableSql);
      connection.createStatement().executeUpdate(createTableSql);
    }
    close(tableResultSet, null, connection);
  }

  /**
   * initialInternalDatabase
   *
   * @param resource h2 database resource path
   * @throws IOException if file copy fail
   */
  public static void initialInternalDatabase(String resource) throws IOException {
    InputStream resourceAsStream = Mineauth.class.getResourceAsStream(resource);
    File file = new File("mineauth");
    if (!file.exists()) {
      boolean mkdirFlag = file.mkdir();
      if (!mkdirFlag) {
        throw new DataRuntimeException("create mineauth initial database fail");
      }
    }
    file = new File("mineauth/internalDatabase.mv.db");
    if (!file.exists()) {
      LOGGER.info("Init h2 database for default config");
      boolean newFile = file.createNewFile();
      if (!newFile) {
        throw new DataRuntimeException("create mineauth initial database fail");
      }
      try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = resourceAsStream.read(buf)) > 0) {
          fileOutputStream.write(buf, 0, bytesRead);
        }
      }
    }
  }

  private static void tableColumnInit() {
    EntityMapper playerEntityMapper =
        MetadataUtils.getEntityMapperByTypeName(AuthPlayer.class.getTypeName());
    if (playerEntityMapper == null) {
      throw new DataRuntimeException(
          "Class metadata has not been initialized: " + AuthPlayer.class.getTypeName());
    }
    Set<TableColumn> playerTableColumn = new HashSet<>();

    playerTableColumn.add(
        new TableColumn(
            "id", "ID", new ColumnType("varchar", 36), false, true, String.class.getTypeName()));
    playerTableColumn.add(
        new TableColumn(
            "username",
            MineauthConfig.databaseConfig.columnUsername.get(),
            new ColumnType("VARCHAR", 40),
            true,
            String.class.getTypeName()));
    playerTableColumn.add(
        new TableColumn(
            "uuid",
            MineauthConfig.databaseConfig.columnUuid.get(),
            new ColumnType("VARCHAR", 40),
            false,
            String.class.getTypeName()));
    playerTableColumn.add(
        new TableColumn(
            "password",
            MineauthConfig.databaseConfig.columnPassword.get(),
            new ColumnType("VARCHAR", 40),
            false,
            String.class.getTypeName()));
    playerTableColumn.add(
        new TableColumn(
            "lastLogin",
            MineauthConfig.databaseConfig.columnLastLogin.get(),
            new ColumnType("TIMESTAMP"),
            true,
            LocalDateTime.class.getTypeName()));
    playerTableColumn.add(
        new TableColumn(
            "identifier",
            MineauthConfig.databaseConfig.columnIdentifier.get(),
            new ColumnType("VARCHAR", 40),
            true,
            String.class.getTypeName()));
    playerTableColumn.add(
        new TableColumn(
            "banned",
            MineauthConfig.databaseConfig.columnBanned.get(),
            new ColumnType("VARCHAR", 5),
            true,
            String.class.getTypeName()));
    playerEntityMapper.setTableColumns(playerTableColumn);
    playerEntityMapper.setTableName(MineauthConfig.databaseConfig.table.get());
  }

  public static <T extends BaseEntity> boolean updateById(T entity) {
    String id = entity.getId();
    if (StringUtils.isEmpty(id)) {
      throw new DataRuntimeException("no id found in parameter entity");
    }
    // avoid to build sql within 'id=xxxx'
    // todo new entity by copy properties
    entity = copyEntity(entity.getClass(), entity);
    entity.setId(null);
    String updateStatement = buildAssignmentStatement(entity);
    ExpansionAbleConnectionPool instance = ExpansionAbleConnectionPool.getInstance();
    Connection connection;
    try {
      EntityMapper entityMapper =
          MetadataUtils.getEntityMapperByTypeName(entity.getClass().getTypeName());
      if (entityMapper == null) {
        throw new DataRuntimeException(
            "Class metadata has not been initialized: " + AuthPlayer.class.getTypeName());
      }
      String tableName = entityMapper.getTableName();

      String sql = "update `" + tableName + "` " + updateStatement;
      connection = instance.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      int i = preparedStatement.executeUpdate();
      close(null, preparedStatement, connection);
      if (i == 1) {
        return true;
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }
    return false;
  }

  public static <T extends BaseEntity> T selectOne(T entity) {
    Class<? extends BaseEntity> aClass = entity.getClass();
    String whereStatement = buildWhereStatement(entity);
    ExpansionAbleConnectionPool instance = ExpansionAbleConnectionPool.getInstance();
    Connection connection;
    try {
      EntityMapper entityMapper =
          MetadataUtils.getEntityMapperByTypeName(entity.getClass().getTypeName());
      if (entityMapper == null) {
        throw new DataRuntimeException(
            "Class metadata has not been initialized: " + AuthPlayer.class.getTypeName());
      }
      String tableName = entityMapper.getTableName();

      String sql = "select * from `" + tableName + "` where " + whereStatement + " LIMIT 1";
      connection = instance.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet resultSet = preparedStatement.executeQuery();
      Map<String, String> fieldMap = new HashMap<>();
      if (resultSet.next()) {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
          fieldMap.put(metaData.getColumnLabel(i), resultSet.getString(i));
        }
        close(resultSet, preparedStatement, connection);
        return (T) mapperBy(aClass, fieldMap);
      } else {
        close(resultSet, preparedStatement, connection);
        return null;
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }
    return null;
  }

  public static <T extends BaseEntity> boolean insertOne(T entity) {
    entity.setId(UUID.randomUUID().toString());
    String insertStatement = buildAssignmentStatement(entity);
    ExpansionAbleConnectionPool instance = ExpansionAbleConnectionPool.getInstance();
    Connection connection;
    try {
      EntityMapper entityMapper =
          MetadataUtils.getEntityMapperByTypeName(entity.getClass().getTypeName());
      if (entityMapper == null) {
        throw new DataRuntimeException(
            "Class metadata has not been initialized: " + AuthPlayer.class.getTypeName());
      }
      String tableName = entityMapper.getTableName();

      String sql = "insert into `" + tableName + "` " + insertStatement;
      connection = instance.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      int i = preparedStatement.executeUpdate();
      close(null, preparedStatement, connection);
      if (i == 1) {
        return true;
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }
    return false;
  }

  public static <T extends BaseEntity> String buildWhereStatement(T entity) {
    List<String> wheres = getStatement(entity);
    return String.join(" AND ", wheres);
  }

  public static <T extends BaseEntity> String buildAssignmentStatement(T entity) {
    List<String> assignments = getStatement(entity);
    return "SET " + String.join(",", assignments);
  }

  private static <T extends BaseEntity> List<String> getStatement(T entity) {
    List<String> statementList = new ArrayList<>();
    String typeName = entity.getClass().getTypeName();
    EntityMapper entityMapperByTypeName = MetadataUtils.getEntityMapperByTypeName(typeName);
    if (entityMapperByTypeName == null || entityMapperByTypeName.getFields() == null) {
      throw new DataRuntimeException(
          "Class metadata has not been initialized: " + AuthPlayer.class.getTypeName());
    }
    Set<Field> fields = entityMapperByTypeName.getFields();
    Set<TableColumn> tableColumns = entityMapperByTypeName.getTableColumns();
    Map<String, TableColumn> tableColumnMap =
        tableColumns.parallelStream()
            .collect(Collectors.toMap(TableColumn::getAlias, tableColumn -> tableColumn));
    fields.forEach(
        field -> {
          String name = field.getName();
          try {
            field.setAccessible(true);
            Object o = field.get(entity);
            if (o != null) {
              statementList.add(
                  String.format(
                      "`%s`='%s'",
                      tableColumnMap.get(name).getColumnName(),
                      CONVERTER_MAP.get(field.getType().getTypeName()).parse(o)));
            }
          } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            LOGGER.debug(e.getMessage(), e);
          }
        });
    return statementList;
  }

  public static <T extends BaseEntity> T mapperBy(Class<T> tClass, Map<String, String> fieldMap) {
    try {
      T t = tClass.newInstance();
      EntityMapper entityMapperByTypeName =
          MetadataUtils.getEntityMapperByTypeName(tClass.getTypeName());
      if (entityMapperByTypeName == null || entityMapperByTypeName.getFields() == null) {
        return null;
      }
      Set<Field> fieldsByTypeName = entityMapperByTypeName.getFields();
      Map<String, String> aliasColumnMap =
          entityMapperByTypeName.getTableColumns().parallelStream()
              .collect(Collectors.toMap(TableColumn::getAlias, TableColumn::getColumnName));
      for (Field field : fieldsByTypeName) {
        Converter<?> converter = CONVERTER_MAP.get(field.getType().getTypeName());
        if (converter != null) {
          field.set(t, converter.valueOf(fieldMap.get(aliasColumnMap.get(field.getName()))));
        }
      }
      return t;
    } catch (InstantiationException | IllegalAccessException e) {
      LOGGER.error(e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }
    return null;
  }

  public static <T extends BaseEntity> T copyEntity(Class<T> tClass, T src) {
    final EntityMapper entityMapperByTypeName =
        MetadataUtils.getEntityMapperByTypeName(src.getClass().getTypeName());
    if (entityMapperByTypeName == null || entityMapperByTypeName.getFields() == null) {
      return null;
    }
    try {
      final T t = tClass.newInstance();
      entityMapperByTypeName
          .getFields()
          .forEach(
              field -> {
                try {
                  if (field.get(src) != null) {
                    field.set(t, field.get(src));
                  }
                } catch (IllegalAccessException e) {
                  e.printStackTrace();
                }
              });
      return t;
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void close(
      ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
    if (null != resultSet) {
      try {
        resultSet.close();
      } catch (Exception e) {
        LOGGER.error(e.getMessage());
        LOGGER.debug(e.getMessage(), e);
      }
    }
    if (null != preparedStatement) {
      try {
        preparedStatement.close();
      } catch (Exception e) {
        LOGGER.error(e.getMessage());
        LOGGER.debug(e.getMessage(), e);
      }
    }
    if (null != connection) {
      try {
        connection.close();
      } catch (Exception e) {
        LOGGER.error(e.getMessage());
        LOGGER.debug(e.getMessage(), e);
      }
    }
  }
}
