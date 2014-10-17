package com.hannesdorfmann.annotatedadapter.processor;

import dagger.ObjectGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * @author Hannes Dorfmann
 */
public class AdapterInfo {

  public enum AdapterType {
    SUPPORT_RECYCLER_VIEW, LIST_VIEW
  }

  private static final String HOLDERS_SUFFIX = "Holders";
  private static final String BINDER_SUFFIX = "Binder";
  private static final String DELEGATOR_SUFFIX = "AdapterDelegator";

  private List<ViewTypeInfo> viewTypes = new ArrayList<ViewTypeInfo>();
  private TypeElement adapterClass;
  private AdapterType adapterType;

  private AdapterInfo superAnnotatedAdapterClass = null;
  private boolean searchedForSuperAnnotatedAdapterClass = false;

  @Inject Elements elementUtils;

  public AdapterInfo(ObjectGraph graph, TypeElement adapterClass, AdapterType adapterType) {
    this.adapterClass = adapterClass;
    this.adapterType = adapterType;
    graph.inject(this);
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

  public String getQualifiedAdapterDelegatorClassName() {
    return getQualifiedAdapterClassName() + DELEGATOR_SUFFIX;
  }

  public String getQualifiedAdapterClassName() {
    return adapterClass.asType().toString();
  }

  public String getQualifiedBinderClassName() {
    return getQualifiedAdapterClassName() + BINDER_SUFFIX;
  }

  public String getAdapterClassName() {
    return adapterClass.getSimpleName().toString();
  }

  public AdapterType getAdapterType() {
    return adapterType;
  }

  public boolean isAbstractClass() {
    return adapterClass.getModifiers().contains(Modifier.ABSTRACT);
  }

  /**
   * Checks if this AnnotatedAdapter has another AnnotatedAdapter as super class. Therfore the
   * binder interface must extends from this one.
   */
  public AdapterInfo getAnnotatedAdapterSuperClass(Map<String, AdapterInfo> adaptersMap) {

    if (searchedForSuperAnnotatedAdapterClass) {
      return superAnnotatedAdapterClass;
    }

    searchedForSuperAnnotatedAdapterClass = true;

    // Search from bottom up along inheritance three for the fist Annotated adapter class we find
    TypeElement currentClass = adapterClass;

    while (currentClass != null) {
      TypeMirror currentMirror = currentClass.getSuperclass();

      if (currentMirror instanceof NoType || currentMirror.getKind() == TypeKind.NONE) {
        // java.lang.Object has been found, there is no other super class
        return null;
      }

      AdapterInfo superAdapter = adaptersMap.get(currentMirror.toString());
      if (superAdapter != null) {
        // "Cache" the found super class for performance reasons
        superAnnotatedAdapterClass = superAdapter;
        return superAnnotatedAdapterClass;
      }

      // Continue with the super class
      currentClass = elementUtils.getTypeElement(currentMirror.toString());
    }

    return null;
  }

  /**
   * Checks if this adapter inheritance from another annotated adapter
   */
  public boolean hasAnnotatedAdapterSuperClass(Map<String, AdapterInfo> adaptersMap) {
    return getAnnotatedAdapterSuperClass(adaptersMap) != null;
  }
}
