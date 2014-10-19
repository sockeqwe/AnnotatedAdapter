package com.hannesdorfmann.annotatedadapter.support.recyclerview;

/**
 * @author Hannes Dorfmann
 */
public interface SupportRecyclerDelegators {

  public static final String AUTO_GENERATOR_PACKAGE ="com.hannesdorfmann.annotatedadapter";

  public static final String AUTO_GENERATOR_CLASS_NAME = "AutoSupportDelegators";

  public static final String AUTO_GENERATOR_QUALIFIED_NAME = AUTO_GENERATOR_PACKAGE +"."+ AUTO_GENERATOR_CLASS_NAME;

  SupportRecyclerAdapterDelegator getDelegator(SupportAnnotatedAdapter adapter);
}
