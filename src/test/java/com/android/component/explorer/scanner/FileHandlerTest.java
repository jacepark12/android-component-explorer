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

        Set<String> activityExpected = new HashSet<String>();
        activityExpected.add("Activity");
        activityExpected.add("AppCompatActivity");

        assertEquals(activityExpected, fileHandler.getActivityClassNames());

        Set<String> fragmentExpected = new HashSet<String>();
        fragmentExpected.add("Fragment");
        fragmentExpected.add("DialogFragment");

        assertEquals(fragmentExpected, fileHandler.getFragmentClassNames());
    }


}