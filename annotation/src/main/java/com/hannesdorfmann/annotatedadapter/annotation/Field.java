package com.hannesdorfmann.annotatedadapter.annotation;

/**
 * Defines a java field in the viewholders class. Can be of any arbitrary type. The difference
 * between this annoation and {@link ViewField} is that view does automatically findViewById().
 *
 * @author Hannes Dorfmann
 * @since 1.1
 * @see ViewField
 */
public @interface Field {

  /**
   * The type of the field
   */
  Class<?> type();

  /**
   * The name of the field
   */
  String name();
}
