package io.github.askmeagain.jdbiplugin.entity;

import io.github.askmeagain.jdbiplugin.common.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.json.Json;

import java.util.Map;

@Table("sample_table")
@Getter
@AllArgsConstructor
public class SampleEntity {

  @ColumnName("column_one")
  private String columnOne;
  @ColumnName("column_two")
  private String columnTwo;
  @ColumnName("column_three")
  private String columnThree;

  @Json
  @ColumnName("json_data")
  private Map<String, String> columnMap;
}