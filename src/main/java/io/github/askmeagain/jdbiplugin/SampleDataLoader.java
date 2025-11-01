package io.github.askmeagain.jdbiplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.askmeagain.jdbiplugin.entity.ChildEntity;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import io.github.askmeagain.jdbiplugin.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

  private static final String INSERT_QUERY = "INSERT INTO sample_table (id, column_one, column_two, column_three, json_data) VALUES (:id, :columnOne, :columnTwo, :columnThree, cast(:columnMap as jsonb))";
  private static final String SELECT_QUERY = "SELECT * FROM sample_table WHERE id = :id";
  private static final String SELECT_QUERY_PROJECTION = "SELECT column_one, json_data FROM sample_table WHERE id = :id AND column_one = :columnOne";
  private final Jdbi jdbi;

  @SneakyThrows
  @Override
  public void run(String... args) {
    var entity1 = new SampleEntity(UUID.randomUUID(), SampleEntity.SampleEnum.ANOTHER_TEST, "value2", "value3", Map.of("key", "value"));
    var entity2 = new SampleEntity(UUID.randomUUID(), SampleEntity.SampleEnum.TEST, "value2", "value3", Map.of("key", "value"));
    var entity3 = new SampleEntity(UUID.randomUUID(), SampleEntity.SampleEnum.ANOTHER_TEST, "value2", "value3", Map.of("key", "value"));
    var entity4 = new SampleEntity(UUID.randomUUID(), SampleEntity.SampleEnum.TEST, "value2", "value3", Map.of("key", "value"));
    var entity4Updated = new SampleEntity(entity4.getId(), SampleEntity.SampleEnum.ANOTHER_TEST, "updated", "updated", Map.of("key", "updated"));

    var child1 = new ChildEntity(UUID.randomUUID(), entity1.getId(), "value2");
    var child2 = new ChildEntity(UUID.randomUUID(), entity2.getId(), "value2");

    var queryParams = Map.<String, Object>of(
      "id", entity1.getId(),
      "columnOne", entity1.getColumnOne(),
      "columnTwo", entity1.getColumnTwo(),
      "columnThree", entity1.getColumnThree(),
      "columnMap", new ObjectMapper().writeValueAsString(Map.of("key", "value"))
    );

    var dynamicSqlReadProjectionParams = Map.<String, Object>of("id", entity2.getId(), "columnOne", SampleEntity.SampleEnum.TEST);

    var repository = jdbi.onDemand(SampleRepository.class);

    //simple dynamic sql and insertion
    repository.dynamicSql(INSERT_QUERY, queryParams);

    //manual insertion
    repository.manualInsert(entity2);

    //simple insertion
    repository.insertFullBean(entity3);
    repository.insertFullBean(entity4);
    repository.insertFullBean(child1);
    repository.insertFullBean(child2);

    //update
    repository.updateFullBean(entity4Updated);

    //find single
    repository.findById(entity3.getId()).ifPresent(LogUtils::logAsJson);
    repository.findById(UUID.randomUUID()).ifPresent(LogUtils::logAsJson);

    //dynamic sql with dynamic parameters
    repository.dynamicSqlRead(SELECT_QUERY, Map.of("id", entity3.getId())).forEach(LogUtils::logAsJson);

    //dynamic sql with projection
    repository.dynamicSqlReadProjection(SELECT_QUERY_PROJECTION, dynamicSqlReadProjectionParams).forEach(LogUtils::logAsJson);

    //join via join row
    repository.joinViaJoinRow().forEach(LogUtils::logAsJson);

    //join via reducer
    repository.joinViaReducer().forEach(LogUtils::logAsJson);

    log.info("Everything worked!");
  }
}
