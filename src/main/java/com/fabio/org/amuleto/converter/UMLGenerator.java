package com.fabio.org.amuleto.converter;

import java.io.File;

import net.sourceforge.plantuml.FileFormat;

public class UMLGenerator {

  public static void generateFromDirectory(File sourceDir, File outputFile) throws Exception {
    UMLTextGenerator.generateFromDirectory(sourceDir, outputFile);
  }

  public static void generateVectorFromDirectory(
      File sourceDir, File outputVectorFile, FileFormat vectorFormat) throws Exception {
    UMLVectorGenerator.generateVectorFromDirectory(sourceDir, outputVectorFile, vectorFormat);
  }
}
