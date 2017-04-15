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

        assertEquals(true, filter.interested(0, "/test/dir/test.java", new File("/test/dir/test.java")));
        assertEquals(false, filter.interested(0, "/test/dir/test.cpp", new File("/test/dir/test.cpp")));
        assertEquals(false, filter.interested(0, "/test/dir/test.txt", new File("/test/dir/text.txt")));
    }

}