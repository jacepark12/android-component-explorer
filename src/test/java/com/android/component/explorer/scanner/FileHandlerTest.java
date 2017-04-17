package com.android.component.explorer.scanner;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 16..
 */
public class FileHandlerTest {

    @Test
    // Testing initializing code in FileHandler constructor
    public void initializeTest(){
        FileHandler fileHandler = FileHandler.getInstance();

        Set<String> expected = new HashSet<String>();
        expected.add("Activity");
        expected.add("AppCompatActivity");

        assertEquals(expected, fileHandler.getActivityClassNames());
    }


}