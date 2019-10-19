package com.hannesdorfmann.annotatedadapter.support.recyclerview;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * This is the base class for adapters for recyclerviews
 *
 * @author Hannes Dorfmann
 */
public abstract class SupportAnnotatedAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static SupportRecyclerDelegators delgators;

  private SupportRecyclerAdapterDelegator adapterDelegator;

  protected LayoutInflater inflater;

  public SupportAnnotatedAdapter(Context context) {
    inflater = LayoutInflater.from(context);

    if (delgators == null) {
      try {
        Class<?> autoClass = Class.forName(SupportRecyclerDelegators.AUTO_GENERATOR_QUALIFIED_NAME);
        delgators = (SupportRecyclerDelegators) autoClass.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
            "Could not load " + SupportRecyclerDelegators.AUTO_GENERATOR_QUALIFIED_NAME);
      }
    }

    adapterDelegator = delgators.getDelegator(this);

    if(adapterDelegator == null) {
      throw new RuntimeException("Could not load the AdapterDelegator!");
    }

    // Will throw a runtime exception if the required binder has not been implemented
    adapterDelegator.checkBinderInterfaceImplemented(this);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    return adapterDelegator.onCreateViewHolder(this, viewGroup, i);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    adapterDelegator.onBindViewHolder(this, viewHolder, i);
  }

  public LayoutInflater getInflater() {
    return inflater;
  }
}
