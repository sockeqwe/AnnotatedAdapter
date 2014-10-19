package com.hannesdorfmann.annotatedadapter.processor;

import com.google.auto.service.AutoService;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.processor.generator.CodeGenerator;
import com.hannesdorfmann.annotatedadapter.processor.generator.RecyclerViewGenerator;
import com.hannesdorfmann.annotatedadapter.processor.util.AnnotatedAdapterModule;
import com.hannesdorfmann.annotatedadapter.processor.util.ProcessorMessage;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportRecyclerDelegators;
import com.squareup.javawriter.JavaWriter;
import dagger.ObjectGraph;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * AnnotationProcessor for AnnotatedAdapter @ViewType
 *
 * @author Hannes Dorfmann
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.hannesdorfmann.annotatedadapter.annotation.ViewType")
public class AnnotatedAdapterProcessor extends AbstractProcessor {

  @Inject Elements elementUtils;

  @Inject Types typeUtils;

  @Inject Filer filer;

  @Inject ProcessorMessage logger;

  ObjectGraph objectGraph;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);

    objectGraph = ObjectGraph.create(new AnnotatedAdapterModule(env));
    objectGraph.inject(this);
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  private String getExecutionPath() throws UnsupportedEncodingException {
    String path = AnnotatedAdapterProcessor.class.getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");

    return decodedPath;
  }

  private String getWorkingDir() {
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();
    return s;
  }

  private String getExcecutionByClassLoader() {
    ClassLoader loader = AnnotatedAdapterProcessor.class.getClassLoader();
    URL url = loader.getResource(".");
    return url.getFile();
  }

  private String getByEnv() {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null);

    Iterable<? extends File> locations = fm.getLocation(StandardLocation.SOURCE_PATH);
    if (locations.iterator().hasNext()) {
      return locations.iterator().next().getAbsolutePath();
    }
    return null;
  }

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {

    // Search for annotated fields
    ViewTypeSearcher viewTypeSearcher = new ViewTypeSearcher(objectGraph);
    for (Element element : roundEnv.getElementsAnnotatedWith(ViewType.class)) {
      ViewType annotation = element.getAnnotation(ViewType.class);
      viewTypeSearcher.addElement(element, annotation);
    }

    List<AdapterInfo> recyclerApapters = new ArrayList<AdapterInfo>();

    // Code generators
    try {
      // Map of qualified adapter class name -> AdapterInfo
      Map<String, AdapterInfo> adapters = viewTypeSearcher.getAdapterInfos();
      for (AdapterInfo adapterInfo : adapters.values()) {

        if (adapterInfo.getAdapterType() == AdapterInfo.AdapterType.SUPPORT_RECYCLER_VIEW) {
          CodeGenerator codeGen = new RecyclerViewGenerator(objectGraph, adapterInfo, adapters);
          codeGen.generateCode();
          recyclerApapters.add(adapterInfo);
        } else {
          // TODO listview generator
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(null, "An error has occurred while generating adapters code! See stacktrace!");
    }

    // Generate adapter delegators
    if (!recyclerApapters.isEmpty()) {
      try {
        generateSupportRecyclerDelegators(recyclerApapters);
      } catch (IOException excpetion) {
        excpetion.printStackTrace();
        logger.error(recyclerApapters.get(0).getAdapterClass(),
            "An error has occurred while generating AutoAdapterDelegator: %s",
            excpetion.getMessage());
      }
    }
    return false;
  }

  private void generateSupportRecyclerDelegators(List<AdapterInfo> adapters) throws IOException {

    if (adapters.isEmpty()) {
      return;
    }

    Element element = adapters.get(0).getAdapterClass();
    JavaFileObject jfo =
        filer.createSourceFile(SupportRecyclerDelegators.AUTO_GENERATOR_QUALIFIED_NAME, element);
    Writer writer = jfo.openWriter();
    JavaWriter jw = new JavaWriter(writer);

    // Class things
    jw.emitPackage(SupportRecyclerDelegators.AUTO_GENERATOR_PACKAGE);
    jw.emitImports(
        "com.hannesdorfmann.annotatedadapter.recyclerview.SupportRecyclerAdapterDelegator",
        "com.hannesdorfmann.annotatedadapter.recyclerview.SupportAnnotatedAdapter");

    jw.emitJavadoc("Generated class by AnnotatedAdapter . Do not modify this code!");
    jw.beginType(SupportRecyclerDelegators.AUTO_GENERATOR_CLASS_NAME, "class",
        EnumSet.of(Modifier.PUBLIC), null, SupportRecyclerDelegators.class.getCanonicalName());

    jw.beginMethod("SupportRecyclerAdapterDelegator", "getDelegator", EnumSet.of(Modifier.PUBLIC),
        "SupportAnnotatedAdapter", "adapter");

    jw.emitEmptyLine();
    jw.emitStatement("String name = adapter.getClass().getCanonicalName()");
    jw.emitEmptyLine();

    for (AdapterInfo info : adapters) {
      jw.beginControlFlow("if (name.equals(\"%s\"))", info.getQualifiedAdapterClassName());
      jw.emitStatement("return new %s()", info.getQualifiedAdapterDelegatorClassName());
      jw.endControlFlow();
      jw.emitEmptyLine();
    }

    jw.emitStatement(
        "throw new RuntimeException(\"Could not find adapter delegate for \" + adapter)");
    jw.endMethod();
    jw.emitEmptyLine();

    jw.endType();

    jw.close();
  }
}
