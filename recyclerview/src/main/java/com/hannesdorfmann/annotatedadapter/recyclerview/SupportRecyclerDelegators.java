package com.hannesdorfmann.annotatedadapter.recyclerview;

/**
 * @author Hannes Dorfmann
 */
public interface SupportRecyclerDelegators {

  public static final String AUTO_GENERATOR_PACKAGE ="com.hannesdorfmann.annotatedAdapter";

  public static final String AUTO_GENERATOR_CLASS_NAME = "AutoSupportDelegators";

  public static final String AUTO_GENERATOR_QUALIFIED_NAME = AUTO_GENERATOR_PACKAGE +"."+ AUTO_GENERATOR_CLASS_NAME;

  SupportRecyclerAdapterDelegator getDelegator(SupportAnnotatedAdapter adapter);
}
