package com.rappidbizapps.testingfun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by santosh on 6/9/16.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    ActivityController mActivityController;
    MainActivity mainActivity;

    @Before
    public void setup() {
//        mainActivity = Robolectric.setupActivity(MainActivity.class);
        mActivityController = Robolectric.buildActivity(MainActivity.class);
    }

    @Test
    public void textViewExists() {
        TextView textView = (TextView) mainActivity.findViewById(R.id.sample_text);
        assertNotNull("TextView could not be found", textView);
        assertTrue("TextView contains incorrect text", "Hello World!".equals(textView.getText().toString()));
    }

    void createActivitywithIntent(String extra) {
        Intent intent = new Intent(RuntimeEnvironment.application,MainActivity.class);
        intent.putExtra(Constants.ACTIVITY_EXTRA,extra);
        // TODO: 12/9/16 not fully  understood..explore more
        mainActivity = (MainActivity) mActivityController.withIntent(intent).create().start().resume().visible().get();
    }

    @Test
    public void secondActivityStartedOnClick() {
        mainActivity.findViewById(R.id.sampel_button).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);

        Intent expectedIntent = new Intent(mainActivity, SecondActivity.class);

        //Roboelectric keeps track of all launched activities
        Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertTrue(actualIntent.filterEquals(expectedIntent));

    }

    @Test
    public void createAndDestroyActivity() {
        createActivitywithIntent("MY_EXTRA");
    }

    @After
    public void tearDown() {
        mActivityController.pause().stop().destroy();
    }

}
