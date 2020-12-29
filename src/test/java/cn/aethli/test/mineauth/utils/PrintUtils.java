package cn.aethli.test.mineauth.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PrintUtils {

  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
  public static void jsonPrint(Object o) {
    System.out.println(gson.toJson(o));
  }
}
