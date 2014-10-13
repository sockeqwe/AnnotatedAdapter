package com.hannesdorfmann.annotatedadapter.processor;

import com.google.auto.service.AutoService;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.processor.generator.CodeGenerator;
import com.hannesdorfmann.annotatedadapter.processor.generator.RecyclerViewGenerator;
import com.hannesdorfmann.annotatedadapter.processor.util.AnnotatedAdapterModule;
import com.hannesdorfmann.annotatedadapter.processor.util.ProcessorMessage;
import dagger.ObjectGraph;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaCompiler;
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
      viewTypeSearcher.addElementIfNotAlready(element);
    }

    // Code generators
    try {
      for (AdapterInfo adapterInfo : viewTypeSearcher.getAdapterInfos()) {

        if (adapterInfo.generatesCode()) {

          if (adapterInfo.getAdapterType() == AdapterInfo.AdapterType.RECYCLER_VIEW) {

            CodeGenerator codeGen = new RecyclerViewGenerator(objectGraph, adapterInfo);
            codeGen.generateCode();
          } else {
            // TODO listview generator
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      // TODO logger
    }

    return false;
  }
}
