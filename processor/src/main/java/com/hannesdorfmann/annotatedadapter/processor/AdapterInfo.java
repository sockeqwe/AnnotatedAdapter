package com.hannesdorfmann.annotatedadapter.processor;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.TypeElement;

/**
 * @author Hannes Dorfmann
 */
public class AdapterInfo {

  public enum AdapterType {
    RECYCLER_VIEW, LIST_VIEW
  }

  private static final String HOLDERS_SUFFIX = "Holders";
  private static final String BINDER_SUFFIX = "Binder";
  private static final String DELEGATOR_SUFFIX = "AdapterDelegator";

  private List<ViewTypeInfo> viewTypes = new ArrayList<ViewTypeInfo>();
  private TypeElement adapterClass;
  private AdapterType adapterType;

  public AdapterInfo(TypeElement adapterClass, AdapterType adapterType) {
    this.adapterClass = adapterClass;
    this.adapterType = adapterType;
  }

  public void addViewTypeInfo(ViewTypeInfo i) {
    viewTypes.add(i);
  }

  public List<ViewTypeInfo> getViewTypes() {
    return viewTypes;
  }

  public TypeElement getAdapterClass() {
    return adapterClass;
  }

  public String getViewHoldersClassName() {
    return adapterClass.getSimpleName() + HOLDERS_SUFFIX;
  }

  public String getBinderClassName() {
    return adapterClass.getSimpleName() + BINDER_SUFFIX;
  }

  public String getAdapterDelegatorClassName() {
    return adapterClass.getSimpleName() + DELEGATOR_SUFFIX;
  }
}
