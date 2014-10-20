package com.hannesdorfmann.annotatedadaptertest.recycler.inheritance;

import android.content.Context;
import android.util.Pair;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public abstract class AbsColorAdapter extends SupportAnnotatedAdapter {

  protected List<String> items;

  @ViewType(
      layout = R.layout.row_small,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class))
  public final int smallRow = 0;

  public AbsColorAdapter(Context context, List<String> items) {
    super(context);
    this.items = items;

    Pair.create(2, 4);
  }

  @Override public int getItemCount() {
    return items == null ? 0 : items.size();
  }


}
