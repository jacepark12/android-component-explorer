package com.android.component.explorer.scanner;

import com.google.common.io.Files;

import java.io.File;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class DirExplorer {

    // if handle is true, explore all dir and handle all file
    private boolean handleAll = false;

    public interface FileHandler {
        void handle(int level, String path, File file);
        void preHandle(int level, String path, File file);
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

    public boolean isHandleAll() {
        return handleAll;
    }

    public void setHandleAll(boolean handleAll) {
        this.handleAll = handleAll;
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
            if (handleAll) {
                System.out.println("handling : " + path);
                fileHandler.handle(level, path, file);
            } else if (filter.interested(level, path, file)) {
                System.out.println("handling : " + path);
                fileHandler.handle(level, path, file);
            }
            System.out.println(path);
        }
    }

    //preExplore executes tasks which should be executed before main exploring
    public void preExplore(File root) {
        preExplore(0, "", root);
    }

    private void preExplore(int level, String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                preExplore(level + 1, path + "/" + child.getName(), child);
            }
        } else {
            if (Files.getFileExtension(file.getName()).equals("java")){
                this.fileHandler.preHandle(level, path, file);
            }
        }
    }
}