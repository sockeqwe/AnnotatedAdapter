package com.hannesdorfmann.annotatedadaptertest.listview.inheritance;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.ArrayList;
import java.util.List;

public class ListViewInheritanceActivity extends Activity {

  @BindView(R.id.listView) ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_view_inheritance);
    ButterKnife.bind(this);


    List<String> list = new ArrayList<String>();
    for (int i = 0; i < 40; i++) {
      list.add("Item " + i);
    }

    listView.setAdapter(new ThreeColorsAdapter(this, list));
  }
}
