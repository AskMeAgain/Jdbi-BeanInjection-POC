package io.github.askmeagain.jdbiplugin;

import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

  private static final String INSERT_QUERY = "INSERT INTO sample_table (column_one, column_two, column_three) VALUES (:columnOne, :columnTwo, :columnThree)";
  private final Jdbi jdbi;

  @Override
  public void run(String... args) {
    var entity = new SampleEntity("value1", "value2", "value3", Map.of("key", "value"));

    jdbi.useExtension(SampleRepository.class, repository -> repository.manualInsert(entity));
    //jdbi.useExtension(SampleRepository.class, repository -> repository.insertFullBean(entity));

    var queryParams = Map.of(
      "columnOne", entity.getColumnOne(),
      "columnTwo", entity.getColumnTwo(),
      "columnThree", entity.getColumnThree(),
      "columnMap", Map.of("key", "value")
    );

    //jdbi.useExtension(SampleRepository.class, repository -> repository.dynamicSql(INSERT_QUERY, queryParams));

    log.info("Inserted sample DTO into database using JDBI!");
  }
}
