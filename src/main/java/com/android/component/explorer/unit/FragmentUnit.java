package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class FragmentUnit extends ComponentUnit {

    private String name;

    public FragmentUnit(String name){
        this.name = name;
    }

    public FragmentUnit(String name, VirtualFile virtualFile){
        super(name, virtualFile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
