package com.helloworld.kenny.coupletones.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.helloworld.kenny.coupletones.MapsActivity;

/**
 * Created by Karen on 5/8/2016.
 */
public class JUnit_test extends ActivityInstrumentationTestCase2<MapsActivity> {

    MapsActivity maps;

    public JUnit_test() {
        super(MapsActivity.class);
    }

    public void test() {
        assertEquals(2+2, 4);
    }


}
