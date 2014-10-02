package com.hannesdorfmann.annotatedadapter.annotation;

/**
 * Annotate integer fields with that annotation
 *
 * @author Hannes Dorfmann
 */
public @interface ViewType {

  /**
   * The layout resource
   */
  int layout();

  /**
   * The fields in the layout
   */
  Field[] fields() default { };
}
