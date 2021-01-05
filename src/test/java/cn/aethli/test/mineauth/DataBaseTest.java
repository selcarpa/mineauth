package cn.aethli.test.mineauth;

import cn.aethli.mineauth.Mineauth;
import cn.aethli.mineauth.common.utils.DataUtils;
import cn.aethli.mineauth.common.utils.MetadataUtils;
import cn.aethli.mineauth.datasource.ExpansionAbleConnectionPool;
import cn.aethli.mineauth.entity.AuthPlayer;
import cn.aethli.mineauth.exception.DataRuntimeException;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataBaseTest {

  private static String id;

  @Test
  @DisplayName("databaseInit")
  @Order(1)
  public void databaseInit() throws SQLException, IOException, ClassNotFoundException {
    copyDatabase();
    ExpansionAbleConnectionPool.init(
        "org.h2.Driver",
        "jdbc:h2:file:./mineauth_test/internalDatabase;SCHEMA=MINEAUTH;AUTO_SERVER=TRUE",
        "root",
        "admin",
        2);
    MetadataUtils.initMetadata();
    DataUtils.DatabaseInit();
  }

  // cause h2 driver is working, cannot delete test database
  @Test
  @DisplayName("databaseDelete")
  @Order(1000)
  @Disabled
  public void databaseDelete() throws IOException {
    File file = new File("mineauth_test");
    FileUtils.deleteDirectory(file);
  }

  public void copyDatabase() throws IOException {
    InputStream resourceAsStream =
        Mineauth.class.getResourceAsStream(Mineauth.DEFAULT_H2_DATABASE_FILE_RESOURCE_PATH);
    File file = new File("mineauth_test");
    if (!file.exists()) {
      boolean mkdirFlag = file.mkdir();
      if (!mkdirFlag) {
        throw new DataRuntimeException("create mineauth initial database fail");
      }
    }
    file = new File("mineauth_test/internalDatabase.mv.db");
    if (!file.exists()) {
      boolean newFile = file.createNewFile();
      if (!newFile) {
        throw new DataRuntimeException("create mineauth initial database fail");
      }
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      byte[] buf = new byte[1024];
      int bytesRead;
      while ((bytesRead = resourceAsStream.read(buf)) > 0) {
        fileOutputStream.write(buf, 0, bytesRead);
      }
    }
  }


  @Test
  @DisplayName("insertOnePlayer")
  @Order(101)
  public void insertOnePlayer() {
    AuthPlayer authPlayer = new AuthPlayer();
    authPlayer.setBanned(false);
    authPlayer.setPassword("123131231");
    authPlayer.setPassword(DigestUtils.md5Hex(authPlayer.getPassword()));
    authPlayer.setUuid(UUID.randomUUID().toString());
    authPlayer.setLastLogin(LocalDateTime.now());
    boolean b = DataUtils.insertOne(authPlayer);
    id = authPlayer.getId();
    selectOnePlayer();
  }

  public void selectOnePlayer() {
    AuthPlayer authPlayer = new AuthPlayer();
    authPlayer.setId(id);
    AuthPlayer selectOne = DataUtils.selectOne(authPlayer);
    Gson gson = new Gson();
    System.out.println(gson.toJson(selectOne));
  }

  @Test
  @DisplayName("updateOnePlayer")
  @Order(102)
  public void updateOnePlayer() {
    AuthPlayer authPlayer = new AuthPlayer();
    authPlayer.setId(id);
    authPlayer.setBanned(true);
    authPlayer.setPassword("46431");
    authPlayer.setUuid(UUID.randomUUID().toString());
    authPlayer.setLastLogin(LocalDateTime.now());
    boolean b = DataUtils.updateById(authPlayer);
    selectOnePlayer();
  }

  @Test
  @Order(201)
  @DisplayName("connectionPoolSwitchDatabase")
  public void connectionPoolSwitchDatabase() throws SQLException, ClassNotFoundException {
    List<Connection> connections = new ArrayList<>();
    ExpansionAbleConnectionPool instance = ExpansionAbleConnectionPool.getInstance();
    for (int i = 0; i <= 5; i++) {
      connections.add(instance.getConnection());
    }
    ExpansionAbleConnectionPool.init(
            "org.h2.Driver",
            "jdbc:h2:file:./mineauth_test/internalDatabase;SCHEMA=MINEAUTH;AUTO_SERVER=TRUE",
            "root",
            "admin",
            2);
    for (Connection connection : connections) {
      connection.close();
    }
  }
}
