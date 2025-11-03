package io.github.askmeagain.jdbiplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.askmeagain.jdbiplugin.entity.ChildEntity;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.entity.TestId;
import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import io.github.askmeagain.jdbiplugin.service.TransactionService;
import io.github.askmeagain.jdbiplugin.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

  private static final String INSERT_QUERY = "INSERT INTO sample_table (id, column_one, column_two, column_three, json_data) VALUES (:id, :columnOne, :columnTwo, :columnThree, cast(:columnMap as jsonb))";
  private static final String SELECT_QUERY = "SELECT * FROM sample_table WHERE id = :id";
  private static final String SELECT_QUERY_PROJECTION = "SELECT column_one, json_data FROM sample_table WHERE id = :id AND column_one = :columnOne";

  public static final SampleEntity ENTITY_1 = new SampleEntity(UUID.randomUUID(), List.of(SampleEntity.SampleEnum.ANOTHER_TEST), "value2", new TestId("value3"), Map.of("key", "value"));
  public static final ChildEntity CHILD_1 = new ChildEntity(UUID.randomUUID(), ENTITY_1.getId(), "value2");
  public static final SampleEntity ENTITY_2 = new SampleEntity(UUID.randomUUID(), List.of(SampleEntity.SampleEnum.TEST), "value2", new TestId("value3"), Map.of("key", "value"));
  public static final ChildEntity CHILD_2 = new ChildEntity(UUID.randomUUID(), ENTITY_2.getId(), "value2");
  public static final SampleEntity ENTITY_3 = new SampleEntity(UUID.randomUUID(), List.of(SampleEntity.SampleEnum.ANOTHER_TEST), "value2", new TestId("value3"), Map.of("key", "value"));
  public static final SampleEntity ENTITY_4 = new SampleEntity(UUID.randomUUID(), List.of(SampleEntity.SampleEnum.TEST), "value2", new TestId("value3"), Map.of("key", "value"));
  public static final SampleEntity ENTITY_4_UPDATED = new SampleEntity(ENTITY_4.getId(), List.of(SampleEntity.SampleEnum.ANOTHER_TEST), "updated", new TestId("updated"), Map.of("key", "updated"));


  private final Jdbi jdbi;

  private final TransactionService transactionService;

  @Transaction
  @SneakyThrows
  @Override
  public void run(String... args) {

    var queryParams = Map.<String, Object>of(
      "id", ENTITY_1.getId(),
      "columnOne", new ArrayList<>(ENTITY_1.getColumnOne()),
      "columnTwo", ENTITY_1.getColumnTwo(),
      "columnThree", ENTITY_1.getColumnThree(),
      "columnMap", new ObjectMapper().writeValueAsString(Map.of("key", "value"))
    );

    var dynamicSqlReadProjectionParams = Map.<String, Object>of("id", ENTITY_2.getId(), "columnOne", List.of(SampleEntity.SampleEnum.TEST));

    var repository = jdbi.onDemand(SampleRepository.class);

    //manual insertion
    repository.manualInsert(ENTITY_2);

    //simple dynamic sql and insertion
    repository.dynamicSql(INSERT_QUERY, queryParams);

    //simple insertion
    transactionService.insertFullBeans();

    //update
    repository.updateFullBean(ENTITY_4_UPDATED);

    //find single
    repository.findById(ENTITY_3.getId()).ifPresent(LogUtils::logAsJson);
    repository.findById(UUID.randomUUID()).ifPresent(LogUtils::logAsJson);

    //dynamic sql with dynamic parameters
    repository.dynamicSqlRead(SELECT_QUERY, Map.of("id", ENTITY_3.getId()));

    //dynamic sql with projection
    repository.dynamicSqlReadProjection(SELECT_QUERY_PROJECTION, dynamicSqlReadProjectionParams);

    //join via join row
    repository.joinViaJoinRow();

    //join via reducer
    repository.joinViaReducer();

    log.info("Everything worked!");
  }
}
