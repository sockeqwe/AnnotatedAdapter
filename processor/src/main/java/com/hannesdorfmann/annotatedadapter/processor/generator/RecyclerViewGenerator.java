package com.hannesdorfmann.annotatedadapter.processor.generator;

import com.hannesdorfmann.annotatedadapter.processor.AdapterInfo;
import dagger.ObjectGraph;
import javax.annotation.processing.Filer;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public class RecyclerViewGenerator implements CodeGenerator {

  private AdapterInfo info;

  @Inject
  Filer filer;

  public RecyclerViewGenerator(ObjectGraph graph, AdapterInfo info){
      this.info = info;
  }

  @Override public void generateAdapterHelper() {

  }

  @Override public void generateBinderInterface() {

  }
}
