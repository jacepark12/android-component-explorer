package com.android.component.explorer.scanner;

import com.google.common.io.Files;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class Filter implements DirExplorer.Filter {
    private Set<String> extension_filter = new HashSet<String>();
    private Set<String> filter_name = new HashSet<String>();

    private static Filter instance;

    private Filter(){
        this.extension_filter.add("java");
        this.extension_filter.add("xml");

        this.filter_name.add("package-info.java");
    }

    public static Filter getInstance(){
        if(instance == null){
            return instance = new Filter();
        }
        return instance;
    }

    public boolean interested(int level, String path, File file) {
        boolean result = true;

        // Get filter only java class files
        //TODO Refactor
        //check extension
        if(!extension_filter.contains(Files.getFileExtension(file.getName()))){
            result = false;
        }

        if(filter_name.contains(file.getName())){
            result = false;
        }

        //explore /app/src directory
        if(!path.startsWith("/app/src")){
           result = false;
        }

        return result;
    }
}
