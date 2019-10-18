package com.hannesdorfmann.annotatedadaptertest.recycler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.hannesdorfmann.annotatedadaptertest.R;
import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends Activity {

  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    ButterKnife.bind(this);

    List<String> list = new ArrayList<String>();
    for (int i = 0; i < 40; i++) {
      list.add("Item " + i);
    }

    RecyclerAdapter adapter = new RecyclerAdapter(this, list);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
  }

}
