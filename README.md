# AnnotatedAdapter
Sick of writing ViewHolder classes, inflate xml and distinguish ViewTypes in your adapters?  
Write less code with AnnotatedAdapter, an annotation processor for generating `RecyclerView` and `AbsListView` adapters.

# Best Practice
An interface called `Binder` (see Usage) will be generated for each AnnoatedAdapter. Hence the following workflow is considered as best practice in android studio:
 1. Create your adapter class and make this class extends from `SupportAnnotatedAdapter`
 2. Define at least one `@ViewType`
 3. In the main menu bar: `Build -> Rebuild Project`. This will force to generate the _Binder interface_
 4. Make your adapter class implementing the Binder interface and implement the required methods

Note that the manually triggered rebuild is normally required only on the very first time you create a new adapter class.


# Dependency
Check [GradlePlease](http://gradleplease.appspot.com/#com.hannesdorfmann.annotatedadapter) to get the latest version number.

To run annotation processing you need to apply Hugo Visser's awesome [android-apt](https://bitbucket.org/hvisser/android-apt) gradle plugin.

 - Use `SupportAnnotatedAdapter` as base class and the following dependencies for `RecyclerView` from **support library**
```groovy
dependencies {
	compile 'com.hannesdorfmann.annotatedadapter:annotation:1.0.0'
	compile 'com.hannesdorfmann.annotatedadapter:support-recyclerview:1.0.0'
	apt 'com.hannesdorfmann.annotatedadapter:processor:1.0.0'
}
```

 - Use `AbsListAnnotatedAdapter` as base class and the following dependencies for `AbsListView widgets` like `ListView` or `GridView`: 
```groovy
dependencies {
	compile 'com.hannesdorfmann.annotatedadapter:annotation:1.0.0'
	apt 'com.hannesdorfmann.annotatedadapter:processor:1.0.0'  	
}
```

# Usage
Check out the sample folder, but basically you have to create an adapter class like this and annotate the view types with `@ViewType` and provide some more information in its annotation:

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
  @Override public void bindViewHolder(SampleAdapterHolders.MediumRowViewHolder vh,
        int position) {
  
      String str = items.get(position);
      vh.text.setText(str);
    }
  
    /**
     * Bind the data to this view type rowWithPic; RowWithPicViewHolder was generated
     */
    @Override public void bindViewHolder(SampleAdapterHolders.RowWithPicViewHolder vh,
        int position) {
  
      String str = items.get(position);
      vh.text.setText(str);
      vh.image.setImageResource(R.drawable.ic_launcher);
    }
  
}
```


Even if there are already some comments in the code shown above, let's review the code step by step:

 1. Create an adapter class that extends from `SupportAnnotatedAdapter` for _android.support.v7.widget.RecyclerView_ or `AbsListAnnotatedAdapter` for _AbsListView (like ListView or GridView)_
 2. Set view types like you would do in any normal adapter by specifying integer constants. Remember those constants must start with zero.
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
 5. An interface will be generated (if adapter class contains at least one `@ViewType`) with the name `AdapterClassName + Binder`. 
 6. Let your adapter class implement this interface. For each view type you have to implement the corresponding `bindViewHolder()` method where you bind the data to the generated view holder. 


# Lifecycle and methods call
Internally views and ViewHolders are created and are recycled like you expect from your own handwritten adapter implementation. Basically the following steps are executed for each cell (view):
 1. Call `int viewType = getItemViewType(position)` to determine the view type
 2. If there is a cell (view) that can be recycled then continue in step 4.
 3. If no cell (view) can be recycled instantiate a new one:
    1. Inflate the xml layout specified in `@ViewType( layout = R.layout.id )`
    2. Create a new instance of the corresponding ViewHolder class. `findViewById()`will be used for each field in `@ViewHolder ( fields = { @Field ( ... ) } )`
    3. If you want to do additional initialization of the inflated View (like setting the width or height of a subview) in code then you have to set `@ViewHolder( initMethod = true)`. This will force to create a method called `initViewHolder(viewHolderClass, view, parent)` in the Binder interface which you have to implement afterwards
 4. Call `bindViewHolder(viewHolder, position)` to bind the data to the cell (view)
 
# Inheritance
AnnotatedAdapter supports inheritance. The only thing you have to keep in mind, like for any other handwritten adapter, is that the view holders constant integer value must be unique along the inheritance tree.

Example:
```java
public class BaseAdapter extends SupportAnnotatedAdapter implements BaseAdapterHolder {

    @ViewHolder (...)
    public final int simpleRow = 0;

}

public class OtherAdapter extends BaseAdpter implements BaseAdapterHolder {

    @ViewHolder (...)
    public final int otherRow = 0;  // Cause problems, because BaseAdapter.simpleRow == 1 && OtherAdapter.otherRow == 1 

}
```    

In this case are `@ViewType simpleRow = 0` and `@ViewType otherRow = 0` which will cause unexpected behaviour. 
To avoid this kind of problems AnnotatedAdapter will throw a compile time error that states that there are two view types with the same value.
However, you can disable this check by setting `@ViewType( checkValue = false )`. Do that only if you have a very good reason for.
Usually it should be enough to override the `bindViewHolder()` method in your subclass instead of setting `@ViewType( checkValue = false )`.
The only good reason I can see right now is to "override" the xml layout that should be inflated. Notice that at this point the subclass @ViewType definition will be used instead of the base class @ViewType definition.