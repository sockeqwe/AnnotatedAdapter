package com.hannesdorfmann.annotatedadapter.processor.generator;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.processor.AdapterInfo;
import com.hannesdorfmann.annotatedadapter.processor.ViewTypeInfo;
import com.hannesdorfmann.annotatedadapter.processor.util.TypeHelper;
import com.squareup.javawriter.JavaWriter;
import dagger.ObjectGraph;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

/**
 * @author Hannes Dorfmann
 */
public class RecyclerViewGenerator implements CodeGenerator {

  private AdapterInfo info;

  @Inject Filer filer;

  @Inject TypeHelper typeHelper;

  public RecyclerViewGenerator(ObjectGraph graph, AdapterInfo info) {
    this.info = info;
  }

  @Override public void generateAdapterHelper() {

  }

  @Override public void generateBinderInterface() throws IOException {

    if (info.getViewTypes().isEmpty()) {
      return;
    }

    String packageName = typeHelper.getPackageName(info.getAdapterClass());
    String binderClassName = info.getBinderClassName();
    String qualifiedBinderName = packageName + binderClassName;

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
      jw.beginMethod("void", vt.getBinderMethodName(), EnumSet.of(Modifier.PUBLIC),
          vt.getViewHolderClassName(), "vh");
      jw.endMethod();
      jw.emitEmptyLine();
    }

    jw.endType();
    jw.close();
  }

  @Override public void generateViewHolders() throws IOException {

    if (info.getViewTypes().isEmpty()) {
      return;
    }

    String packageName = typeHelper.getPackageName(info.getAdapterClass());
    String holdersClassName = info.getViewHoldersClassName();
    String qualifiedHoldersName = packageName + holdersClassName;

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
      jw.beginType(v.getViewHolderClassName(), "class", EnumSet.of(Modifier.PUBLIC));
      jw.emitEmptyLine();

      // Insert fields
      for (Field f : v.getFields()) {
        jw.emitField(f.type().getCanonicalName(), f.name(), EnumSet.of(Modifier.PUBLIC));
      }

      if (v.getFields().length > 0) {
        jw.emitEmptyLine();
      }

      jw.beginConstructor(EnumSet.of(Modifier.PUBLIC), "android.view.View", "view");
      for (Field f : v.getFields()) {
        jw.emitStatement("%s = (%s) view.findViewById(%d)", f.name(), f.type().getCanonicalName(),
            f.id());
      }
      jw.endConstructor();
      jw.endType();

      jw.emitEmptyLine();
    }

    jw.endType(); // End of holders class
    jw.close();
  }
}
