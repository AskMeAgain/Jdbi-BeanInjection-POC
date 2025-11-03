package io.github.askmeagain.jdbiplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.spring.EnableJdbiRepositories;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@EnableJdbiRepositories
@SpringBootApplication
public class JdbiPluginApplication {

  public static void main(String[] args) {
    SpringApplication.run(JdbiPluginApplication.class, args);
  }

  @Bean
  public Jdbi jdbi(DataSource dataSource, List<? extends ColumnMapper> allColumnMappers, List<? extends ArgumentFactory> allArgumentFactories) {
    var proxy = new TransactionAwareDataSourceProxy(dataSource);
    var jdbi = Jdbi
      .create(proxy)
      .installPlugin(new SqlObjectPlugin())
      .installPlugin(new Jackson2Plugin())
      .installPlugin(new PostgresPlugin());

    allColumnMappers.forEach(jdbi::registerColumnMapper);
    allArgumentFactories.forEach(jdbi::registerArgument);

    jdbi.getConfig(Jackson2Config.class).setMapper(new ObjectMapper());

    return jdbi;
  }
}
