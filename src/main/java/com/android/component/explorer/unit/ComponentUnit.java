package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 4. 14..
 */
public class ComponentUnit {

    private VirtualFile virtualFile = null;
    private String name;

    public ComponentUnit(){

    }

    public ComponentUnit(String name){
        this.name = name;
    }

    public ComponentUnit(String name, VirtualFile virtualFile){
        this.name = name;
        this.virtualFile = virtualFile;
    }
    public void setVirtualFile(VirtualFile virtualFile){
        this.virtualFile = virtualFile;
    };
    public VirtualFile getVirtualFile(){
        return this.virtualFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
