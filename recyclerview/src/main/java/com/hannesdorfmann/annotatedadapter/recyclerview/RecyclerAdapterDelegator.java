package com.hannesdorfmann.annotatedadapter.recyclerview;

import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

/**
 * @author Hannes Dorfmann
 */
public interface RecyclerAdapterDelegator {

  public void checkBinderInterfaceImplemented(SupportAnnotatedAdapter adapter);

  public int getViewTypeCount();

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType);

  public abstract void onBindViewHolder(RecyclerView.ViewHolder vh, int position);
}
