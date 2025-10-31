package io.github.askmeagain.jdbiplugin.customizer;

import io.github.askmeagain.jdbiplugin.common.Table;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizer;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public class InsertAllSqlStatementCustomizer implements SqlStatementCustomizerFactory {

  private final ConcurrentMap<Method, String> methodCache = new ConcurrentHashMap<>();
  private final ConcurrentMap<Class<?>, String> beanCache = new ConcurrentHashMap<>();

  @Override
  public SqlStatementCustomizer createForMethod(java.lang.annotation.Annotation annotation, Class<?> sqlObjectType, Method method) {
    return stmt -> {
      var params = method.getParameters();
      for (var param : params) {
        if (param.isAnnotationPresent(org.jdbi.v3.sqlobject.customizer.BindBean.class)) {
          var beanClass = param.getType();

          var insertSql = methodCache.computeIfAbsent(method, m -> beanCache.computeIfAbsent(beanClass, this::generateInsertSql));

          stmt.define("insert_all", insertSql);
          break;
        }
      }
    };
  }

  @SneakyThrows
  private String generateInsertSql(Class<?> beanClass) {
    var tableName = beanClass.getAnnotation(Table.class).value();

    var columnNames = Arrays.stream(beanClass.getDeclaredFields())
      .filter(x -> x.isAnnotationPresent(ColumnName.class))
      .map(x -> x.getAnnotation(ColumnName.class).value())
      .collect(Collectors.joining(", "));

    var fieldNames = Arrays.stream(beanClass.getDeclaredFields())
      .filter(x -> x.isAnnotationPresent(ColumnName.class))
      .map(field -> ":" + field.getName())
      .collect(Collectors.joining(", "));

    return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnNames, fieldNames);
  }
}