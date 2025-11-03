package io.github.askmeagain.jdbiplugin.converter;

import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Component
public class EnumListArgumentFactory implements ArgumentFactory {
  @Override
  public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
    if (value instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Enum<?>) {
      return Optional.of((pos, stmt, ctx) -> {
        String[] names = list.stream()
          .map(e -> ((Enum<?>) e).name())
          .toArray(String[]::new);
        stmt.setArray(pos, stmt.getConnection().createArrayOf("text", names));
      });
    }
    return Optional.empty();
  }
}