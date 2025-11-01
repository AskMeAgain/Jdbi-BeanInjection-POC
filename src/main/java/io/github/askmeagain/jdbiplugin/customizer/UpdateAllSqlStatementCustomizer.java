package io.github.askmeagain.jdbiplugin.customizer;

import io.github.askmeagain.jdbiplugin.common.Table;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizer;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public class UpdateAllSqlStatementCustomizer implements SqlStatementCustomizerFactory {

  private final ConcurrentMap<Class<?>, String> beanCache = new ConcurrentHashMap<>();

  @Override
  public SqlStatementCustomizer createForMethod(java.lang.annotation.Annotation annotation, Class<?> sqlObjectType, Method method) {
    return stmt -> {
      var params = method.getParameters();
      for (var param : params) {
        if (param.isAnnotationPresent(org.jdbi.v3.sqlobject.customizer.BindBean.class)) {
          var beanClass = param.getType();

          var updateSql = beanCache.computeIfAbsent(beanClass, this::generateUpdateSql);

          stmt.define("update_all", updateSql);
          break;
        }
      }
    };
  }

  @SneakyThrows
  private String generateUpdateSql(Class<?> beanClass) {
    var tableName = beanClass.getAnnotation(Table.class).value();

    var setClause = Arrays.stream(beanClass.getDeclaredFields())
      .filter(x -> x.isAnnotationPresent(ColumnName.class))
      .map(f -> f.getAnnotation(ColumnName.class).value() + " = :" + f.getName())
      .collect(Collectors.joining(", "));

    return String.format("UPDATE %s SET %s WHERE %s = :%s",
      tableName, setClause, "id", "id");
  }
}