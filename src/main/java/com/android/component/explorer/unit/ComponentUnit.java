package com.android.component.explorer.unit;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by parkjaesung on 2017. 4. 14..
 */
public class ComponentUnit {

    public VirtualFile virtualFile = null;

    public void setVirtualFile(VirtualFile virtualFile){
        this.virtualFile = virtualFile;
    };
    public VirtualFile getVirtualFile(){
        return this.virtualFile;
    }
}
