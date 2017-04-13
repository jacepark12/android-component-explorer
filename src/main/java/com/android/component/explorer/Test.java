package com.android.component.explorer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class Test {

    static ArrayList<String> tests = new ArrayList<String>();
    public static void main(String[] args) throws FileNotFoundException, ParseException {

        File file = new File("/Users/parkjaesung/Documents/workspace/Github/android-component/src/main/java/HelloAction.java");

        System.out.println(file.getName());

        System.out.println(Files.getFileExtension(file.getName()));

        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("/Users/parkjaesung/Documents/workspace/Github/android-component/src/main/java/HelloAction.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);

        // prints the resulting compilation unit to default system output
        System.out.println(cu.toString());
        System.out.println(cu.getClass().toString());

        System.out.printf("getTypes==================");
        System.out.println(cu.getTypes().get(0).toString().split(" "));

        String[] tmp = cu.getTypes().get(0).toString().split(" ");

        for (String t: tmp
             ) {
            System.out.println(t);
        }

        System.out.println("gettyupes getname=======");
        System.out.println(cu.getTypes());

        System.out.println(Test.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println(System.getProperty("user.dir"));
    }
}
