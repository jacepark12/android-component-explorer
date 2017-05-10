package com.android.component.explorer.scanner;

import com.android.component.explorer.scanner.parser.ClassParser;
import com.github.javaparser.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class ClassParserTest {

    ClassParser classParser = ClassParser.getInstance();
    StringBuilder rootPath = new StringBuilder(System.getProperty("user.dir"));
    @Test
    public void getClassName() throws Exception {
        String sampleClassPath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/sample/ActivitySample.java").toString();
        assertEquals(classParser.getClassName(new File(sampleClassPath)), "ActivitySample");
    }

    @Test
    public void getParentClassName() throws Exception {
        String sampleClassPath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/sample/ActivitySample.java").toString();
        assertEquals(classParser.getParentClassName(new File(sampleClassPath)), "Activity");
    }

    @Test
    public void getLayoutXMLNameFromMethod() throws Exception{
        String sampleMethodString1 = "setContentView(R.layout.activity_navigation_drawer);";
        String expected1 = "activity_navigation_drawer";

        String sampleMethodString2 = "View view = inflater.inflate(R.layout.fragment_track,container,false);";
        String expected2 = "fragment_track";

        assertEquals(expected1, classParser.getLayoutXMLNameFromMethod(sampleMethodString1));
        assertEquals(expected2, classParser.getLayoutXMLNameFromMethod(sampleMethodString2));

    }

    @Test
    public void getClassNameFromPackage() throws Exception {
        assertEquals("AppCompatActivity" , classParser.getClassNameFromPackage("AppCompatActivity"));
        assertEquals("DialogFragment" , classParser.getClassNameFromPackage("android.support.v4.app.DialogFragment"));
    }

    @Test
    public void isAndroidPackage() throws Exception {
        assertEquals(true, classParser.isAndroidPackage("Activity"));
        assertEquals(false, classParser.isAndroidPackage("this.is.test.case.android"));
        assertEquals(true, classParser.isAndroidPackage("android.test.case"));
    }
    @Test
    public void getFullClassName(){
        String expected = "com.android.component.explorer.scanner.sample.ActivitySample";
        String sampleClassPath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/sample/ActivitySample.java").toString();

        assertEquals(expected, classParser.getFullClassName(new File(sampleClassPath)));
    }

    @Test
    public void getParentClassFullPackage() throws FileNotFoundException, ParseException {
        String expected = "com.android.component.explorer.scanner.sample2.Activity2";
        String sampleClassPath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/sample/ActivitySample2.java").toString();

        assertEquals(expected, classParser.getParentClassFullPackage(new File(sampleClassPath)));
    }

    @Test
    public void hasParentClass(){
        boolean expected1 = true;
        String sampleClassPath1 = System.getProperty("user.dir") + "/src/test/java/com/android/component/explorer/scanner/sample/ActivitySample.java";

        boolean expected2 = false;
        String sampleClassPath2 = System.getProperty("user.dir") + "/src/test/java/com/android/component/explorer/scanner/sample/ActivitySample2.java";

        assertEquals(expected1, classParser.hasParentClass(new File(sampleClassPath1)));
        assertEquals(expected2, classParser.hasParentClass(new File(sampleClassPath2)));
    }
}