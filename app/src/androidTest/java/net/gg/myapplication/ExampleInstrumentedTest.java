package net.gg.myapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;


import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import net.gg.myapplication.Activity.AddTask;
import net.gg.myapplication.Activity.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // this class help to click item in recyclerView
    public static class MyViewAction {
        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }


    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void AddTask() {

        /// test add task and delete it because if i duplicated the String in Activity
        // , the check method will be return an error ;
        // i fix the problem by use random string;
        String randomString = UUID.randomUUID().toString();
        Espresso.onView(withId(R.id.addTaskBtn)).perform(click());
        onView(withId(R.id.Edit_text_task_name)).perform(typeText(randomString));
        onView(withId(R.id.Edit_text_task_description)).perform(typeText("task_description"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_submit_add_task)).perform(click());


        RecyclerView recyclerView = mainActivity.getActivity().findViewById(R.id.my_recycler_view);
        int itemCount = recyclerView.getAdapter().getItemCount();
        // TODO: 5/10/2022 test for delete button
//        onView(withId(R.id.my_recycler_view))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(itemCount-1, MyViewAction.clickChildViewWithId(R.id.icon_delete_task)));
        onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.scrollToPosition(itemCount - 1));
        Espresso.onView(withText(randomString)).check(matches(isDisplayed()));

    }


    @Test
    public void TestDetailPage() {
        String randomString = UUID.randomUUID().toString();
        Espresso.onView(withId(R.id.addTaskBtn)).perform(click());
        onView(withId(R.id.Edit_text_task_name)).perform(typeText(randomString));
        onView(withId(R.id.Edit_text_task_description)).perform(typeText("task_description"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_submit_add_task)).perform(click());
        RecyclerView recyclerView = mainActivity.getActivity().findViewById(R.id.my_recycler_view);
        int itemCount = recyclerView.getAdapter().getItemCount();
        onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(itemCount - 1, click()));
        onView(withText(randomString)).check(matches(isDisplayed()));
    }

    @Test
    public void testSettingPage() {
        openActionBarOverflowOrOptionsMenu(mainActivity.getActivity());
        onView(withText("Setting"))
                .perform(click());
        onView(withId(R.id.edit_text_enter_your_name)).perform(typeText("mohammed"));
        onView(withId(R.id.submit_btn_your_name)).perform(click());

    }
}