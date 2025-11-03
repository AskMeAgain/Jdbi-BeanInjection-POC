package io.github.askmeagain.jdbiplugin.converter;

import io.github.askmeagain.jdbiplugin.entity.TestId;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class TestIdConverter implements ColumnMapper<TestId>, ArgumentFactory {
  @Override
  public TestId map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
    String id = rs.getString(columnNumber);
    return id != null ? new TestId(id) : null;
  }

  @Override
  public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
    if (value instanceof TestId testId) {
      return Optional.of((pos, stmt, ctx) -> stmt.setString(pos, testId.getId()));
    }
    return Optional.empty();
  }
}