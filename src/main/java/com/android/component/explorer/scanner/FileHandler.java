package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.UnitManager;
import com.android.component.explorer.unit.ActivityUnit;
import com.github.javaparser.ParseException;
import com.intellij.openapi.vfs.LocalFileSystem;

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
    Set<String> fragmentClassNames = new HashSet<String>();

    private FileHandler(){
        activityClassNames = readPropertyByLine("activity_classes.properties");
        fragmentClassNames = readPropertyByLine("fragment_classes.properties");
    }

    public static FileHandler getInstance(){
        return instance;
    }

    public void handle(int level, String path, File file) {
        String className = "";
        try {
             className = classParser.getParentClassName(file);
            System.out.println("Analyzed " + path + " Parent class name : " + classParser.getParentClassName(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(activityClassNames.contains(className)){
            ActivityUnit activityUnit = new ActivityUnit(getFileName(file), LocalFileSystem.getInstance().findFileByIoFile(file));
            unitManager.addActivity(getFileName(file), activityUnit);
        }else if(fragmentClassNames.contains(className)){
        }
    }

    public Set<String> getActivityClassNames() {
        return activityClassNames;
    }

    public Set<String> getFragmentClassNames() {
        return fragmentClassNames;
    }

    public Set<String> readPropertyByLine(String fileName){
        HashSet<String> result = new HashSet<String>();
        try {

            //System.out.println(String.valueOf(getClass().getResourceAsStream("activity_classes.properties")));
            File file = new File(ClassLoader.getSystemClassLoader().getResource(fileName).getPath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader((fileReader));

            String line;
            while((line = bufferedReader.readLine())!= null){
                result.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getFileName(File file){
        StringBuilder sb = new StringBuilder(file.getName());

        return sb.substring(0, sb.lastIndexOf("."));
    }
}
