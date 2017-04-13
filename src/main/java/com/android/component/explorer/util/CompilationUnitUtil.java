package com.android.component.explorer.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class CompilationUnitUtil {

    public CompilationUnit getCompilationUnit(File file) throws ParseException, FileNotFoundException {
        FileInputStream in = new FileInputStream(file);
        CompilationUnit compilationunit = JavaParser.parse(in);

        return compilationunit;
    }

    @NotNull
    public ArrayList<String> getClassSourceList(File classFile) throws ParseException, FileNotFoundException {
        CompilationUnit compilationunit = getCompilationUnit(classFile);
        String classSource = compilationunit.getTypes().get(0).toString();
        return new ArrayList<String>(Arrays.asList(classSource.split(" ")));
    }

    @NotNull
    public ArrayList<String> getClassSourceList(CompilationUnit compilationunit) throws ParseException, FileNotFoundException {
        String classSource = compilationunit.getTypes().get(0).toString();
        return new ArrayList<String>(Arrays.asList(classSource.split(" ")));
    }
}
