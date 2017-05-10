package com.android.component.explorer.manager;

import java.util.HashMap;

/**
 * Created by parkjaesung on 2017. 5. 10..
 */
public class ClassAndParentManager {

    private HashMap<String, String> classAndParent = new HashMap<String, String>();

    private static ClassAndParentManager instance;

    private ClassAndParentManager(){

    }

    public static ClassAndParentManager getInstance(){
        if(instance == null){
            return instance = new ClassAndParentManager();
        }
        return instance;
    }

    public void setClassAndParent(String childPackage, String parentPackage){
        this.classAndParent.put(childPackage, parentPackage);
    }

    public String getParentPackageByChild(String child){
        return this.classAndParent.get(child);
    }

}
