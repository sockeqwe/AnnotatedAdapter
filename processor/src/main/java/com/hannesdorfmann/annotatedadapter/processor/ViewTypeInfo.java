package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import javax.lang.model.element.Element;

/**
 * Information about an ViewType (read from @ViewType annotation)
 *
 * @author Hannes Dorfmann
 */
public class ViewTypeInfo {

  private static final String VIEWHOLDER_SUFFIX = "ViewHolder";
  private static final String METHOD_PREFIX = "bind";

  private Element field;
  private ViewType annotation;

  public ViewTypeInfo(Element field, ViewType annotation) {
    this.field = field;
    this.annotation = annotation;
  }

  public String getFieldName() {
    return field.getSimpleName().toString();
  }

  public String getViewHolderClassName() {
    return field.getSimpleName().toString() + VIEWHOLDER_SUFFIX;
  }

  public String getBinderMethodName() {
    return METHOD_PREFIX + getViewHolderClassName();
  }

  public Field[] getFields() {
    return annotation.fields();
  }

  public Class<?> getModelClass() {
    if (annotation.model().length == 0) {
      return null;
    }

    return annotation.model()[0];
  }

  public String getQualifiedModelClass(){
    return getModelClass().getCanonicalName();
  }

  public boolean hasModelClass() {
    return getModelClass() != null;
  }

  public int getLayoutRes(){
    return annotation.layout();
  }
}
