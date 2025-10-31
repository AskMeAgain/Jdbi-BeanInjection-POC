package io.github.askmeagain.jdbiplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.askmeagain.jdbiplugin.entity.ChildEntity;
import io.github.askmeagain.jdbiplugin.entity.SampleEntity;
import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.json.JsonPlugin;
import org.jdbi.v3.json.internal.JsonArgumentFactory;
import org.jdbi.v3.postgres.PostgresPlugin;
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
    var jdbi = Jdbi
      .create(dataSource)
      .installPlugin(new SqlObjectPlugin())
      .installPlugin(new Jackson2Plugin())
      .installPlugin(new PostgresPlugin());

    jdbi.getConfig(Jackson2Config.class).setMapper(new ObjectMapper());

    return jdbi;
  }

}
