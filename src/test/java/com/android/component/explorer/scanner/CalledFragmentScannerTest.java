package com.android.component.explorer.scanner;

import com.android.component.explorer.manager.UnitManager;
import com.android.component.explorer.unit.FragmentUnit;
import com.android.component.explorer.util.CompilationUnitUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by parkjaesung on 2017. 4. 13..
 */
public class CalledFragmentScannerTest {

    CalledFragmentScanner calledFragmentScanner = CalledFragmentScanner.getInstance();
    CompilationUnitUtil compilationUnitUtil = new CompilationUnitUtil();
    StringBuilder rootPath = new StringBuilder(System.getProperty("user.dir"));

    @Before
    public void setUp() throws Exception {
        UnitManager unitManager = UnitManager.getInstance();

        unitManager.addFragment(new FragmentUnit("AddressBookFragment"));
        unitManager.addFragment(new FragmentUnit("SendCheckFragment"));
        unitManager.addFragment(new FragmentUnit("SettingFragment"));
        unitManager.addFragment(new FragmentUnit("SelectTemplateFragment"));
        unitManager.addFragment(new FragmentUnit("CreateCardFragment"));
    }

    @Test
    public void getFragmentInstanceNames() throws Exception {
        String samplePath = rootPath.append("/src/test/java/com/android/component/explorer/scanner/sample/FragmentParseSample.txt").toString();
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "addressBookFragment" ,"mFrontFragment", "mSendCheckFragment","mSettingFragment", "mSelectTemplateFragment"
        ));
        ArrayList<String> classSources = compilationUnitUtil.getClassSourceList(new File(samplePath));
        assertEquals(expected, calledFragmentScanner.getFragmentInstanceNames(classSources));
    }

}