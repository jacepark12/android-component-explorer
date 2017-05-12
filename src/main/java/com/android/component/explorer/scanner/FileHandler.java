package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.ClassAndParentManager;
import com.android.component.explorer.manager.FileManagerClass;
import com.android.component.explorer.manager.UnitManager;
import com.android.component.explorer.manager.exception.XmlLayoutNotFound;
import com.android.component.explorer.scanner.exception.ClassParseException;
import com.android.component.explorer.scanner.parser.ClassParser;
import com.android.component.explorer.unit.ActivityUnit;
import com.android.component.explorer.unit.FragmentUnit;
import com.android.component.explorer.unit.LayoutUnit;
import com.github.javaparser.ParseException;
import com.google.common.io.Files;
import com.intellij.openapi.vfs.LocalFileSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class FileHandler implements DirExplorer.FileHandler {

    private static FileHandler instance = new FileHandler();

    private UnitManager unitManager = UnitManager.getInstance();
    private ClassParser classParser = ClassParser.getInstance();
    private FileManagerClass fileManagerClass = FileManagerClass.getInstance();
    private ClassAndParentManager classAndParentManager = ClassAndParentManager.getInstance();

    private Set<String> activityClassNames = new HashSet<String>();
    private Set<String> fragmentClassNames = new HashSet<String>();

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

    //Method for executing tasks which should be executed before main handling
    public void preHandle(int level, String path, File file) {
        //save class and parent class

        if(classParser.hasParentClass(file)) {
            try {
                classAndParentManager.setClassAndParent(classParser.getFullClassName(file), classParser.getParentClassFullPackage(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
        if(!classParser.hasParentClass(file)){
            return;
        }
        String parentPackage = "";
        String classPackage = classParser.getFullClassName(file);
        try {
            parentPackage = classParser.getParentClassFullPackage(file);
            System.out.println("Analyzed " + path + " Parent class name : " + classParser.getParentClassName(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(classParser.isAndroidPackage(parentPackage)){
            handleViewComponentClass(parentPackage, file);
        }else{
            String tmpParentPackage = parentPackage;
            while(true){
                if(classAndParentManager.hasChild(tmpParentPackage)){
                    System.out.println("hasChild : " + tmpParentPackage);
                    tmpParentPackage = classAndParentManager.getParentPackageByChild(tmpParentPackage);
                    System.out.println("getParentPackageByChild : " + tmpParentPackage);

                    if(classParser.isAndroidPackage(tmpParentPackage)){

                        System.out.println("this is androidPackage : " + tmpParentPackage);
                        handleViewComponentClass(tmpParentPackage, file);
                        break;
                    }
                }else{
                    break;
                }
            }
        }

//        if(classParser.isAndroidPackage(packageString)) {
//            if (activityClassNames.contains(classParser.getClassNameFromPackage(packageString))) {
//                ActivityUnit activityUnit = new ActivityUnit(getFileName(file), LocalFileSystem.getInstance().findFileByIoFile(file));
//                //save layoutUnit
//                try {
//                    activityUnit.setLayoutUnit(getLayoutUnitFromComponent(file));
//                    System.out.println("SetLayoutUnit of : " + file.getName());
//                } catch (XmlLayoutNotFound xmlLayoutNotFound) {
//                    xmlLayoutNotFound.printStackTrace();
//                }
//
//                unitManager.addActivity(getFileName(file), activityUnit);
//            } else if (fragmentClassNames.contains(classParser.getClassNameFromPackage(packageString))) {
//                FragmentUnit fragmentUnit = new FragmentUnit(getFileName(file), LocalFileSystem.getInstance().findFileByIoFile(file));
//
//                try {
//                    fragmentUnit.setLayoutUnit(getLayoutUnitFromComponent(file));
//                } catch (XmlLayoutNotFound xmlLayoutNotFound) {
//                    xmlLayoutNotFound.printStackTrace();
//                }
//
//                unitManager.addFragment(getFileName(file), fragmentUnit);
//            }
//        }
    }

    private void handleViewComponentClass(String parentPackage, File classFile){
        System.out.println("handleViewComponentClass : " + parentPackage);
        if(activityClassNames.contains(classParser.getClassNameFromPackage(parentPackage))){
            ActivityUnit activityUnit = new ActivityUnit(getFileName(classFile), LocalFileSystem.getInstance().findFileByIoFile(classFile));
            //save layoutUnit
            try {
                activityUnit.setLayoutUnits(getLayoutUnitFromComponents(classFile));
                System.out.println("SetLayoutUnit of : " + classFile.getName());
            } catch (XmlLayoutNotFound xmlLayoutNotFound) {
                xmlLayoutNotFound.printStackTrace();
            }
            unitManager.addActivity(getFileName(classFile), activityUnit);
        }else if(fragmentClassNames.contains(classParser.getClassNameFromPackage(parentPackage))){
            FragmentUnit fragmentUnit = new FragmentUnit(getFileName(classFile), LocalFileSystem.getInstance().findFileByIoFile(classFile));

            try {
                fragmentUnit.setLayoutUnits(getLayoutUnitFromComponents(classFile));
            } catch (XmlLayoutNotFound xmlLayoutNotFound) {
                xmlLayoutNotFound.printStackTrace();
            }

            unitManager.addFragment(getFileName(classFile), fragmentUnit);
        }
    }

    private ArrayList<LayoutUnit> getLayoutUnitFromComponents(File classFile) throws XmlLayoutNotFound {
        ArrayList<LayoutUnit> result = new ArrayList<LayoutUnit>();
        ArrayList<String> layoutFileNames = new ArrayList<String>();
        try {
            layoutFileNames = classParser.getLayoutXMLNames(classFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassParseException e) {
            e.printStackTrace();
        }

        for (String layoutFileName : layoutFileNames){
            System.out.println("getLayoutUnitFromComponent : " + layoutFileName);
            result.add(new LayoutUnit(layoutFileName, LocalFileSystem.getInstance().findFileByIoFile(fileManagerClass.getXmlLayoutFileByName(layoutFileName))));
        }

        return result;
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

            bufferedReader.close();
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

    private boolean isLayoutResourceDir(String path){
        //app/src/main/res/layout/~~~.xml
        //-> check if xml file is in layout dir or not
        path = path.substring(0,path.lastIndexOf("/"));
        path = path.substring(path.lastIndexOf("/")+1);

        System.out.println("isLayoutResourceDir : " + path);
        return path.equals("layout");
    }
}
