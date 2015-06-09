package com.hannesdorfmann.annotatedadapter.annotation;

/**
 * Defines a UI (android.view.View) field in the viewholders class. The difference between this and
 * {@link Field} is that this annotation automatically generates the code for
 * <code>findViewById()</code>
 *
 * @author Hannes Dorfmann
 * @see Field
 * @since 1.1
 */
public @interface ViewField {

  /**
   * The type of the View
   */
  Class<? extends android.view.View> type();

  /**
   * The R.id. of the view widget
   */
  int id();

  /**
   * The name of the field
   */
  String name();
}
