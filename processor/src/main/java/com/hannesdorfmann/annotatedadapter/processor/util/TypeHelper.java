package com.hannesdorfmann.annotatedadapter.processor.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Hannes Dorfmann
 */
public class TypeHelper {

  private Elements elements;
  private Types types;

  TypeHelper(Elements elements, Types types){
    this.elements = elements;
    this.types = types;
  }

  public boolean isOfType(Element element, String type) {
    return isOfType(element.asType(), type);
  }

  public boolean isOfType(TypeMirror typeMirror, String type) {
    return types.isAssignable(typeMirror, elements.getTypeElement(type).asType());
  }

  public String getPackageName(TypeElement type) {
    PackageElement pkg = elements.getPackageOf(type);
    if (!pkg.isUnnamed()) {
      return pkg.getQualifiedName().toString();
    } else {
      return ""; // Default package
    }
  }
}
