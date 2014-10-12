package com.hannesdorfmann.annotatedadapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.support.v7.widget.RecyclerView;

/**
 * This is the base class for adapters for recyclerviews
 *
 * @author Hannes Dorfmann
 */
public abstract class AnnotatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static RecyclerDelegators delgators;

  private RecyclerAdapterDelegator adapterDelegator;

  protected LayoutInflater inflater;

  public AnnotatedAdapter(Context context) {
    inflater = LayoutInflater.from(context);

    if (delgators == null) {
      // TODO Class.forName() generated delegators
    }

    adapterDelegator = delgators.getDelegator(this);

    // Will throw a runtime exception if the required binder has not been implemented
    adapterDelegator.checkBinderInterfaceImplemented(this);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    return adapterDelegator.onCreateViewHolder(viewGroup, i);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    adapterDelegator.onBindViewHolder(viewHolder, i);
  }

  public LayoutInflater getInflater(){
    return inflater;
  }
}
