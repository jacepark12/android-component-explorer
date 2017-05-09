package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.FileManagerClass;
import com.android.component.explorer.manager.UnitManager;
import com.android.component.explorer.manager.exception.XmlLayoutNotFound;
import com.android.component.explorer.scanner.exception.ClassParseException;
import com.android.component.explorer.unit.ActivityUnit;
import com.android.component.explorer.unit.FragmentUnit;
import com.android.component.explorer.unit.LayoutUnit;
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
    FileManagerClass fileManagerClass = FileManagerClass.getInstance();

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
        }else if(extension.equals("xml") && isLayoutResourceDir(path)){
            handleLayoutXML(level, path, file);
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
                //save layoutUnit
                try {
                    activityUnit.setLayoutUnit(getLayoutUnitFromComponent(file));
                    System.out.println("SetLayoutUnit of : " + file.getName());
                } catch (XmlLayoutNotFound xmlLayoutNotFound) {
                    xmlLayoutNotFound.printStackTrace();
                }

                unitManager.addActivity(getFileName(file), activityUnit);
            } else if (fragmentClassNames.contains(getClassNameFromPackage(packageString))) {
                FragmentUnit fragmentUnit = new FragmentUnit(getFileName(file), LocalFileSystem.getInstance().findFileByIoFile(file));

                try {
                    fragmentUnit.setLayoutUnit(getLayoutUnitFromComponent(file));
                } catch (XmlLayoutNotFound xmlLayoutNotFound) {
                    xmlLayoutNotFound.printStackTrace();
                }

                unitManager.addFragment(getFileName(file), fragmentUnit);
            }
        }
    }

    private LayoutUnit getLayoutUnitFromComponent(File classFile) throws XmlLayoutNotFound {
        String layoutFileName = "";
        try {
            layoutFileName = classParser.getLayoutXMLName(classFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassParseException e) {
            e.printStackTrace();
        }
        System.out.println("getLayoutUnitFromComponent : " + layoutFileName);
        return new LayoutUnit(layoutFileName, LocalFileSystem.getInstance().findFileByIoFile(fileManagerClass.getXmlLayoutFileByName(layoutFileName)));
    }

    private void handleLayoutXML(int level, String path, File file){
        System.out.println("Save xml layout : " + file.getName());
        //save layout xml to FileManagerClass
        fileManagerClass.addXmlLayoutFile(getFileName(file), file);
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

    private boolean isLayoutResourceDir(String path){
        //app/src/main/res/layout/~~~.xml
        //-> check if xml file is in layout dir or not
        path = path.substring(0,path.lastIndexOf("/"));
        path = path.substring(path.lastIndexOf("/")+1);

        System.out.println("isLayoutResourceDir : " + path);
        return path.equals("layout");
    }
}
