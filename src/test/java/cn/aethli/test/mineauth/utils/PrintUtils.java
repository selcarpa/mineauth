package cn.aethli.test.mineauth.utils;

import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrintUtils {

  private static final Gson gson;

  static {
    gson =
        new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(
                LocalDateTime.class,
                (JsonSerializer<LocalDateTime>)
                    (src, typeOfSrc, context) ->
                        new JsonPrimitive(
                            src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"))))
            .registerTypeAdapter(
                LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>)
                    (json, typeOfT, context) -> {
                      String datetime = json.getAsJsonPrimitive().getAsString();
                      return LocalDateTime.parse(
                          datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
                    })
            .create();
  }

  public static void jsonPrint(Object o) {
    System.out.println(gson.toJson(o));
  }
}
