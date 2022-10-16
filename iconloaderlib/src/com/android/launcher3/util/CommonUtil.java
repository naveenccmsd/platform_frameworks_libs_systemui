package com.android.launcher3.util;

import static android.content.Intent.ACTION_PACKAGE_ADDED;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;

import com.android.launcher3.icons.IconProvider;
import com.android.launcher3.icons.R;
import com.android.launcher3.icons.ThemedIconDrawable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class CommonUtil {

    public static void updateMap(Context context, Map<ComponentName, ThemedIconDrawable.ThemeData> map) {
        List<ComponentName> mCalendars = parseComponentsOrNull(context, R.array.calendar_components_name);
        final int id = getDynamicIconId(context);
        for (ComponentName mCalendar : mCalendars) {
           if( map.get(new ComponentName(mCalendar.getPackageName(), "") )==null){
                map.put(new ComponentName(mCalendar.getPackageName(), ""), new ThemedIconDrawable.ThemeData(context.getResources(), mCalendar.getPackageName(), id));
            }
        }
        List<ComponentName> mClocks = parseComponentsOrNull(context, R.array.clock_components_name);
        final int cid =  context.getResources().getIdentifier("themed_icon_clock" , "drawable", context.getPackageName());
        for (ComponentName mClock : mClocks) {
            if( map.get(new ComponentName(mClock.getPackageName(), "") )==null){
                map.put(new ComponentName(mClock.getPackageName(), ""), new ThemedIconDrawable.ThemeData(context.getResources(), mClock.getPackageName(), cid));
            }
        }
    }
    public static int getDynamicIconId(Context mContext) {
        return mContext.getResources().getIdentifier("themed_icon_calendar_" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH), "drawable", mContext.getPackageName());
    }


    public static List<ComponentName> parseComponentsOrNull(Context context, int resId) {
        String[] cn = context.getResources().getStringArray(resId);
        List<ComponentName> comps = new ArrayList<>();
        for (String c : cn) {
            comps.add(new ComponentName(c, ""));
        }
        return comps.size() == 0 ? null : comps;
    }
   /* public static ArrayMap<String, ThemedIconDrawable.ThemeData> updateMapStr(Context context, ArrayMap<String, ThemedIconDrawable.ThemeData> map) {
        List<ComponentName> mCalendars = parseComponentsOrNull(context, R.array.calendar_components_name);
        Log.d("Utils", "Befroe" + map.size());
        final int id = getDynamicIconId(context);
//        val iconId = parser.getAttributeResourceValue(null, ATTR_DRAWABLE, 0)
        for (ComponentName mCalendar : mCalendars) {
            Log.d("Utils", mCalendar.getPackageName() + "==?" + context.getPackageName() + "," + id);
            map.put(mCalendar.getPackageName(), new ThemedIconDrawable.ThemeData(context.getResources(), context.getPackageName(), id));
        }
        return map;
    }*/
/*   public static int getDynamicIconId(Context mContext,String customPackage) {
       return mContext.getResources().getIdentifier("themed_icon_calendar_" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH), "drawable", mContext.getPackageName());
   }*/



    public static ThemedIconDrawable.ThemeData getThemeMap(Context context,ComponentName componentName, Map<ComponentName, ThemedIconDrawable.ThemeData> themeMap) {
//        updateMap(context,themeMap);
        List<ComponentName> mCalendars = parseComponentsOrNull(context, R.array.calendar_components_name);
        if(mCalendars.stream().anyMatch(s->s.getPackageName().equalsIgnoreCase(componentName.getPackageName()))){
            final int id = getDynamicIconId(context);
            return new ThemedIconDrawable.ThemeData(context.getResources(), componentName.getPackageName(), id);
        }
        List<ComponentName> mClocks = parseComponentsOrNull(context, R.array.clock_components_name);
        if(mClocks.stream().anyMatch(s->s.getPackageName().equalsIgnoreCase(componentName.getPackageName()))){

            final int cid =  context.getResources().getIdentifier("themed_icon_clock_adaptive" , "drawable", context.getPackageName());
            Log.d("Naveen","clockIcons");
            return new ThemedIconDrawable.ThemeData(context.getResources(), componentName.getPackageName(), cid);
        }
        ThemedIconDrawable.ThemeData res = null;
        if (themeMap.get(componentName) != null) {
            res =  themeMap.get(componentName);
        } else if(themeMap.get(new ComponentName(componentName.getPackageName(), "")) !=null){
            res = themeMap.get(new ComponentName(componentName.getPackageName(), ""));
        }
        return res;
    }

    public static void updateIconState(Context context,IconProvider.IconChangeListener mCallback, UserHandle myUserHandle) {
        new IntentFilter(ACTION_PACKAGE_ADDED).addAction(context.getPackageName());
        List<ComponentName> mCalendars = parseComponentsOrNull(context, R.array.calendar_components_name);
        for (ComponentName mCalendar : mCalendars) {
            mCallback.onAppIconChanged(mCalendar.getPackageName(), myUserHandle);
            mCallback.onAppIconChanged(context.getPackageName(), myUserHandle);

        }
        List<ComponentName> mClocks = parseComponentsOrNull(context, R.array.clock_components_name);
        for (ComponentName mClock : mClocks) {
            mCallback.onAppIconChanged(mClock.getPackageName(), myUserHandle);
            mCallback.onAppIconChanged(context.getPackageName(), myUserHandle);

        }

    }
}
