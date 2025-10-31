package io.github.askmeagain.jdbiplugin.reducer;

import io.github.askmeagain.jdbiplugin.entity.ChildEntity;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Map;
import java.util.UUID;

public class SampleChildReducer implements LinkedHashMapRowReducer<UUID, SampleEntity> {

  @Override
  public void accumulate(Map<UUID, SampleEntity> map, RowView rowView) {
    var f = map.computeIfAbsent(rowView.getColumn("s_id", UUID.class),
      id -> rowView.getRow(SampleEntity.class));

    if (rowView.getColumn("c_id", UUID.class) != null) {
      f.getChilds().add(rowView.getRow(ChildEntity.class));
    }
  }
}