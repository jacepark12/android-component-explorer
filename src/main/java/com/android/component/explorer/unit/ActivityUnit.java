package com.android.component.explorer.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class ActivityUnit extends ComponentUnit {

    private String name;
    private List<FragmentUnit> fragments = new ArrayList<FragmentUnit>();

    public ActivityUnit(){

    }

    public ActivityUnit(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
