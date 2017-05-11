package com.android.component.explorer.scanner;

import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 16..
 */
public class FileHandlerTest {

    FileHandler fileHandler = FileHandler.getInstance();
    StringBuilder rootPath = new StringBuilder(System.getProperty("user.dir"));

    @Test
    // Testing initializing code in FileHandler constructor
    public void initializeTest(){


        Set<String> activityExpected = new HashSet<String>();
        activityExpected.add("Activity");
        activityExpected.add("AppCompatActivity");

        assertEquals(activityExpected, fileHandler.getActivityClassNames());

        Set<String> fragmentExpected = new HashSet<String>();
        fragmentExpected.add("Fragment");
        fragmentExpected.add("DialogFragment");

        assertEquals(fragmentExpected, fileHandler.getFragmentClassNames());
    }

    @Test
    public void getFileName() throws Exception {
        String samplePath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/FileHandlerTest.java").toString();

        File file = new File(samplePath);
        String result = fileHandler.getFileName(file);

        assertEquals("FileHandlerTest", result);
    }

}