package com.hannesdorfmann.annotatedadapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate integer fields with that annotation
 *
 * @author Hannes Dorfmann
 */

@Target(ElementType.FIELD) @Retention(RetentionPolicy.CLASS) @Documented
public @interface ViewType {

  /**
   * The layout resource
   */
  int layout();

  /**
   * The fields in the layout
   */
  Field[] fields() default { };

  /*
   * By providing a model the data will automatically be casted into this one and passed as
   * parameter into the corresponding binder interface. Only one model class is allowed.
   ≈
  Class<?>[] model() default { };
  */
}
