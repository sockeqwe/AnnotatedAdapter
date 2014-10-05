// IntelliJ API Decompiler stub source generated from a class file
// Implementation of methods is not available

package com.android.support.v7.widget;

public class RecyclerView {

  public static abstract class ViewHolder {

  }

  public static abstract class Adapter<VH extends ViewHolder> {

    public Adapter() {
    }

    public abstract VH onCreateViewHolder(android.view.ViewGroup viewGroup, int i);

    public abstract void onBindViewHolder(VH vh, int i);

    public int getItemViewType(int position) {
      return 0;
    }

    public abstract int getItemCount();
  }
}