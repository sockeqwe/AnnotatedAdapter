package com.hannesdorfmann.annotatedadapter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate integer fields with that annotation to mark them as ViewType
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
   * The UI view-fields for the view holder class. This view fields will be filled with Views
   * inheriting from
   * android.view.View that are automatically retrieved with <code>findViewById()</code>
   *
   * @see ViewField
   * @see #fields()
   */
  ViewField[] views() default { };

  /**
   * Define some additional fields for the viewholders class that are NOT UI View fields (not binded
   * with <code>findViewById()</code>).
   *
   * @see Field
   * @see #views()
   */
  Field[] fields() default { };

  /**
   * Should a init method be generated for this view holder (part of the binder interface) whrere
   * you can initialize the new created viewholder by setting view properties by hand (rather than
   * in xml layout)
   */
  boolean initMethod() default false;

  /**
   * if true (default value), annotated checks at compile time if the integer value of the view
   * type
   * is unique (including inheritance). Set it to <i>false</i> if and only if you have very good
   * reasons to do that like overriding the inflated xml layout.
   */
  boolean checkValue() default true;

  /*
   * By providing a model the data will automatically be casted into this one and passed as
   * parameter into the corresponding binder interface. Only one model class is allowed.
   ≈
  Class<?>[] model() default { };
  */
}
