package com.android.component.explorer.manager;

import com.android.component.explorer.manager.exception.XmlLayoutNotFound;

import java.util.HashMap;

/**
 * Created by parkjaesung on 2017. 5. 8..
 */
public class FilePathManagerClass {

    private static FilePathManagerClass instance;
    private HashMap<String, String> xmlLayoutPath = new HashMap<String, String>();
    private FilePathManagerClass(){

    }

    public static FilePathManagerClass getInstance(){
        if(instance == null){
            return instance = new FilePathManagerClass();
        }
        return instance;
    }

    public void addXMLLayoutPath(String name, String path){
        this.xmlLayoutPath.put(name, path);
    }

    public HashMap<String, String> getXmlLayoutPath() {
        return xmlLayoutPath;
    }

    public String getXmlLayoutPathByName(String name) throws XmlLayoutNotFound {
        if(!this.xmlLayoutPath.containsKey(name)){
            throw new XmlLayoutNotFound("No" + name + "XML layout found in project ");
        }

        return this.xmlLayoutPath.get(name);
    }
}
