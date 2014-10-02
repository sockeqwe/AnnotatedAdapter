package com.hannesdorfmann.annotatedadapter.annotation;

/**
 * @author Hannes Dorfmann
 */
public @interface Field {

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
