package io.github.askmeagain.jdbiplugin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.json.Json;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ProjectionDto {

  @ColumnName("column_one")
  private String columnOne;

  @Json
  @ColumnName("json_data")
  private Map<String, String> columnMap;
}