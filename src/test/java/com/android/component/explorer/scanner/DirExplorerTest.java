package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.UnitManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class DirExplorerTest {

    DirExplorer.FileHandler fileHandler;
    DirExplorer.Filter filter;

    UnitManager unitManager;
    StringBuilder rootPath = new StringBuilder(System.getProperty("user.dir"));

    @Before
    public void setUp(){
        fileHandler = FileHandler.getInstance();
        filter = Filter.getInstance();
        unitManager = UnitManager.getInstance();
    }
    @Test
    public void explore() throws Exception {
        DirExplorer dirExplorer = new DirExplorer(this.filter, this.fileHandler);

        String samplePath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/sample").toString();
        dirExplorer.explore(new File(samplePath));

        ArrayList<String> expectedActivityNames = new ArrayList<String>(Arrays.asList(
                "ActivitySample"
        ));
        assertEquals(expectedActivityNames, new ArrayList<String>(unitManager.getActivities().keySet()));
    }

}