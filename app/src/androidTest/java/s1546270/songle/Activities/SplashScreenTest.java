package s1546270.songle.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashScreenTest {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

    @Test
    public void splashScreenTest() {

        Activity activity = mActivityTestRule.getActivity();
        SharedPreferences pref = activity.getSharedPreferences("SonglePref", 0);
        int score = pref.getInt("score",-1);

        assert(score != -1);

    }

}
