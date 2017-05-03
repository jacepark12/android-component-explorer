package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.UnitManager;
import com.android.component.explorer.unit.ActivityUnit;
import com.android.component.explorer.unit.FragmentUnit;
import com.github.javaparser.ParseException;
import com.google.common.io.Files;
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
        activityClassNames.add("Activity");
        activityClassNames.add("AppCompatActivity");

        fragmentClassNames.add("Fragment");
        fragmentClassNames.add("DialogFragment");
        //TODO : Issue-> unable to read property file while running idea
        //activityClassNames = readPropertyByLine("activity_classes.properties");
        //fragmentClassNames = readPropertyByLine("fragment_classes.properties");
    }

    public static FileHandler getInstance(){
        return instance;
    }

    public void handle(int level, String path, File file) {
        String extension = Files.getFileExtension(file.getName());

        if (extension.equals("java")){
            handleJava(level, path, file);
        }else if(extension.equals("xml")){
            handleXML(level, path, file);
        }
    }

    private void handleJava(int level, String path, File file){
        String packageString = "";
        try {
            packageString = classParser.getParentClassName(file);
            System.out.println("Analyzed " + path + " Parent class name : " + classParser.getParentClassName(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(isAndroidPackage(packageString)) {
            if (activityClassNames.contains(getClassNameFromPackage(packageString))) {
                ActivityUnit activityUnit = new ActivityUnit(getFileName(file), LocalFileSystem.getInstance().findFileByIoFile(file));
                unitManager.addActivity(getFileName(file), activityUnit);
            } else if (fragmentClassNames.contains(getClassNameFromPackage(packageString))) {
                FragmentUnit fragmentUnit = new FragmentUnit(getFileName(file), LocalFileSystem.getInstance().findFileByIoFile(file));
                unitManager.addFragment(getFileName(file), fragmentUnit);
            }
        }
    }

    private void handleXML(int level, String path, File file){

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

    public String getClassNameFromPackage(String packageString) {
        String[] parse = packageString.split("\\.");
        if(parse.length == 0){
            return packageString;
        }else{
            return parse[parse.length-1];
        }
    }

    public boolean isAndroidPackage(String packageString){
        boolean result = true;

        String[] parse = packageString.split("\\.");

        if(parse.length > 1){
          if(!parse[0].equals("android")){
              result = false;
          }
        }

        return result;
    }
}
