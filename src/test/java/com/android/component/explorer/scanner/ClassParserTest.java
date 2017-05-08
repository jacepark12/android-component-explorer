package com.android.component.explorer.scanner;

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
        String sampleMethodString = "setContentView(R.layout.activity_navigation_drawer);";
        String expected = "activity_navigation_drawer";

        assertEquals(expected, classParser.getLayoutXMLNameFromMethod(sampleMethodString));

    }

}