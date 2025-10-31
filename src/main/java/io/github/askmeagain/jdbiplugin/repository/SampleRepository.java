package io.github.askmeagain.jdbiplugin.repository;

import io.github.askmeagain.jdbiplugin.common.DynamicSql;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.common.InsertBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Map;

@RegisterBeanMapper(SampleEntity.class)
public interface SampleRepository {

  @InsertBean
  @SqlUpdate("<insert_all>")
  void insertFullBean(@BindBean SampleEntity dto);

  @DynamicSql
  @SqlUpdate("<sql>")
  void dynamicSql(@Define("sql") String sql, @BindMap Map<String, Object> params);
}