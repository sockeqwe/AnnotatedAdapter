package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.processor.util.ProcessorMessage;
import com.hannesdorfmann.annotatedadapter.processor.util.TypeHelper;
import dagger.ObjectGraph;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Searches for @ViewTypes and collects data about annotated field and surrounding adapter classes
 *
 * @author Hannes Dorfmann
 */
public class ViewTypeSearcher {

  public static final String SUPPORT_RECYCLER_ADAPTER =
      "com.hannesdorfmann.annotatedadapter.recyclerview.SupportAnnotatedAdapter";

  @Inject ProcessorMessage logger;
  @Inject TypeHelper typeHelper;
  @Inject Elements elementUtils;
  @Inject Types typeUtils;

  private ObjectGraph graph;

  /**
   * Maps the
   */
  private Map<String, AdapterInfo> classMap = new LinkedHashMap<String, AdapterInfo>();

  public ViewTypeSearcher(ObjectGraph graph) {
    this.graph = graph;
    graph.inject(this);
  }

  public void addElement(Element field, ViewType annotation) {

    if (!isValidAnnotation(field, annotation)) {
      return;
    }

    if (isValidField(field) && isFieldInValidClass((VariableElement) field)) {

      TypeElement surroundingClass = (TypeElement) field.getEnclosingElement();
      AdapterInfo.AdapterType adapterType = isValidAdapterClass(surroundingClass);

      String className = surroundingClass.asType().toString();
      AdapterInfo adapterInfo = classMap.get(className);
      if (adapterInfo == null) {
        adapterInfo = new AdapterInfo(graph, surroundingClass, adapterType);
        classMap.put(className, adapterInfo);
      }
      adapterInfo.addViewTypeInfo(new ViewTypeInfo(field, annotation));
    }
  }

  private boolean isFieldInValidClass(VariableElement field) {

    if (field.getEnclosingElement() == null) {
      logger.error(field, "%s is not in a class", field.getSimpleName());
      return false;
    }

    if (field.getEnclosingElement().getKind() != ElementKind.CLASS) {
      logger.error(field, "Only fields in a class can be annotated with @%s (caused by %s in %s)",
          ViewType.class, field.getSimpleName(), field.getEnclosingElement().asType().toString());
      return false;
    }

    if (typeHelper.isOfType(field.getEnclosingElement(),
        AbsListViewAnnotatedAdapter.class.getCanonicalName())) {
      return true;
    }

    if (typeHelper.isOfType(field.getEnclosingElement(), SUPPORT_RECYCLER_ADAPTER)) {
      return true;
    }

    // Failing
    logger.error(field, "The class with @%s annotations must extend from %s or %s",
        ViewType.class.getSimpleName(), SUPPORT_RECYCLER_ADAPTER,
        AbsListViewAnnotatedAdapter.class.getCanonicalName());

    return false;
  }

  private boolean isValidField(Element element) {

    if (element.getKind() == ElementKind.FIELD) {

      // Check if its public
      if (!element.getModifiers().contains(Modifier.PUBLIC)) {
        logger.error(element, "%s in %s is not public. Only final integer fields with public "
                + "visibility can be annotated with @%s.", element.getSimpleName(),
            element.getEnclosingElement().getSimpleName(), ViewType.class.getSimpleName());
        return false;
      }

      // Check if its final
      if (!element.getModifiers().contains(Modifier.FINAL)) {
        logger.error(element,
            "%s in %s is not final. Only final integer fields can be annotated with @%s.",
            element.getSimpleName(), element.getEnclosingElement().getSimpleName(),
            ViewType.class.getSimpleName());
        return false;
      }

      // Check if its an int
      if (element.asType().getKind() != TypeKind.INT) {
        logger.error(element,
            "%s in %s is not a integer. Only final public integer fields can be annotated with @%s.",
            element.getSimpleName(), element.getEnclosingElement().getSimpleName(),
            ViewType.class.getSimpleName());

        return false;
      }

      // Everything is ok, its a valid integer
      return true;
    } else {
      logger.error(element, "@%s can only be applied on integer fields. %s is not a field",
          ViewType.class.getSimpleName(), element.getSimpleName());
      return false;
    }
  }

  private boolean isValidAnnotation(Element element, ViewType annotation) {

    if (annotation == null) { // Should never be the case --> useless check
      logger.error(element, "The annotation is null");
      return false;
    }
  /*
    if (annotation.model().length > 1) {
      logger.error(element,
          "The field %s in %s annotated with @%s has specified a more than one model. Please specify exactly one model or don't set this annotation",
          element.getSimpleName(), element.asType().toString(), ViewType.class.getSimpleName());
      return false;
    }

*/
    return true;
  }

  /**
   * Checks the type of the adapter
   *
   * @return null if its not a subclass of an Abslist adapter or RecyclerViewAdapter
   */
  private AdapterInfo.AdapterType isValidAdapterClass(TypeElement adapterClass) {

    TypeElement recyclerAdapter = elementUtils.getTypeElement(SUPPORT_RECYCLER_ADAPTER);
    if (typeUtils.isSubtype(adapterClass.asType(), recyclerAdapter.asType())) {
      return AdapterInfo.AdapterType.SUPPORT_RECYCLER_VIEW;
    }

    TypeElement listAdapter = elementUtils.getTypeElement("android.widget.Adapter"); // TODO adapter
    if (typeUtils.isSubtype(adapterClass.asType(), listAdapter.asType())) {
      return AdapterInfo.AdapterType.LIST_VIEW;
    }

    logger.error(adapterClass, "The class %s contains @%s annotations but is not a subclass of "
            + "%s nor %s. "
            + "Make %s extends one of those adapter classes", adapterClass.getSimpleName(),
        ViewType.class.getSimpleName(), SUPPORT_RECYCLER_ADAPTER,
        AbsListViewAnnotatedAdapter.class.getCanonicalName(), adapterClass.getSimpleName());

    return null;
  }

  /**
   * Get all the classes that have annoatated fields
   */
  public Map<String, AdapterInfo> getAdapterInfos() {
    return classMap;
  }
}
