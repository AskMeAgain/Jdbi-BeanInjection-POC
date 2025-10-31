package io.github.askmeagain.jdbiplugin.entity;

import io.github.askmeagain.jdbiplugin.common.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.util.UUID;

@Table("child_table")
@Getter
@AllArgsConstructor
public class ChildEntity {

  @ColumnName("id")
  private UUID id;

  @ColumnName("sample_table_id")
  private UUID sampleTableId;

  @ColumnName("extra_column")
  private String extraColumn;
}