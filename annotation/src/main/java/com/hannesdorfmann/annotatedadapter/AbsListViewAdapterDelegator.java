package com.hannesdorfmann.annotatedadapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author Hannes Dorfmann
 */
public interface AbsListViewAdapterDelegator {

  public void checkBinderInterfaceImplemented(AbsListViewAnnotatedAdapter adapter);

  public int getViewTypeCount();

  public View onCreateViewHolder(AbsListViewAnnotatedAdapter adapter,
      ViewGroup viewGroup, int viewType);

  public abstract void onBindViewHolder(AbsListViewAnnotatedAdapter adapter, View view,
      int position);
}
