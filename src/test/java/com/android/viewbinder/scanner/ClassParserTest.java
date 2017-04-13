package com.android.viewbinder.scanner;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class ClassParserTest {

    ClassParser classParser = ClassParser.getInstance();
    StringBuilder rootPath = new StringBuilder(System.getProperty("user.dir"));
    @Test
    public void getClassName() throws Exception {
        String sampleClassPath = rootPath.append("/src/test/java/com/android/viewbinder/scanner/sample/SampleClass.java").toString();
        assertEquals(classParser.getClassName(new File(sampleClassPath)), "SampleClass");
    }

    @Test
    public void getClassName1() throws Exception {

    }

    @Test
    public void getParentClassName() throws Exception {
        String sampleClassPath = rootPath.append("/src/test/java/com/android/viewbinder/scanner/sample/SampleClass.java").toString();
        assertEquals(classParser.getParentClassName(new File(sampleClassPath)), "Activity");
    }

    @Test
    public void getParentClassName1() throws Exception {

    }

    @Test
    public void getInterfaceName() throws Exception {

    }

    @Test
    public void getInterfaceName1() throws Exception {

    }

}