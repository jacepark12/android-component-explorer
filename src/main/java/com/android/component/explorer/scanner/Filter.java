package com.android.component.explorer.scanner;

import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class Filter implements DirExplorer.Filter {
    private ArrayList<String> extension_filter = new ArrayList<String>(Arrays.asList("java"));
    private ArrayList<String> filter_name = new ArrayList<String>(Arrays.asList("package-info.java"));
    public boolean interested(int level, String path, File file) {
        // Get filter only java class files
        //TODO Refactor
        if(!path.contains("package-info") &&extension_filter.contains(Files.getFileExtension(file.getName()))){
            return true;
        }
        return false;
    }
}
