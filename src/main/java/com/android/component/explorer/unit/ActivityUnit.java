package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class ActivityUnit extends ComponentUnit {

    //Every Activity and Fragment unit has one layoutUnit(layout xml)
    private LayoutUnit layoutUnit;

    public ActivityUnit(){

    }

    public ActivityUnit(String name){
        super(name);
    }

    public ActivityUnit(String name, VirtualFile virtualFile){
        super(name, virtualFile);
    }

    public ActivityUnit(String name, VirtualFile virtualFile, LayoutUnit layoutUnit){
        super(name, virtualFile);
        this.layoutUnit = layoutUnit;
    }

    public LayoutUnit getLayoutUnit() {
        return layoutUnit;
    }

    public void setLayoutUnit(LayoutUnit layoutUnit) {
        this.layoutUnit = layoutUnit;
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
