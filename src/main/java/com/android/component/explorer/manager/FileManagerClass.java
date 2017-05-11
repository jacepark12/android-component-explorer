package com.android.component.explorer.manager;

import com.android.component.explorer.manager.exception.XmlLayoutNotFound;

import java.io.File;
import java.util.HashMap;

/**
 * Created by parkjaesung on 2017. 5. 8..
 */
/*
saves file name & file path
 */
public class FileManagerClass {

    private static FileManagerClass instance;
    private HashMap<String, File> xmlLayoutFile = new HashMap<String, File>();
    private FileManagerClass(){

    }

    public static FileManagerClass getInstance(){
        if(instance == null){
            return instance = new FileManagerClass();
        }
        return instance;
    }

    public void addXmlLayoutFile(String name, File file){
        this.xmlLayoutFile.put(name, file);
    }

    public HashMap<String, File> getXmlLayoutFile() {
        return xmlLayoutFile;
    }

    public File getXmlLayoutFileByName(String name) throws XmlLayoutNotFound {
        if(!this.xmlLayoutFile.containsKey(name)){
            throw new XmlLayoutNotFound("No " + name + " XML layout found in project ");
        }

        return this.xmlLayoutFile.get(name);
    }
}
