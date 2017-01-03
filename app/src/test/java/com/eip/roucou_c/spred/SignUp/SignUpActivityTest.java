package com.eip.roucou_c.spred.SignUp;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.eip.roucou_c.spred.R;
import com.eip.roucou_c.spred.RandomString;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by roucou_c on 29/12/2016.
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

//    @Rule
//    public ActivityTestRule<SignUpActivity> mActivityRule = new ActivityTestRule<>(SignUpActivity.class);

    private String password;
    private String email;
    private String prenom;
    private String nom;

    @Before
    public void setUp() throws Exception {
        RandomString randomString = new RandomString(10);
        prenom = randomString.nextString() + "test";
        nom = randomString.nextString() + "test";
        email = randomString.nextString() + "@test.fr";
        password = "1234";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void signUp() {

        onView(withId(R.id.signup_step1_email)).perform(typeText(email));
        onView(withId(R.id.signup_step1_nom)).perform(typeText(nom));
        onView(withId(R.id.signup_step1_prenom)).perform(typeText(prenom));
        onView(withId(R.id.signup_step1_password)).perform(typeText(password));
        SystemClock.sleep(1000);
        onView(withId(R.id.signup_step1_submit)).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

}