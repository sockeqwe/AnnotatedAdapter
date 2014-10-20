package com.hannesdorfmann.annotatedadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * The base adapter for handling
 *
 * @author Hannes Dorfmann
 */
public abstract class AbsListViewAnnotatedAdapter extends SimpleAbsListAdapter {

  private AbsListViewAdapterDelegator adapterDelegator = null;
  private AbsListViewDelegators delgators = null;

  protected AbsListViewAnnotatedAdapter(Context context) {
    super(context);

    if (delgators == null) {
      try {
        Class<?> autoClass = Class.forName(AbsListViewDelegators.AUTO_GENERATOR_QUALIFIED_NAME);
        delgators = (AbsListViewDelegators) autoClass.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
            "Could not load " + AbsListViewDelegators.AUTO_GENERATOR_QUALIFIED_NAME);
      }
    }

    adapterDelegator = delgators.getDelegator(this);

    if (adapterDelegator == null) {
      throw new RuntimeException("Could not load the AdapterDelegator!");
    }

    // Will throw a runtime exception if the required binder has not been implemented
    adapterDelegator.checkBinderInterfaceImplemented(this);
  }

  @Override public void bindView(int position, int type, View view) {
    adapterDelegator.onBindViewHolder(this, view, position); // TODO type
  }

  @Override public View newView(int type, ViewGroup parent) {
    return adapterDelegator.onCreateViewHolder(this, parent, type);
  }

  @Override public int getViewTypeCount() {
    return adapterDelegator.getViewTypeCount();
  }
}
