package cn.aethli.mineauth.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

/**
 * simple connection pool
 *
 * @author selcaNyan
 */
public class ExpansionAbleConnectionPool implements DataSource {

  private static final ConcurrentLinkedDeque<Connection> POOL = new ConcurrentLinkedDeque<>();
  private static ExpansionAbleConnectionPool connectionPool = null;
  private static boolean initFlag = false;
  private static String version;

  private static String url;
  private static String user;
  private static String password;

  /** hidden construct method */
  private ExpansionAbleConnectionPool() {}

  /**
   * singleton instance
   *
   * @return singleton instance
   */
  public static synchronized ExpansionAbleConnectionPool getInstance() {
    if (!initFlag) {
      throw new RuntimeException("can not invoke getInstance before init");
    }
    if (connectionPool == null) {
      connectionPool = new ExpansionAbleConnectionPool();
    }
    return connectionPool;
  }

  public static void print() {
    System.out.println("connection pool size:\t" + POOL.size());
  }

  /**
   * init database connection pool
   *
   * @param driver database driver full name
   * @param url database url within a schema name
   * @param user database user
   * @param password password
   */
  public static void init(String driver, String url, String user, String password, int poolSize)
      throws SQLException, ClassNotFoundException {

    ExpansionAbleConnectionPool.url = url;
    ExpansionAbleConnectionPool.user = user;
    ExpansionAbleConnectionPool.password = password;

    // init driver
    Class.forName(driver);
    for (int i = 0; i < poolSize; i++) {
      Connection conn = DriverManager.getConnection(url, user, password);
      POOL.add(conn);
    }
    initFlag = true;
    version = UUID.randomUUID().toString();
  }

  /**
   * get connection from pool
   *
   * @return a database connection
   */
  public Connection getConnection() throws SQLException {
    if (POOL.size() > 0) {
      String v = version;
      final Connection connection = POOL.removeFirst();
      return (Connection)
          Proxy.newProxyInstance(
              ExpansionAbleConnectionPool.class.getClassLoader(),
              connection.getClass().getInterfaces(),
              (proxy, method, args) -> {
                if (!"close".equals(method.getName())) {
                  return method.invoke(connection, args);
                } else {
                  addConnection(connection, v);
                  return null;
                }
              });
    } else { // when there's no idle connection got a non-proxy connection
      return DriverManager.getConnection(url, user, password);
    }
  }

  private void addConnection(Connection connection,String v) throws SQLException {
    if (version.equals(v)){
      POOL.add(connection);
      System.out.println("come back");
    }else {
      connection.close();
      System.out.println("let it go");
    }
  }


  @Override
  public Connection getConnection(String username, String password) {
    return null;
  }

  @Override
  public <T> T unwrap(Class<T> iface) {
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) {
    return false;
  }

  @Override
  public PrintWriter getLogWriter() {
    return null;
  }

  @Override
  public void setLogWriter(PrintWriter out) {}

  @Override
  public int getLoginTimeout() {
    return 0;
  }

  @Override
  public void setLoginTimeout(int seconds) {}

  @Override
  public Logger getParentLogger() {
    return null;
  }
}
