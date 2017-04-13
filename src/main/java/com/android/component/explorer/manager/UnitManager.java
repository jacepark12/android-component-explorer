package com.android.component.explorer.manager;

import com.android.component.explorer.unit.ActivityUnit;
import com.android.component.explorer.unit.FragmentUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class UnitManager {
    private static UnitManager instance;
    private List<ActivityUnit> activities = new ArrayList<ActivityUnit>();
    private List<FragmentUnit> fragments = new ArrayList<FragmentUnit>();

    private UnitManager(){

    }

    public static UnitManager getInstance(){
        if(instance == null){
            return instance = new UnitManager();
        }
        return instance;
    }

    public void addActivities(ActivityUnit activityUnit){
        this.activities.add(activityUnit);
    }

    public void addFragment(FragmentUnit fragmentUnit){
        this.fragments.add(fragmentUnit);
    }

    public boolean hasActivity(String activityName){
        boolean result = false;
        for (ActivityUnit activityUnit: activities) {
            if(activityUnit.getName().equals(activityName)){
                result = true;
                break;
            }
        }

        return result;
    }

    public List<ActivityUnit> getActivities() {
        return activities;
    }

    public List<FragmentUnit> getFragments() {
        return fragments;
    }
}
