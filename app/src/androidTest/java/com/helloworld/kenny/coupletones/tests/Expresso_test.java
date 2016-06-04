package com.helloworld.kenny.coupletones.tests;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.helloworld.kenny.coupletones.MapsActivity;
import com.helloworld.kenny.coupletones.R;
import com.helloworld.kenny.coupletones.favorites.Favorites;
import com.helloworld.kenny.coupletones.favorites.PartnerFavoriteEntry;
import com.helloworld.kenny.coupletones.favorites.exceptions.InvalidNameException;
import com.helloworld.kenny.coupletones.favorites.exceptions.NameInUseException;
import com.helloworld.kenny.coupletones.firebase.exceptions.PartnerAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.exceptions.UserAlreadyRegisteredException;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseFavoriteManager;
import com.helloworld.kenny.coupletones.firebase.managers.FirebaseRegistrationManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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

    //Test 1.1: User will see own favorites
    @Test
    public void testFavorites() {
        Favorites fav = new Favorites();
        String s = "Fake Place";
        try {
            fav.addEntry(s, new LatLng(5, 5));
        } catch(NameInUseException e) {
        } catch(InvalidNameException e) {
        }
        onView(withId(R.id.main)).perform(swipeLeft());
        onView(withId(R.id.left_listFavorite)).check(matches(isDisplayed()));
        onView(withId(R.id.listview_item_text)).check(matches(withText(s)));
    }

    //Test 1.1.5: Assuming that User is paired, they will see their partner's favorites
    @Test
    public void testPartnerFavPaired() {
        onView(withId(R.id.main)).perform(swipeRight());

        onView(withId(R.id.right_drawer)).check(matches(isDisplayed()));
        onView(withId(R.id.listview_favorite_name)).check(matches(withText("")));


    }

    //Test 1.2: Assuming that User is paired, they will see anything when swiped left on
    //partner's favorites
    @Test
    public void testPartnerFavUnpaired() {
        onView(withId(R.id.main)).perform(swipeLeft());

        onView(withId(R.id.right_drawer)).check(matches(isDisplayed()));

        onView(withId(R.id.listview_favorite_name)).check(matches(withText("")));
    }

    //Test 2: Settings for Tones and Notification
    @Test
    public void testSettings() {
        onData(withId(R.id.drawer_layout)).perform(swipeLeft());

        onView(withId(R.id.right_drawer)).check(matches(isDisplayed()));
        onView(withId(R.id.settings)).perform(click());

        onView(withId(R.id.tone)).check(matches(isDisplayed()));
        onView(withId(R.id.vibration)).check(matches(isDisplayed()));

        onView(withId(R.id.tone)).check(matches(isChecked()));
        onView(withId(R.id.vibration)).check(matches(isChecked()));
    }

    //Test 3: History displays
    @Test
    public void testHistory() {
        onView(withId(R.id.main)).perform(swipeLeft());
        onView(withId(R.id.left_listFavorite)).check(matches(isDisplayed()));

        onView(withId(R.id.get_history)).perform(click());
        onView(withId(R.id.left_listHistory)).check(matches(isDisplayed()));
        onView(withId(R.id.get_favorites)).check(matches(isDisplayed()));

        onView(withId(R.id.get_favorites)).perform(click());
        onView(withId(R.id.left_listFavorite)).check(matches(isDisplayed()));
    }

    //Test 4: Tone selections
    @Test
    public void testSelection() {
        onView(withId(R.id.main)).perform(swipeLeft());
        onView(withId(R.id.left_listFavorite)).check(matches(isDisplayed()));
        onView(withId(R.id.selection)).check(matches(isDisplayed()));

        onView(withId(R.id.selection)).perform(click());
        onView(withId(R.id.arrival_tone)).check(matches(isDisplayed()));

        onView(withId(R.id.arrival_tone)).perform(click());
        onView(withText("Pick an Arrival Vibration")).check(matches(isDisplayed()));
        onView(withText("CANCEL")).perform(click());

    }

}
