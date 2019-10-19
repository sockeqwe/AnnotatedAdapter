package com.hannesdorfmann.annotatedadapter.support.recyclerview;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Hannes Dorfmann
 */
public interface SupportRecyclerAdapterDelegator {

  public void checkBinderInterfaceImplemented(SupportAnnotatedAdapter adapter);

  public int getViewTypeCount();

  public RecyclerView.ViewHolder onCreateViewHolder(SupportAnnotatedAdapter adapter,
      ViewGroup viewGroup, int viewType);

  public abstract void onBindViewHolder(SupportAnnotatedAdapter adapter, RecyclerView.ViewHolder vh,
      int position);
}
