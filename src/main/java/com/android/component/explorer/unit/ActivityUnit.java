package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class ActivityUnit extends ViewComponentUnit {

    public ActivityUnit(){

    }

    public ActivityUnit(String name, VirtualFile virtualFile){
        super(name, virtualFile);
    }

    public ActivityUnit(String name, VirtualFile virtualFile, LayoutUnit layoutUnit){
        super(name, virtualFile, layoutUnit);
    }

    @Override
    public String toString() {
        return super.getName()+".java";
    }
}
