package com.android.component.explorer.scanner.parser;

import com.android.component.explorer.scanner.exception.ClassParseException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
/*
Get class name, parent name from class file
 */
public class ClassParser {

    /**
     * The constant instance.
     */
    private static ClassParser instance;

    private ClassParser() {

    }

    /**
     * Get instance class parser.
     *
     * @return the class parser
     */
    public static ClassParser getInstance() {

        if (instance == null) {
            return instance = new ClassParser();
        }
        return instance;
    }

    /**
     * Gets class name.
     *
     * @param classFile the class file
     * @return the class name
     * @throws FileNotFoundException the file not found exception
     * @throws ParseException        the parse exception
     */
    public String getClassName(File classFile) throws FileNotFoundException, ParseException {
        CompilationUnit compilationunit = getCompilationUnit(classFile);

        return compilationunit.getTypes().get(0).getName();
    }

    /**
     * Get class name string.
     *
     * @param compilationunit the compilationunit
     * @return the string
     */
    public String getClassName(CompilationUnit compilationunit) {
        return compilationunit.getTypes().get(0).getName();
    }


    /**
     * Gets parent class name.
     *
     * @param classFile the class file
     * @return the parent class name
     * @throws FileNotFoundException the file not found exception
     * @throws ParseException        the parse exception
     */
    public String getParentClassName(File classFile) throws FileNotFoundException, ParseException {
        ArrayList<String> classSources = getClassSourceList(classFile);

        return classSources.get(classSources.indexOf("extends") + 1);
    }

    /**
     * Gets parent class name.
     *
     * @param compilationunit the compilationunit
     * @return the parent class name
     * @throws FileNotFoundException the file not found exception
     * @throws ParseException        the parse exception
     */
    public String getParentClassName(CompilationUnit compilationunit) throws FileNotFoundException, ParseException {
        ArrayList<String> classSources = getClassSourceList(compilationunit);

        return classSources.get(classSources.indexOf("extends") + 1);
    }

    /**
     * Gets interface name.
     *
     * @param classFile the class file
     * @return the interface name
     * @throws FileNotFoundException the file not found exception
     * @throws ParseException        the parse exception
     */
    public String getInterfaceName(File classFile) throws FileNotFoundException, ParseException {
        ArrayList<String> classSources = getClassSourceList(classFile);

        return classSources.get(classSources.indexOf("implements") + 1);
    }

    /**
     * Gets interface name.
     *
     * @param compilationunit the compilationunit
     * @return the interface name
     * @throws FileNotFoundException the file not found exception
     * @throws ParseException        the parse exception
     */
    public String getInterfaceName(CompilationUnit compilationunit) throws FileNotFoundException, ParseException {
        ArrayList<String> classSources = getClassSourceList(compilationunit);

        return classSources.get(classSources.indexOf("implements") + 1);
    }

    public String getLayoutXMLName(File classFile) throws FileNotFoundException, ParseException, ClassParseException {
        String result = "";
        ArrayList<String> classSources = getClassLineByLine(classFile);

        //search from back of sources
        //setContentView method call can be duplicated, so should get last called layout

        Pattern inflatePattern = Pattern.compile("[a-z]*?.inflate\\(\\S+\\);");
        Pattern setContentViewPattern = Pattern.compile("[a-z]*.?setContentView\\(\\S+\\);");

        for (int i = classSources.size() - 1; i >= 0; i--) {
            String classSource = classSources.get(i);
            classSource = classSource.replace(" ", "");
            System.out.println("classSource : " + classSource);
            Matcher matcher1 = inflatePattern.matcher(classSource);
            Matcher matcher2 = setContentViewPattern.matcher(classSource);

            if (matcher1.find()) {
                result = getLayoutXMLNameFromMethod(classSource.substring(matcher1.start(), matcher1.end()));
                break;
            } else if (matcher2.find()) {
                result = getLayoutXMLNameFromMethod(classSource.substring(matcher2.start(), matcher2.end()));
                break;
            }
        }

        if (result.equals("")) {
            throw new ClassParseException("No XML layout found in component class : " + classFile.getName());
        }

        return result;
    }

    public String getLayoutXMLNameFromMethod(String methodString) {
        Pattern pattern = Pattern.compile("\\(\\S+\\)");
        Matcher matcher = pattern.matcher(methodString);
        matcher.find();

        String tmp = methodString.substring(matcher.start(), matcher.end());
        tmp = tmp.replaceAll("[()]", "");

        return tmp.split(",")[0].split("\\.")[2];
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
        return new ArrayList<String>(Arrays.asList(classSource.split(" ")));
    }

    private ArrayList<String> getClassLineByLine(File classFile){
        ArrayList<String> result = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(classFile));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @NotNull
    private ArrayList<String> getClassSourceList(CompilationUnit compilationunit) throws ParseException, FileNotFoundException {
        String classSource = compilationunit.getTypes().get(0).toString();
        return new ArrayList<String>(Arrays.asList(classSource.split(" ")));
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

    public String getClassNameFromPackage(String packageString) {
        String[] parse = packageString.split("\\.");
        if(parse.length == 0){
            return packageString;
        }else{
            return parse[parse.length-1];
        }
    }
}
