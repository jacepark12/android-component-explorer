package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.UnitManager;
import com.github.javaparser.ParseException;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class FileHandler implements DirExplorer.FileHandler {

    private static FileHandler instance = new FileHandler();

    UnitManager unitManager = UnitManager.getInstance();
    ClassParser classParser = ClassParser.getInstance();

    Set<String> activityClassNames = new HashSet<String>();

    private FileHandler(){
        try {

            //System.out.println(String.valueOf(getClass().getResourceAsStream("activity_classes.properties")));
            File file = new File(ClassLoader.getSystemClassLoader().getResource("activity_classes.properties").getPath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader((fileReader));

            String line;
            while((line = bufferedReader.readLine())!= null){
                activityClassNames.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileHandler getInstance(){
        return instance;
    }

    public void handle(int level, String path, File file) {
        try {
            classParser.getParentClassName(file);
            System.out.println("Analyzed " + path + " Parent class name : " + classParser.getParentClassName(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getActivityClassNames() {
        return activityClassNames;
    }
}
