package io.github.askmeagain.jdbiplugin;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(SampleDTO.class)
public interface SampleRepository {

  @InsertAll
  @SqlUpdate("<insert_all>")
  void insert(@BindBean SampleDTO dto);
}