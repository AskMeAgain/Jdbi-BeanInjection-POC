package io.github.askmeagain.jdbiplugin;

import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class JdbiPluginApplication {

  public static void main(String[] args) {
    SpringApplication.run(JdbiPluginApplication.class, args);
  }

  @Bean
  public Jdbi jdbi(DataSource dataSource) {
    return Jdbi
      .create(dataSource)
      .installPlugin(new SqlObjectPlugin());
  }

  @Component
  @RequiredArgsConstructor
  public static class SampleDataLoader implements CommandLineRunner {

    private static final String INSERT_QUERY = "INSERT INTO sample_table (column_one, column_two, column_three) VALUES (:columnOne, :columnTwo, :columnThree)";
    private final Jdbi jdbi;

    @Override
    public void run(String... args) {
      var entity = new SampleEntity("value1", "value2", "value3");

      jdbi.useExtension(SampleRepository.class, repository -> repository.insertFullBean(entity));

      var queryParams = Map.<String, Object>of(
        "columnOne", entity.getColumnOne(),
        "columnTwo", entity.getColumnTwo(),
        "columnThree", entity.getColumnThree()
      );

      jdbi.useExtension(SampleRepository.class, repository -> repository.dynamicSql(INSERT_QUERY, queryParams));

      log.info("Inserted sample DTO into database using JDBI!");
    }
  }
}
