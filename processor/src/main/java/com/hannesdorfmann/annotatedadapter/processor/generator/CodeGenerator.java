package com.hannesdorfmann.annotatedadapter.processor.generator;

import java.io.IOException;

/**
 * @author Hannes Dorfmann
 */
public interface CodeGenerator {

  public void generateAdapterHelper() throws IOException;

  public void generateBinderInterface() throws IOException;

  public void generateViewHolders() throws IOException;
}
