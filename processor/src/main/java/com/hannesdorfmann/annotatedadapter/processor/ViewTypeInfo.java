package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import java.util.ArrayList;
import java.util.List;
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
  private List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>(6);

  public ViewTypeInfo(Element field, ViewType annotation) {
    this.field = field;
    this.annotation = annotation;

    for (Field f : annotation.fields()) {
      fieldInfos.add(new FieldInfo(f));
    }
  }

  public String getFieldName() {
    return field.getSimpleName().toString();
  }

  public String getViewHolderClassName() {
    String name = field.getSimpleName().toString() + VIEWHOLDER_SUFFIX;
    char first = Character.toUpperCase(name.charAt(0));
    name = first + name.substring(1);
    return name;
  }

  public String getBinderMethodName() {
    return METHOD_PREFIX + getViewHolderClassName();
  }

  public List<FieldInfo> getFields() {
    return fieldInfos;
  }

  /*
  public Class<?> getModelClass() {
    if (annotation.model().length == 0) {
      return null;
    }

    return annotation.model()[0];
  }

  public String getQualifiedModelClass() {
    return getModelClass().getCanonicalName();
  }

  public boolean hasModelClass() {
    return getModelClass() != null;
  }
  */

  public int getLayoutRes() {
    return annotation.layout();
  }
}
