package com.hannesdorfmann.annotatedadapter.processor.generator;

/**
 * @author Hannes Dorfmann
 */
public interface CodeGenerator {

  public void generateAdapterHelper();

  public void generateBinderInterface();
}
