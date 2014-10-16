# AnnotatedAdapter
Sick of writing ViewHolder classes, inflate xml and distinguis ViewTypes in your adapters?  
Write less code with AnnotatedAdapter, an annotation processor for generating `RecyclerView` and `AbsListView` adapters.

# Work in Progress
Please note that this project is still under development. However **SNAPSHOTS** are available in maven central. Right now only `android.support.v7.widget.RecyclerView` is implemented. `AbsListView` as well as `android.widget.RecyclerView` (Android 5.0) will be implemented in the final 1.0.0 release

# Dependency
Check [GradlePlease](http://gradleplease.appspot.com/#com.hannesdorfmann.annotatedadapter) to get the latest version number.

To run annotation processing you need to apply Hugo Visser's awesome [android-apt](https://bitbucket.org/hvisser/android-apt) gradle plugin.

```groovy
repositories {

  // Maven central snapshot repository
  maven { 
    url "https://oss.sonatype.org/content/repositories/snapshots"
  }
}

dependencies {
	compile 'com.hannesdorfmann.fragmentargs:annotation:0.5.0-SNAPSHOT'
	apt 'com.hannesdorfmann.fragmentargs:processor:0.5.0-SNAPSHOT'
}
```

# How does it works?
Check out the sample folder, but basically you have to create an adapter class like this and annotate the viewtypes with `@ViewType` and provide some more information in this annotation:

```java
public class SampleAdapter extends SupportAnnotatedAdapter 
                            implements SampleAdapterBinder {

  /**
   * Specify a view type by annotating a public final int with @ViewType.
   * Like for any other adapter the view types must be start with an integer = 0
   */
  @ViewType(
      layout = R.layout.row_medium,   // The layout that will be inflated for this view type 
      fields = {                      // The fields of the view holder
        @Field(
            id = R.id.textView,       // The id of this view
            name = "text",            // The name of this field in the generated ViewHolder
            type = TextView.class)    // The type (class) of view in the generated view holder
            }
        }
   )
  public final int mediumRow = 0;     // The annotated ViewType constant


   @ViewType(
        layout = R.layout.row_with_pic,
        fields = {
            @Field(id = R.id.textView, name = "text", type = TextView.class),
            @Field(id = R.id.imageView, name = "image", type = ImageView.class)
        }
    )
  public final int rowWithPic = 1;

  List<String> items;

  public SampleAdapter(Context context, List<String> items) {
    super(context);
    this.items = items;
  }

  /**
   * Get the number of items like in any other adapter
   */
  @Override public int getItemCount() {
    return items == null ? 0 : items.size();
  }

  /**
   * Determine the view type for the cell at position (like you would do in any other adpater)
   */
  @Override public int getItemViewType(int position) {
    if (position % 2 == 0)
        return mediumRow;
    else
        return rowWithPic;
    }
  }
  
  /**
   * Bind the data to this view type mediumRow; MediumRowViewHolder was generated
   */
  @Override public void bindMediumRowViewHolder(SampleAdapterHolders.MediumRowViewHolder vh,
        int position) {
  
      String str = items.get(position);
      vh.text.setText(str);
    }
  
    /**
     * Bind the data to this view type rowWithPic; RowWithPicViewHolder was generated
     */
    @Override public void bindRowWithPicViewHolder(SampleAdapterHolders.RowWithPicViewHolder vh,
        int position) {
  
      String str = items.get(position);
      vh.text.setText(str);
      vh.image.setImageResource(R.drawable.ic_launcher);
    }
  
}
```


Even if there are already some comments in the code shown above, let's review the code step by step:

 1. Create an adapter class that extends from `SupportAnnotatedAdapter` for _android.support.v7.widget.RecyclerView_ , `AnnotatedAdapter` for _RecyclerView in Android 5.0 (not from support library) or `AbsListAnnotatedAdapter` for _AbsListView (like ListView or GridView)_
 2. Set view types like you would do in any normal adapter by specifying integer constants. Remember those constants must begin by zero.
 3. Annotate this view types with `@ViewType`. Specify the layout that should be inflated for this view type and declare the fields that should be generated for the corresponding view holder. The following anntated view type:
 ```java
 @ViewType(
         layout = R.layout.row_with_pic,
         fields = {
             @Field(id = R.id.textView, name = "fooText", type = TextView.class),
             @Field(id = R.id.imageView, name = "image", type = ImageView.class)
         }
   
     )
   public final int rowWithPic = 0;
 ```
 will generate the following view holder class:
 ```java
 public static class RowWithPicViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
 
     public android.widget.TextView fooText;
     public android.widget.ImageView image;
 
     public RowWithPicViewHolder(android.view.View view) {
 
       super(view);
 
       fooText = (android.widget.TextView) view.findViewById(R.id.textView);
       image = (android.widget.ImageView) view.findViewById(R.id.imageView);
     }
   }
   ```
 4. Like in any other adapter you have to specify which view type should be displayed for the given position by overriding `public int getItemViewType(int position)` and you of course you have to say how many items are displayed in the RecyclerView / ListView by overriding `public int getItemCount()`
 5. An interface will be generated (if adapter class contains at least one `@ViewType`) with the name `AdapterClassName + Binder`. Implement this interface. For each view type you have to implement the corresponding method from this interface where you bind the data to the generated view holder.
 
