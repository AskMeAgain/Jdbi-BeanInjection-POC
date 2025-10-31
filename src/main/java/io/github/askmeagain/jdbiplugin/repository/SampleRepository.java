package io.github.askmeagain.jdbiplugin.repository;

import io.github.askmeagain.jdbiplugin.common.DynamicSql;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.common.InsertBean;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Map;
import java.util.Optional;

@RegisterConstructorMapper(SampleEntity.class)
public interface SampleRepository {

  @SqlQuery("SELECT * FROM sample_table WHERE id = :id")
  Optional<SampleEntity> findById(int id);

  @InsertBean
  @SqlUpdate("<insert_all>")
  void insertFullBean(@BindBean SampleEntity dto);

  @SqlUpdate("INSERT INTO sample_table (column_one, column_two, column_three, json_data) VALUES (:columnOne, :columnTwo, :columnThree, :columnMap)")
  void manualInsert(@BindBean SampleEntity dto);

  @DynamicSql
  @SqlUpdate("<sql>")
  void dynamicSql(@Define("sql") String sql, @BindMap Map<String, Object> params);
}