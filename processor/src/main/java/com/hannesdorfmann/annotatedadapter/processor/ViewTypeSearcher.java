package com.hannesdorfmann.annotatedadapter.processor;

import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.processor.util.ProcessorMessage;
import dagger.ObjectGraph;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * @author Hannes Dorfmann
 */
public class ViewTypeSearcher {

  @Inject ProcessorMessage logger;

  /**
   * Maps the
   */
  private Map<String, Element> classMap = new HashMap<String, Element>();

  public ViewTypeSearcher(ObjectGraph graph) {
    graph.inject(this);
  }

  public void addElementIfNotAlready(VariableElement field) {

  }

  public boolean isValidField(Element element) {

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

      // TODO check if its an int
      
    } else {
      logger.error(element, "@%s can only be applied on integer fields. %s is not a field",
          ViewType.class.getSimpleName(), element.getSimpleName());
      return false;
    }

    return false;
  }
}
