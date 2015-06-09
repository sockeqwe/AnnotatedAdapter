package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import javax.lang.model.type.MirroredTypeException;

/**
 * @author Hannes Dorfmann
 */
public class ViewInfo {

  private String qualifiedClassName;
  private String fieldName;
  private int id;


  public ViewInfo(ViewField viewField){
    fieldName = viewField.name();
    id = viewField.id();

    try
    {
      qualifiedClassName = viewField.type().getCanonicalName();
    }
    catch( MirroredTypeException mte )
    {
      qualifiedClassName = mte.getTypeMirror().toString();
    }

  }

  public String getQualifiedClassName() {
    return qualifiedClassName;
  }

  public String getViewFieldName() {
    return fieldName;
  }

  public int getId() {
    return id;
  }
}
