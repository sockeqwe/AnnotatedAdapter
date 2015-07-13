package com.hannesdorfmann.annotatedadaptertest.listview.inheritance;

import android.content.Context;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public abstract class AbsColorAdapter extends AbsListViewAnnotatedAdapter implements AbsColorAdapterBinder {

  @ViewType(
      layout = R.layout.row_small,
      views = @ViewField(id = R.id.textView, name = "text", type = TextView.class))
  public final int rowSimple = 0;

  protected List<String> items;

  public AbsColorAdapter(Context context, List<String> items) {
    super(context);
    this.items = items;
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
}
