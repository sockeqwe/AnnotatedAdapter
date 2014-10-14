package com.hannesdorfmann.annotatedadapter.processor.generator;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.processor.AdapterInfo;
import com.hannesdorfmann.annotatedadapter.processor.ViewTypeInfo;
import com.hannesdorfmann.annotatedadapter.processor.ViewTypeSearcher;
import com.hannesdorfmann.annotatedadapter.processor.util.TypeHelper;
import com.hannesdorfmann.annotatedadapter.recyclerview.SupportRecyclerAdapterDelegator;
import com.squareup.javawriter.JavaWriter;
import dagger.ObjectGraph;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

/**
 * @author Hannes Dorfmann
 */
public class RecyclerViewGenerator implements CodeGenerator {

  private static String VIEW_HOLDER = "android.support.v7.widget.RecyclerView.ViewHolder";

  private AdapterInfo info;
  @Inject Filer filer;
  @Inject TypeHelper typeHelper;

  public RecyclerViewGenerator(ObjectGraph graph, AdapterInfo info) {
    this.info = info;
    graph.inject(this);
  }

  private void generateAdapterHelper() throws IOException {

    if (info.getViewTypes().isEmpty()) {
      return;
    }

    String packageName = typeHelper.getPackageName(info.getAdapterClass());
    String delegatorClassName = info.getAdapterDelegatorClassName();
    String delegatorBinderName = packageName + "." + delegatorClassName;

    //
    // Write code
    //

    JavaFileObject jfo = filer.createSourceFile(delegatorBinderName, info.getAdapterClass());
    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);

    jw.emitPackage(packageName);
    jw.emitEmptyLine();
    jw.emitJavadoc("Generated class by AnnotatedAdapter . Do not modify this code!");
    jw.beginType(delegatorClassName, "class", EnumSet.of(Modifier.PUBLIC), null,
        SupportRecyclerAdapterDelegator.class.getCanonicalName());
    jw.emitEmptyLine();
    jw.emitEmptyLine();

    // Check binder interface implemented
    jw.beginMethod("void", "checkBinderInterfaceImplemented", EnumSet.of(Modifier.PUBLIC),
        ViewTypeSearcher.SUPPORT_RECYCLER_ADAPTER, "adapter");
    jw.emitEmptyLine();
    jw.beginControlFlow("if (!(adapter instanceof %s)) ", info.getBinderClassName());
    jw.emitStatement(
        "throw new java.lang.RuntimeException(\"The adapter class %s must implement the binder interface %s \")",
        info.getAdapterClassName(), info.getBinderClassName());
    jw.endControlFlow();
    jw.endMethod();

    // ViewTypeCount
    jw.beginMethod("int", "getViewTypeCount", EnumSet.of(Modifier.PUBLIC));
    jw.emitStatement("return %d", info.getViewTypes().size());
    jw.endMethod();

    jw.emitEmptyLine();
    jw.emitEmptyLine();

    // onCreateViewHolder
    jw.beginMethod(VIEW_HOLDER, "onCreateViewHolder", EnumSet.of(Modifier.PUBLIC),
        ViewTypeSearcher.SUPPORT_RECYCLER_ADAPTER, "adapter", "android.view.ViewGroup", "viewGroup",
        "int", "viewType");

    jw.emitEmptyLine();
    jw.emitStatement("%s ad = (%s) adapter", info.getAdapterClass(), info.getAdapterClass());
    jw.emitEmptyLine();

    int ifs = 0;

    for (ViewTypeInfo vt : info.getViewTypes()) {
      jw.beginControlFlow((ifs > 0 ? "else " : "") + "if (viewType == ad.%s)", vt.getFieldName());
      jw.emitStatement("android.view.View v = ad.getInflater().inflate(%d, viewGroup, false)",
          vt.getLayoutRes());
      jw.endControlFlow();
      ifs++;
    }

    jw.emitEmptyLine();
    jw.emitStatement(
        "throw new java.lang.IllegalArgumentException(\"Unknown view type \"+viewType)");
    jw.endMethod();

    jw.emitEmptyLine();
    jw.emitEmptyLine();

    // onBindViewHolder
    jw.beginMethod("void", "onBindViewHolder", EnumSet.of(Modifier.PUBLIC),
        ViewTypeSearcher.SUPPORT_RECYCLER_ADAPTER, "adapter", VIEW_HOLDER, "vh", "int", "position");

    jw.emitEmptyLine();
    jw.emitStatement("%s binder = (%s) adapter", info.getBinderClassName(),
        info.getBinderClassName());

