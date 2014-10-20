package com.hannesdorfmann.annotatedadaptertest.recycler.inheritance;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class DualColorAdapter extends AbsColorAdapter implements DualColorAdapterBinder {

  @ViewType(
      layout = R.layout.row_medium,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class))
  public final int mediumRow = 1;

  public DualColorAdapter(Context context, List<String> items) {
    super(context, items);
  }

  @Override public int getItemViewType(int position) {
    return position % 2 == 0 ? smallRow : mediumRow;
  }

  @Override public void bindViewHolder(DualColorAdapterHolders.MediumRowViewHolder vh,
      int position) {
    String txt = items.get(position);
    vh.text.setText(txt);
  }

  @Override public void bindViewHolder(AbsColorAdapterHolders.SmallRowViewHolder vh,
      int position) {

    String txt = items.get(position);
    vh.text.setText(txt);
    vh.text.setTextColor(Color.CYAN);
  }
}
