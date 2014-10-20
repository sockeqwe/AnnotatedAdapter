package com.hannesdorfmann.annotatedadaptertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hannesdorfmann.annotatedadaptertest.abs.ColorActivity;
import com.hannesdorfmann.annotatedadaptertest.listview.ListViewActivity;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.recyclerSimple)
  public void onRecyclerSimpleClicked() {
    startActivity(SampleActivity.class);
  }

  @OnClick(R.id.recyclerInheritance)
  public void onRecyclerInheritanceClicked() {
    startActivity(ColorActivity.class);
  }

  @OnClick(R.id.listViewSimple)
  public void onListSimpleClicked() {
    startActivity(ListViewActivity.class);
  }

  private void startActivity(Class<? extends Activity> targetClass) {
    Intent i = new Intent(this, targetClass);
    startActivity(i);
  }
}
