package br.com.devgeek.cartolaparciais.helper;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by geovannefduarte on 01/09/17.
 */
public class BottomNavigationViewHelper {

    public static void disableShiftMode(BottomNavigationView view){

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);

        try {

            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++){

                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                //noinspection RestrictedApi
                item.setShiftingMode(false);

                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e){
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e){
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
} // https://stackoverflow.com/a/40189977
// https://blog.xamarin.com/exploring-androids-bottom-navigation-view/
// https://android.jlelse.eu/ultimate-guide-to-bottom-navigation-on-android-75e4efb8105f
// https://blog.autsoft.hu/now-you-can-use-the-bottom-navigation-view-in-the-design-support-library/
// https://medium.com/@hitherejoe/exploring-the-android-design-support-library-bottom-navigation-drawer-548de699e8e0

// https://www.youtube.com/watch?v=UqtsyhASW74