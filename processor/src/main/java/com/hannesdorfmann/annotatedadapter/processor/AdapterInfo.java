package com.hannesdorfmann.annotatedadapter.processor;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;

/**
 * @author Hannes Dorfmann
 */
public class AdapterInfo {


  public enum AdapterType {
    RECYCLER_VIEW, LIST_VIEW
  }

  private List<ViewTypeInfo> viewTypes = new ArrayList<ViewTypeInfo>();
  private Element adapterClass;
  private AdapterType adapterType;

  public AdapterInfo(Element adapterClass, AdapterType adapterType) {
    this.adapterClass = adapterClass;
    this.adapterType = adapterType;
  }

  public void addViewTypeInfo(ViewTypeInfo i){
    viewTypes.add(i);
  }

}
