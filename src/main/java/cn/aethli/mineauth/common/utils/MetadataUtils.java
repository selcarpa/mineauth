package cn.aethli.mineauth.common.utils;

import cn.aethli.mineauth.common.model.EntityMapper;
import cn.aethli.mineauth.entity.AuthPlayer;
import cn.aethli.mineauth.entity.BaseEntity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class MetadataUtils {
  private static final Map<String, EntityMapper> ENTITY_MAPPER_MAP = new ConcurrentHashMap<>();
  private static final AtomicBoolean initFlag = new AtomicBoolean(false);

  public static void initMetadata() {
    synchronized (initFlag) {
      if (initFlag.get()) {
        return;
      }
      Field[] baseEntityFields = BaseEntity.class.getDeclaredFields();
      HashSet<Field> fields = new HashSet<>(Arrays.asList(AuthPlayer.class.getDeclaredFields()));
      fields.addAll(Arrays.asList(baseEntityFields));
      EntityMapper entityMapper = ENTITY_MAPPER_MAP.get(AuthPlayer.class.getTypeName());
      if (entityMapper == null) {
        entityMapper = new EntityMapper();
        entityMapper.setClassName(AuthPlayer.class.getTypeName());
        entityMapper.setFields(fields);
        ENTITY_MAPPER_MAP.put(AuthPlayer.class.getTypeName(), entityMapper);
      }
      initFlag.set(true);
    }
  }

  /**
   * getEntityMapperByTypeName
   *
   * @param typeName className within packageName
   * @return class entity mapper
   */
  public static EntityMapper getEntityMapperByTypeName(String typeName) {
    return ENTITY_MAPPER_MAP.get(typeName);
  }
}
