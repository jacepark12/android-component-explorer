package com.android.component.explorer.scanner;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class FilterTest {
    @Test
    public void interested() throws Exception {
        DirExplorer.Filter filter = new Filter();

        assertEquals(filter.interested(0, "/test/dir/test.java", new File("/test/dir/test.java")), true);
        assertEquals(filter.interested(0, "/test/dir/test.cpp", new File("/test/dir/test.cpp")), false);
    }

}