    ifs = 0;
    for (ViewTypeInfo vt : info.getViewTypes()) {
      jw.beginControlFlow((ifs > 0 ? "else " : "") + "if (vh instanceof %s.%s)",
          info.getViewHoldersClassName(), vt.getViewHolderClassName());

      StringBuilder builder = new StringBuilder("binder.");
      builder.append(vt.getBinderMethodName());
      builder.append("( (");
      builder.append(vt.getViewHolderClassName());
      builder.append(") vh, position");
      if (vt.hasModelClass()) {
        builder.append(", (");
        builder.append(vt.getQualifiedModelClass());
        builder.append(") getItem(position)");
      }
      builder.append(")");

      jw.emitStatement(builder.toString());
      jw.endControlFlow();
      ifs++;
    }

    jw.emitStatement("throw new java.lang.IllegalArgumentException("
        + "\"Binder method not found for unknown viewholder class\"+vh.class.getCanonicalName())");

    jw.endMethod();

    jw.endType();
    jw.close();
  }

  private void generateBinderInterface() throws IOException {

    if (info.getViewTypes().isEmpty()) {
      return;
    }

    String packageName = typeHelper.getPackageName(info.getAdapterClass());
    String binderClassName = info.getBinderClassName();
    String qualifiedBinderName = packageName + "." + binderClassName;

    //
    // Write code
    //

    JavaFileObject jfo = filer.createSourceFile(qualifiedBinderName, info.getAdapterClass());
    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);

    jw.emitPackage(packageName);
    jw.emitJavadoc("Generated class by AnnotatedAdapter . Do not modify this code!");
    jw.beginType(binderClassName, "interface", EnumSet.of(Modifier.PUBLIC));
    jw.emitEmptyLine();

    for (ViewTypeInfo vt : info.getViewTypes()) {
      jw.emitEmptyLine();
      List<String> params = new ArrayList(6);

      params.add(vt.getViewHolderClassName());
      params.add("vh");

      params.add("int");
      params.add("position");

      if (vt.hasModelClass()) {
        params.add(vt.getQualifiedModelClass());
        params.add("model");
      }

      jw.beginMethod("void", vt.getBinderMethodName(), EnumSet.of(Modifier.PUBLIC), params,
          new ArrayList<String>());

      jw.endMethod();
      jw.emitEmptyLine();
    }

    jw.endType();
    jw.close();
  }

  private void generateViewHolders() throws IOException {

    if (info.getViewTypes().isEmpty()) {
      return;
    }

    String packageName = typeHelper.getPackageName(info.getAdapterClass());
    String holdersClassName = info.getViewHoldersClassName();
    String qualifiedHoldersName = packageName + "." + holdersClassName;

    //
    // Write code
    //

    JavaFileObject jfo = filer.createSourceFile(qualifiedHoldersName, info.getAdapterClass());
    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);

    // Class things
    jw.emitPackage(packageName);
    jw.emitJavadoc("Generated class by AnnotatedAdapter . Do not modify this code!");
    jw.beginType(holdersClassName, "class", EnumSet.of(Modifier.PUBLIC));
    jw.emitEmptyLine();

    jw.beginConstructor(EnumSet.of(Modifier.PRIVATE));
    jw.endConstructor();

    jw.emitEmptyLine();
    jw.emitEmptyLine();

    for (ViewTypeInfo v : info.getViewTypes()) {
      jw.beginType(v.getViewHolderClassName(), "class", EnumSet.of(Modifier.PUBLIC), VIEW_HOLDER);
      jw.emitEmptyLine();

      // Insert fields
      for (Field f : v.getFields()) {
        jw.emitField(f.type().getCanonicalName(), f.name(), EnumSet.of(Modifier.PUBLIC));
      }

      if (v.getFields().length > 0) {
        jw.emitEmptyLine();
      }

      jw.beginConstructor(EnumSet.of(Modifier.PUBLIC), "android.view.View", "view");
      jw.emitEmptyLine();
      jw.emitStatement("super(view)");
      jw.emitEmptyLine();
      for (Field f : v.getFields()) {
        jw.emitStatement("%s = (%s) view.findViewById(%d)", f.name(), f.type().getCanonicalName(),
            f.id());
      }
      jw.endConstructor();
      jw.endType();

      jw.emitEmptyLine();
      jw.emitEmptyLine();
    }

    jw.endType(); // End of holders class
    jw.close();
  }

  @Override public void generateCode() throws IOException {
    generateViewHolders();
    generateBinderInterface();
    generateAdapterHelper();
  }
}
