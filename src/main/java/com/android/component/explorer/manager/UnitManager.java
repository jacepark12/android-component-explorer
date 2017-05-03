package com.android.component.explorer.manager;

import com.android.component.explorer.unit.ActivityUnit;
import com.android.component.explorer.unit.FragmentUnit;

import java.util.HashMap;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class UnitManager {
    private static UnitManager instance;

    private HashMap<String, ActivityUnit> activities = new HashMap<String, ActivityUnit>();
    private HashMap<String, FragmentUnit> fragments = new HashMap<String, FragmentUnit>();

    private UnitManager(){

    }

    public static UnitManager getInstance(){
        if(instance == null){
            return instance = new UnitManager();
        }
        return instance;
    }

    public void addActivity(String activityName, ActivityUnit activityUnit){
        this.activities.put(activityName, activityUnit);
    }

    public void addFragment(String fragmentName, FragmentUnit fragmentUnit){
        this.fragments.put(fragmentName, fragmentUnit);
    }

    public boolean hasActivity(String activityName){
        return this.activities.containsKey(activityName);
    }

    public HashMap<String, ActivityUnit> getActivities() {
        return activities;
    }

    public ActivityUnit getActivity(String activityName){
        return this.activities.get(activityName);
    }

    public HashMap<String, FragmentUnit> getFragments() {
        return fragments;
    }

    public FragmentUnit getFragment(String fragmentName){
        return this.fragments.get(fragmentName);
    }

    public void clearUnits(){
        this.activities.clear();
        this.fragments.clear();
    }
}
