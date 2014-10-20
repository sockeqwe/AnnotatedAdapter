package com.hannesdorfmann.annotatedadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * This {@link BaseAdapter} encapsulate the getView() call into
 * {@link #newView(int, ViewGroup)} and {@link #bindView(int, int, View)}
 *
 * @author Hannes Dorfmann
 */
abstract class SimpleAbsListAdapter extends BaseAdapter {

  /**
   * The inflater for
   */
  protected LayoutInflater inflater;
  protected Context context;

  public SimpleAbsListAdapter(Context context) {
    this.context = context;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public LayoutInflater getInflater() {
    return inflater;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    int type = getItemViewType(position);
    if (convertView == null) {
      convertView = newView(type, parent);
    }
    bindView(position, type, convertView);
    return convertView;
  }

  /** Create a new instance of a view for the specified {@code type}. */
  public abstract View newView(int type, ViewGroup parent);

  /** Bind the data for the specified {@code position} to the {@code view}. */
  public abstract void bindView(int position, int type, View view);
}
