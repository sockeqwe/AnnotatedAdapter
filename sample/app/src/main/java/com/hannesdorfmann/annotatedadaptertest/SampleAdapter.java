package com.hannesdorfmann.annotatedadaptertest;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.recyclerview.SupportAnnotatedAdapter;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class SampleAdapter extends SupportAnnotatedAdapter implements SampleAdapterBinder {

  @ViewType(
      layout = R.layout.row_medium,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class))
  public final int mediumRow = 0;

  @ViewType(
      layout = R.layout.row_with_pic,
      fields = {
          @Field(id = R.id.textView, name = "text", type = TextView.class),
          @Field(id = R.id.imageView, name = "image", type = ImageView.class)
      }

  )
  public final int rowWithPic = 1;

  @ViewType(
      layout = R.layout.row_small,
      fields = @Field(id = R.id.textView, name = "text", type = TextView.class))
  public final int smallRow = 2;

  List<String> items;

  public SampleAdapter(Context context, List<String> items) {
    super(context);
    this.items = items;
  }

  @Override public int getItemCount() {
    return items == null ? 0 : items.size();
  }

  @Override public int getItemViewType(int position) {
    switch (position % 3) {
      case 0:
        return mediumRow;
      case 1:
        return smallRow;
      case 2:
      default:
        return rowWithPic;
    }
  }

  @Override public void bindViewHolder(SampleAdapterHolders.MediumRowViewHolder vh, int position) {

    String str = items.get(position);
    vh.text.setText(str);
  }

  @Override public void bindViewHolder(SampleAdapterHolders.RowWithPicViewHolder vh, int position) {
    String str = items.get(position);
    vh.text.setText(str);
    vh.image.setImageResource(R.drawable.ic_launcher);
  }

  @Override public void bindViewHolder(SampleAdapterHolders.SmallRowViewHolder vh, int position) {
    String str = items.get(position);
    vh.text.setText(str);
  }
}
