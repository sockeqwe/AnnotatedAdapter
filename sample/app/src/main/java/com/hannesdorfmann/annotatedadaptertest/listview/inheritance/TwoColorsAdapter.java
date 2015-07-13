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
public class TwoColorsAdapter extends AbsColorAdapter implements TwoColorsAdapterBinder{

  @ViewType(
      layout = R.layout.row_medium,
      views = @ViewField(id = R.id.textView, name = "text", type = TextView.class),
      initMethod = true)
  public final int rowMedium = 1;

  public TwoColorsAdapter(Context context, List<String> items) {
    super(context, items);
  }

  @Override public int getItemViewType(int position) {
    return position % 2 == 0 ? rowSimple : rowMedium;
  }

  @Override public void bindViewHolder(AbsColorAdapterHolders.RowSimpleViewHolder vh,
      int position) {

    String item = items.get(position);
    vh.text.setText(item);

  }

  @Override public void initViewHolder(TwoColorsAdapterHolders.RowMediumViewHolder vh, View view,
      ViewGroup parent) {

    vh.text.setTextColor(Color.CYAN);

  }

  @Override public void bindViewHolder(TwoColorsAdapterHolders.RowMediumViewHolder vh,
      int position) {

    String item = items.get(position);
    vh.text.setText(item);

  }
}
