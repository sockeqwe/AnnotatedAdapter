package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import javax.lang.model.element.Element;

/**
 * Information about an ViewType (read from @ViewType annotation)
 *
 * @author Hannes Dorfmann
 */
public class ViewTypeInfo {

  private Element field;
  private ViewType annotation;

  public ViewTypeInfo(Element field, ViewType annotation) {
    this.field = field;
    this.annotation = annotation;
  }
}
