package io.github.askmeagain.jdbiplugin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.askmeagain.jdbiplugin.SampleDataLoader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LogUtils {
  private static final ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  public static void logAsJson(Object obj) {
    log.info("Found object: {}", mapper.writeValueAsString(obj));
  }
}
