package com.hannesdorfmann.annotatedadaptertest.listview.inheritance;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class ThreeColorsAdapter extends TwoColorsAdapter implements ThreeColorsAdapterBinder {

  @ViewType(
      layout = R.layout.row_big,
      views = @ViewField(type = TextView.class, name = "text", id = R.id.textView),
      initMethod = true)
  public final int rowBig = 2;

  public ThreeColorsAdapter(Context context, List<String> items) {
    super(context, items);
  }

  @Override public int getItemViewType(int position) {
    switch (position % 3) {
      case 0:
        return rowSimple;
      case 1:
        return rowMedium;
      default:
        return rowBig;
    }
  }

  @Override public void initViewHolder(ThreeColorsAdapterHolders.RowBigViewHolder vh, View view,
      ViewGroup parent) {
    vh.text.setTextColor(Color.RED);
  }

  @Override public void bindViewHolder(ThreeColorsAdapterHolders.RowBigViewHolder vh,
      int position) {
    String item = items.get(position);
    vh.text.setText(item);
  }
}
