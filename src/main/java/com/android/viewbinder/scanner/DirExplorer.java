package com.android.viewbinder.scanner;

import java.io.File;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class DirExplorer {
    public interface FileHandler {
        void handle(int level, String path, File file);
    }

    public interface Filter {
        boolean interested(int level, String path, File file);
    }

    private FileHandler fileHandler;
    private Filter filter;

    public DirExplorer(Filter filter, FileHandler fileHandler) {
        this.filter = filter;
        this.fileHandler = fileHandler;
    }

    public DirExplorer(){

    }

    public void explore(File root) {
        explore(0, "", root);
    }

    private void explore(int level, String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                explore(level + 1, path + "/" + child.getName(), child);
            }
        } else {
            if (filter.interested(level, path, file)) {
                fileHandler.handle(level, path, file);
            }
            System.out.println(path);
        }
    }

}