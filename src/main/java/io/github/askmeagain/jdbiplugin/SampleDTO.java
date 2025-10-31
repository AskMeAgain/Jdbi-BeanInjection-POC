package io.github.askmeagain.jdbiplugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Table("sample_table")
@Getter
@AllArgsConstructor
public class SampleDTO {

  @ColumnName("column_one")
  private String columnOne;
  @ColumnName("column_two")
  private String columnTwo;
  @ColumnName("column_three")
  private String columnThree;
}