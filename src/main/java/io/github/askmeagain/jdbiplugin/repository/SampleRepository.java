package io.github.askmeagain.jdbiplugin.repository;

import io.github.askmeagain.jdbiplugin.common.InsertBean;
import io.github.askmeagain.jdbiplugin.common.UpdateBean;
import io.github.askmeagain.jdbiplugin.entity.ChildEntity;
import io.github.askmeagain.jdbiplugin.entity.ProjectionDto;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.reducer.SampleChildReducer;
import org.jdbi.v3.core.mapper.JoinRow;
import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.GenerateSqlObject;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.config.RegisterJoinRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.*;
import java.util.stream.Collectors;

@GenerateSqlObject
@JdbiRepository
@RegisterConstructorMapper(ProjectionDto.class)
@RegisterConstructorMapper(value = SampleEntity.class)
@RegisterConstructorMapper(value = ChildEntity.class)
public interface SampleRepository {

  @SqlQuery("SELECT * FROM sample_table s WHERE id = :id")
  Optional<SampleEntity> findById(UUID id);

  @InsertBean
  @SqlUpdate("<insert_all>")
  void insertFullBean(@BindBean SampleEntity dto);

  @UpdateBean
  @SqlUpdate("<update_all>")
  void updateFullBean(@BindBean SampleEntity dto);

  @InsertBean
  @SqlUpdate("<insert_all>")
  void insertFullBean(@BindBean ChildEntity dto);

  @SqlUpdate("INSERT INTO sample_table (id, column_one, column_two, column_three, json_data) VALUES (:id, :columnOne, :columnTwo, :columnThree, :columnMap)")
  void manualInsert(@BindBean SampleEntity dto);

  @SqlUpdate("<sql>")
  void dynamicSql(@Define("sql") String sql, @BindMap Map<String, Object> params);

  @SqlQuery("<sql>")
  List<SampleEntity> dynamicSqlRead(@Define("sql") String sql, @BindMap Map<String, Object> params);

  @SqlQuery("""
        SELECT
            s.id AS s_id,
            s.column_one AS s_column_one,
            s.column_two AS s_column_two,
            s.column_three AS s_column_three,
            s.json_data AS s_json_data,
            c.id AS c_id,
            c.sample_table_id AS c_sample_table_id,
            c.extra_column AS c_extra_column
        FROM sample_table s
        JOIN child_table c ON c.sample_table_id = s.id
    """)
  @RegisterConstructorMapper(value = SampleEntity.class, prefix = "s")
  @RegisterConstructorMapper(value = ChildEntity.class, prefix = "c")
  @RegisterJoinRowMapper({SampleEntity.class, ChildEntity.class})
  List<JoinRow> joinViaJoinRowInternal();

  @SqlQuery("""
        SELECT
            s.id AS s_id,
            s.column_one AS s_column_one,
            s.column_two AS s_column_two,
            s.column_three AS s_column_three,
            s.json_data AS s_json_data,
            c.id AS c_id,
            c.sample_table_id AS c_sample_table_id,
            c.extra_column AS c_extra_column
        FROM sample_table s
        JOIN child_table c ON c.sample_table_id = s.id
    """)
  @UseRowReducer(SampleChildReducer.class)
  @RegisterConstructorMapper(value = SampleEntity.class, prefix = "s")
  @RegisterConstructorMapper(value = ChildEntity.class, prefix = "c")
  List<SampleEntity> joinViaReducer();

  default List<SampleEntity> joinViaJoinRow() {
    return joinViaJoinRowInternal().stream()
      .collect(Collectors.groupingBy(jr -> jr.get(SampleEntity.class), Collectors.mapping(jr -> jr.get(ChildEntity.class), Collectors.toList())))
      .entrySet()
      .stream()
      .peek(entry -> entry.getKey().getChilds().addAll(entry.getValue()))
      .map(Map.Entry::getKey)
      .toList();
  }

  @SqlQuery("SELECT txid_current()")
  long getCurrentTxId();

  @SqlQuery("<sql>")
  List<ProjectionDto> dynamicSqlReadProjection(@Define("sql") String sql, @BindMap Map<String, Object> params);
}