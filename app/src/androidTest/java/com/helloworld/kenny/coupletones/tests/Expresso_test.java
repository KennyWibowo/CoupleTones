package com.helloworld.kenny.coupletones.tests;

import android.content.Context;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.FavoriteSwipeAdapter;
import com.helloworld.kenny.coupletones.MapsActivity;
import com.helloworld.kenny.coupletones.R;
import com.helloworld.kenny.coupletones.favorites.FavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseFavoriteManager;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseRegistrationManager;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Karen-PC on 6/3/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Expresso_test {
    @Rule
    public ActivityTestRule<MapsActivity> mapsActivityRule = new ActivityTestRule<MapsActivity>(MapsActivity.class);

    private static ViewAction actionOpenRightDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.END);
            }
        };
    }

    private static ViewAction actionOpenLeftDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.START);
            }
        };
    }

    private static ViewAction actionCloseDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "close drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(GravityCompat.START);
            }
        };
    }

    //Test 1.1: User will see own favorites
    @Test
    public void testFavorites() {
        onView(withId(R.id.email)).perform(typeText("bobby@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionOpenRightDrawer());
        onView(withId(R.id.right_drawer_view)).check(matches(isDisplayed()));
    }

    //Test 1.2: Register Partner
    @Test
    public void testPartnerRegister() {
        onView(withId(R.id.email)).perform(typeText("bobby@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionOpenLeftDrawer());

        onView(withId(R.id.add_partner)).perform(click());
        onView(withId(R.id.partner_email)).check(matches(isDisplayed()));
        onView(withId(R.id.partner_email)).perform(typeText("joe@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.remove_partner)).check(matches(isDisplayed()));

    }

    //Test 1.3: Unregister a partner
    @Test
    public void testPartnerUnregister() {
        onView(withId(R.id.email)).perform(typeText("bobby@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionOpenLeftDrawer());

        onView(withId(R.id.add_partner)).perform(click());
        onView(withId(R.id.partner_email)).check(matches(isDisplayed()));
        onView(withId(R.id.partner_email)).perform(typeText("joe@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.remove_partner)).check(matches(isDisplayed()));
        onView(withId(R.id.remove_partner)).perform(click());
        onView(withText("Yes!")).perform(click());

        onView(withId(R.id.add_partner)).check(matches(isDisplayed()));
    }

    //Test 2: Settings for Tones and Notification
    @Test
    public void testSettings() {
        onView(withId(R.id.email)).perform(typeText("bobby@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionOpenRightDrawer());

        onView(withId(R.id.right_drawer_view)).check(matches(isDisplayed()));
        onView(withId(R.id.settings)).perform(click());

        onView(withId(R.id.tone)).check(matches(isDisplayed()));
        onView(withId(R.id.vibration)).check(matches(isDisplayed()));

        onView(withId(R.id.tone)).check(matches(isChecked()));
        onView(withId(R.id.vibration)).check(matches(isChecked()));
    }

    //Test 3: Toggle History and Favorite displays
    @Test
    public void testHistory() {
        onView(withId(R.id.email)).perform(typeText("bobby@gmail.com"), closeSoftKeyboard());
        onView(withText("Submit")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(actionOpenLeftDrawer());
        onView(withId(R.id.left_listFavorite)).check(matches(isDisplayed()));

        onView(withId(R.id.get_history)).perform(click());
        onView(withId(R.id.left_listHistory)).check(matches(isDisplayed()));
        onView(withId(R.id.get_favorites)).check(matches(isDisplayed()));

        onView(withId(R.id.get_favorites)).perform(click());
        onView(withId(R.id.left_listFavorite)).check(matches(isDisplayed()));
    }


}
