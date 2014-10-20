package com.hannesdorfmann.annotatedadapter;

/**
 * @author Hannes Dorfmann
 */
public interface AbsListViewDelegators {

  public static final String AUTO_GENERATOR_PACKAGE = "com.hannesdorfmann.annotatedadapter";

  public static final String AUTO_GENERATOR_CLASS_NAME = "AutoAbsListViewDelegators";

  public static final String AUTO_GENERATOR_QUALIFIED_NAME =
      AUTO_GENERATOR_PACKAGE + "." + AUTO_GENERATOR_CLASS_NAME;

  AbsListViewAdapterDelegator getDelegator(AbsListViewAnnotatedAdapter adapter);
}
