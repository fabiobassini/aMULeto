package com.fabio.org.amuleto.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UMLFileUtils {
  public static List<File> getJavaFiles(File dir) {
    List<File> files = new ArrayList<>();
    File[] entries = dir.listFiles();
    if (entries != null) {
      for (File entry : entries) {
        if (entry.isDirectory()) {
          files.addAll(getJavaFiles(entry));
        } else if (entry.getName().endsWith(".java")) {
          files.add(entry);
        }
      }
    }
    return files;
  }
}
