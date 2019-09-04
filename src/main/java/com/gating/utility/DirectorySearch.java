package com.gating.utility;

import java.io.File;
import java.util.List;

public class DirectorySearch {

  public static void searchFilesInDirectory(final String pattern, final File folder, List<String> result) {

    for (final File file : folder.listFiles()) {
      if (file.isDirectory()) {
        searchFilesInDirectory(pattern, file, result);
      }
      if (file.isFile() && file.getName().matches(pattern)) {
        result.add(file.getAbsolutePath());
      }
    }
  }
}
