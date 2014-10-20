package com.hannesdorfmann.annotatedadaptertest.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class ListAdapter extends AbsListViewAnnotatedAdapter implements ListAdapterBinder {

  @ViewType(
      layout = R.layout.row_small,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class),
      initMethod = true)
  public final int rowSimple = 0;

  @ViewType(
      layout = R.layout.row_medium,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class),
      initMethod = true)
  public final int rowMedium = 1;

  private List<String> items;

  public ListAdapter(Context context, List<String> items) {
    super(context);
    this.items = items;
  }

  @Override public int getItemViewType(int position) {
    return position % 2 == 0 ? rowSimple : rowMedium;
  }

  @Override public int getCount() {
    return items == null ? 0 : items.size();
  }

  @Override public Object getItem(int position) {
    return items.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public void initViewHolder(ListAdapterHolders.RowSimpleViewHolder vh, View view,
      ViewGroup parent) {
    vh.text.setTextColor(Color.RED);
  }

  @Override public void bindViewHolder(ListAdapterHolders.RowSimpleViewHolder vh, int position) {
    String item = items.get(position);
    vh.text.setText(item);
  }

  @Override public void initViewHolder(ListAdapterHolders.RowMediumViewHolder vh, View view,
      ViewGroup parent) {
    vh.text.setTextColor(Color.BLUE);
  }

  @Override public void bindViewHolder(ListAdapterHolders.RowMediumViewHolder vh, int position) {
    String item = items.get(position);
    vh.text.setText(item);
  }
}
