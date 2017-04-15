package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.UnitManager;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class CalledFragmentScanner {

    private static CalledFragmentScanner instance;

    private CalledFragmentScanner(){

    }

    public static CalledFragmentScanner getInstance(){
        if(instance == null){
            return instance= new CalledFragmentScanner();
        }

        return instance;
    }

    public List<String> getCalledFragments(File file) throws FileNotFoundException, ParseException {

        ArrayList<String> fragmentInstanceNames = new ArrayList<String>();
        ArrayList<String> activityInstanceNames = new ArrayList<String>();

        ArrayList<String> classSources = getClassSourceList(file);

        //Framgent 객체를 만드는지 파싱


        return null;
    }

    public ArrayList<String> getFragmentInstanceNames(ArrayList<String> classSources){
        ArrayList<String> fragmentInstanceNames = new ArrayList<String>();
        UnitManager unitManager = UnitManager.getInstance();

        for (String source: classSources) {
            System.out.println("source : " + source);

            List<String> fragmentNames = new ArrayList<String>(unitManager.getFragments().keySet());
            for(String fragmentName : fragmentNames){
                if(source.contains(fragmentName) && source.contains("=")){
                    fragmentInstanceNames.add(source.split(" ")[1]);
                }
            }
        }

        return fragmentInstanceNames;
    }


    //TODO Use CompliatioUnit util with DI
    private CompilationUnit getCompilationUnit(File file) throws ParseException, FileNotFoundException {
        FileInputStream in = new FileInputStream(file);
        CompilationUnit compilationunit = JavaParser.parse(in);

        return compilationunit;
    }

    @NotNull
    private ArrayList<String> getClassSourceList(File classFile) throws ParseException, FileNotFoundException {
        CompilationUnit compilationunit = getCompilationUnit(classFile);
        String classSource = compilationunit.getTypes().get(0).toString();
        return new ArrayList<String>(Arrays.asList(classSource.split("\n")));
    }

    @NotNull
    private ArrayList<String> getClassSourceList(CompilationUnit compilationunit) throws ParseException, FileNotFoundException {
        String classSource = compilationunit.getTypes().get(0).toString();
        return new ArrayList<String>(Arrays.asList(classSource.split("\n")));
    }
}
