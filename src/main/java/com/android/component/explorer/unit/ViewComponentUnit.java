package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 5. 8..
 */
public class ViewComponentUnit extends ComponentUnit {

    //Every Activity and Fragment unit has one layoutUnit(layout xml)
    LayoutUnit layoutUnit;

    public ViewComponentUnit(){

    }

    public ViewComponentUnit(String name, VirtualFile virtualFile, LayoutUnit layoutUnit){
        super(name, virtualFile);
        this.layoutUnit = layoutUnit;
    }

    public ViewComponentUnit(String name, VirtualFile virtualFile){
        super(name, virtualFile);
    }

    public boolean hasLayoutUnit(){
        if(this.layoutUnit == null){
            return false;
        }else{
            return true;
        }
    }

    public void setLayoutUnit(LayoutUnit layoutUnit) {
        this.layoutUnit = layoutUnit;
    }

    public LayoutUnit getLayoutUnit() {
        return layoutUnit;
    }
}
