package com.android.viewbinder.scanner;

import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class Filter implements DirExplorer.Filter {
    private ArrayList<String> extension_filter = new ArrayList<String>(Arrays.asList("java"));
    public boolean interested(int level, String path, File file) {
        // Get filter only java class files
        if(extension_filter.contains(Files.getFileExtension(file.getName()))){
            return true;
        }
        return false;
    }
}
