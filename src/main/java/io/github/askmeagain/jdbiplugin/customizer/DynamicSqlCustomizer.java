package io.github.askmeagain.jdbiplugin.customizer;

import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizer;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class DynamicSqlCustomizer implements SqlStatementCustomizerFactory {

  @Override
  public SqlStatementCustomizer createForMethod(java.lang.annotation.Annotation annotation, Class<?> sqlObjectType, Method method) {
    return stmt -> {
      Parameter[] params = method.getParameters();
      for (int i = 0; i < params.length; i++) {
        if (params[i].getType() == String.class) {
          var binding = stmt.getContext().getBinding();
          //Object sqlValue = binding.findForName("arg" + i);
          //stmt.define("sql", Objects.toString(sqlValue));
          break;
        }
      }
    };
  }
}