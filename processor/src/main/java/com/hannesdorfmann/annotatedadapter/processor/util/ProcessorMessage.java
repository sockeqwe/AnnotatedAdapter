package com.hannesdorfmann.annotatedadapter.processor.util;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * This is a simple static helper class to print error, waring or note messages during annotation
 * processing.
 *
 * @author Hannes Dorfmann
 */
public class ProcessorMessage {

  private Messager messager;

  public ProcessorMessage(Messager messager) {
    this.messager = messager;
  }

  public void error(Element element, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    messager.printMessage(Diagnostic.Kind.ERROR, message, element);
  }

  public void warn(Element element, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    messager.printMessage(Diagnostic.Kind.WARNING, message, element);
  }

  public void note(Element element, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }
    messager.printMessage(Diagnostic.Kind.NOTE, message, element);
  }
}
