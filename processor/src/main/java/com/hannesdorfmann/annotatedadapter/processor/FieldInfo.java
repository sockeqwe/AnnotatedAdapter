package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import javax.lang.model.type.MirroredTypeException;

/**
 * @author Hannes Dorfmann
 */
public class FieldInfo {

  private String qualifiedClassName;
  private String fieldName;
  private int id;


  public FieldInfo(Field field){
    fieldName = field.name();
    id = field.id();

    try
    {
      qualifiedClassName = field.type().getCanonicalName();
    }
    catch( MirroredTypeException mte )
    {
      qualifiedClassName = mte.getTypeMirror().toString();
    }

  }

  public String getQualifiedClassName() {
    return qualifiedClassName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public int getId() {
    return id;
  }
}
