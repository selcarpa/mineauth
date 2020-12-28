package cn.aethli.mineauth.common.utils;

import cn.aethli.mineauth.common.model.EntityMapper;
import cn.aethli.mineauth.common.model.TableColumn;
import cn.aethli.mineauth.entity.BaseEntity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MetadataUtils {
  private static final Map<String, EntityMapper> ENTITY_MAPPER_MAP = new ConcurrentHashMap<>();

  /**
   * init metadata, to cache entity-field map exclude abstract class and non-extends BaseEntity
   *
   * @see cn.aethli.mineauth.entity.BaseEntity
   * @param packageName packageName to scan
   * @throws ClassNotFoundException if the class cannot be located
   * @throws IOException If I/O errors occur
   */
  public static void initMetadata(String packageName) throws IOException, ClassNotFoundException {
    List<Class<BaseEntity>> classes = getClasses(packageName);
    Field[] baseEntityFields = BaseEntity.class.getDeclaredFields();
    classes.forEach(
        aClass -> {
          if (!Modifier.isAbstract(aClass.getModifiers())
              && BaseEntity.class.isAssignableFrom(aClass)) {
            Set<Field> fields = new HashSet<>(Arrays.asList(aClass.getDeclaredFields()));
            fields.addAll(Arrays.asList(baseEntityFields));
            EntityMapper entityMapper = ENTITY_MAPPER_MAP.get(aClass.getTypeName());
            if (entityMapper == null) {
              entityMapper = new EntityMapper();
              entityMapper.setClassName(aClass.getTypeName());
              entityMapper.setFields(fields);
              ENTITY_MAPPER_MAP.put(aClass.getTypeName(), entityMapper);
            }
          }
        });
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

  /**
   * Scans all classes accessible from the context class loader which belong to the given package.
   *
   * @param packageName The base package
   * @return The classes
   * @throws ClassNotFoundException if the class cannot be located
   * @throws IOException If I/O errors occur
   */
  private static <T extends BaseEntity> List<Class<T>> getClasses(String packageName)
      throws ClassNotFoundException, IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }
    ArrayList<Class<T>> classes = new ArrayList<>();
    for (File directory : dirs) {
      classes.addAll(findClasses(directory, packageName));
    }
    return classes;
  }

  /**
   * Recursive method used to find all classes in a given directory.
   *
   * @param directory The base directory
   * @param packageName The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException if the class cannot be located
   */
  private static <T extends BaseEntity> List<Class<T>> findClasses(
      File directory, String packageName) throws ClassNotFoundException {
    List<Class<T>> classes = new ArrayList<>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (!file.isDirectory() && file.getName().endsWith(".class")) {
          classes.add(
              (Class<T>)
                  Class.forName(
                      packageName
                          + '.'
                          + file.getName().substring(0, file.getName().length() - 6)));
        }
      }
    }
    return classes;
  }
}
