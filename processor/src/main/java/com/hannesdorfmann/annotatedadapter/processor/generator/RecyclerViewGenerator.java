package com.hannesdorfmann.annotatedadapter.processor.generator;

import dagger.ObjectGraph;
import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.lang.model.element.Element;

/**
 * @author Hannes Dorfmann
 */
public class RecyclerViewGenerator implements CodeGenerator {

  private Element originClass;

  @Inject
  Filer filer;

  public RecyclerViewGenerator(ObjectGraph graph, Element clazz){
      this.originClass = clazz;
  }

  @Override public void generateAdapterHelper() {

  }

  @Override public void generateBinderInterface() {

  }
}
