package com.hannesdorfmann.annotatedadapter.processor.util;

import com.hannesdorfmann.annotatedadapter.processor.AnnotatedAdapterProcessor;
import com.hannesdorfmann.annotatedadapter.processor.ViewTypeSearcher;
import com.hannesdorfmann.annotatedadapter.processor.generator.RecyclerViewGenerator;
import dagger.Module;
import dagger.Provides;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author Hannes Dorfmann
 */
@Module(
    injects = {
        AnnotatedAdapterProcessor.class, ViewTypeSearcher.class, RecyclerViewGenerator.class
    }

    , library = true // TODO remove
)
public class AnnotatedAdapterModule {

  private ProcessingEnvironment environment;

  public AnnotatedAdapterModule(ProcessingEnvironment environment) {
    this.environment = environment;
  }

  @Provides
  public Types providesTypes() {
    return environment.getTypeUtils();
  }

  @Provides
  public Elements providesElements() {
    return environment.getElementUtils();
  }

  @Singleton @Provides
  public ProcessorMessage provideProcessorMessage() {
    return new ProcessorMessage(environment.getMessager());
  }

  @Provides
  public Filer providesFiler() {
    return environment.getFiler();
  }

  @Provides @Singleton
  public TypeHelper providesTypeHelper() {
    return new TypeHelper(environment.getElementUtils(), environment.getTypeUtils());
  }
}
