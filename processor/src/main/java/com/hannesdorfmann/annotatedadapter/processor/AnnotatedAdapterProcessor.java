package com.hannesdorfmann.annotatedadapter.processor;

import com.google.auto.service.AutoService;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.processor.util.AnnotatedAdapterModule;
import com.hannesdorfmann.annotatedadapter.processor.util.ProcessorMessage;
import dagger.ObjectGraph;
import java.io.UnsupportedEncodingException;
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

/**
 * AnnotationProcessor for AnnotatedAdapter @ViewType
 *
 * @author Hannes Dorfmann
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.hannesdorfmann.annotatedadapter.annotation.ViewType")
public class AnnotatedAdapterProcessor extends AbstractProcessor {

  @Inject
  Elements elementUtils;

  @Inject
  Types typeUtils;

  @Inject
  Filer filer;

  @Inject
  ProcessorMessage logger;

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

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnv) {

    ViewTypeSearcher viewTypeSearcher = new ViewTypeSearcher(objectGraph);
    for (Element element : roundEnv.getElementsAnnotatedWith(ViewType.class)) {

    }

    return false;
  }
}
