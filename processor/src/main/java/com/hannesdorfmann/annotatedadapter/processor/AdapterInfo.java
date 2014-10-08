package com.hannesdorfmann.annotatedadapter.processor;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;

/**
 * @author Hannes Dorfmann
 */
public class AdapterInfo {

  private List<ViewTypeInfo> viewTypes = new ArrayList<ViewTypeInfo>();
  private Element adapterClass;

  public AdapterInfo(Element adapterClass) {
    this.adapterClass = adapterClass;
  }

  public void addViewTypeInfo(ViewTypeInfo i){
    viewTypes.add(i);
  }
}
