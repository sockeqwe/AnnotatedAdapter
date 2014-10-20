package com.hannesdorfmann.annotatedadaptertest.listview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends Activity {

  @InjectView(R.id.GridView) GridView gridView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_grid_view);
    ButterKnife.inject(this);

    List<String> list = new ArrayList<String>();
    for (int i = 0; i < 40; i++) {
      list.add("Item " + i);
    }
    
    gridView.setAdapter(new ListAdapter(this, list));
  }
}
