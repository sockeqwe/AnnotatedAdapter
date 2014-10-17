package com.hannesdorfmann.annotatedadaptertest.abs;

import android.content.Context;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class ThreeColorAdapter extends DualColorAdapter implements ThreeColorAdapterBinder {

  @ViewType(
      layout = R.layout.row_small,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class))
  public final int redRow = 3;

  public ThreeColorAdapter(Context context, List<String> items) {
    super(context, items);
  }

  @Override public int getItemViewType(int position) {
    switch (position % 3) {
      case 0:
        return smallRow;

      case 1:
        return mediumRow;

      case 2:
      default:
        return redRow;
    }
  }

  @Override public void bindRedRowViewHolder(ThreeColorAdapterHolders.RedRowViewHolder vh,
      int position) {
    
  }
}
