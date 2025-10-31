package io.github.askmeagain.jdbiplugin.common;

import io.github.askmeagain.jdbiplugin.customizer.DynamicSqlCustomizer;
import io.github.askmeagain.jdbiplugin.customizer.InsertAllSqlStatementCustomizer;
import org.jdbi.v3.sqlobject.customizer.SqlStatementCustomizingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@SqlStatementCustomizingAnnotation(DynamicSqlCustomizer.class)
public @interface DynamicSql {

}