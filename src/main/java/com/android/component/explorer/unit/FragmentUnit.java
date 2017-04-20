package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class FragmentUnit extends ComponentUnit {

    public FragmentUnit(String name){
        super(name);
    }

    public FragmentUnit(String name, VirtualFile virtualFile){
        super(name, virtualFile);
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
