package lib.multiprocess.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class SPHelperImpl {

    private static Map<String, SoftReference<SharedPreferences>> sSharedPreferencesCacheMap;

    /**
     * 获取SharedPreferences，没有时创建新的
     * @param context
     * @param name
     * @return
     */
    private static SharedPreferences getSharedPreferences(Context context, String name,String modeStr) {
        if (context == null) {
            return null;
        }
        if(null ==sSharedPreferencesCacheMap){
            sSharedPreferencesCacheMap =new HashMap<String, SoftReference<SharedPreferences>>();
        }

        if(sSharedPreferencesCacheMap.containsKey(name) && null!=sSharedPreferencesCacheMap.get(name) &&
                null!=sSharedPreferencesCacheMap.get(name).get()){
            return sSharedPreferencesCacheMap.get(name).get();
        }
        int mode = Context.MODE_PRIVATE;
        try {
            mode = Integer.valueOf(modeStr);
        }catch(Exception e){e.printStackTrace();}
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,mode);
        sSharedPreferencesCacheMap.put(name,new SoftReference<SharedPreferences>(sharedPreferences));
        return sharedPreferences;
    }

    synchronized static <T> void saveValue(Context context, String spName,String mode, String type, String key, T t) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit();
        if (Constants.TYPE_BOOLEAN.equalsIgnoreCase(type)) {
            editor.putBoolean(key, (Boolean) t);
        }
        else if (Constants.TYPE_STRING.equalsIgnoreCase(type)) {
            editor.putString(key, (String)t);
        }
        else if (Constants.TYPE_STRING_SET.equalsIgnoreCase(type)) {
            String valueStr = (String) t;
            if (valueStr.matches("\\[.*\\]")){
                String sub=valueStr.substring(1,valueStr.length()-1);
                String[] spl=sub.split(", ");
                HashSet set=new HashSet<>();
                for (String v:spl){
                    set.add(v.replace(SPHelper.COMMA_REPLACEMENT,", "));
                }
                editor.putStringSet(key, set);
            }
        }
        else if (Constants.TYPE_INT.equalsIgnoreCase(type)) {
            editor.putInt(key, (Integer) t);
        }
        else if (Constants.TYPE_FLOAT.equalsIgnoreCase(type)) {
            editor.putFloat(key, (Float) t);
        }
        else if (Constants.TYPE_LONG.equalsIgnoreCase(type)) {
            editor.putLong(key, (Long) t);
        }
        editor.commit();
    }

    static String get(Context context, String spName,String mode, String key, String type) {
        Object value = get_impl(context,spName,mode, key, type);
        return value + "";
    }

    private static Object get_impl(Context context, String spName,String mode, String key, String type) {
        if (!contains(context,spName,mode, key)) {
            return null;
        } else {
            if (type.equalsIgnoreCase(Constants.TYPE_BOOLEAN)) {
                return getBoolean(context,spName,mode, key, false);
            } else if (type.equalsIgnoreCase(Constants.TYPE_STRING)) {
                return getString(context,spName,mode, key, null);
            } else if (type.equalsIgnoreCase(Constants.TYPE_STRING_SET)) {
                return getSetString(context,spName,mode, key);
            } else if (type.equalsIgnoreCase(Constants.TYPE_INT)) {
                return getInt(context,spName,mode, key, 0);
            } else if (type.equalsIgnoreCase(Constants.TYPE_FLOAT)) {
                return getFloat(context,spName,mode, key, 0f);
            } else if (type.equalsIgnoreCase(Constants.TYPE_LONG)) {
                return getLong(context,spName,mode, key, 0L);
            }
            return null;
        }
    }


    static boolean getBoolean(Context context, String spName,String mode, String key, boolean defaultValue) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return defaultValue;
        return sp.getBoolean(key, defaultValue);
    }

    static String getString(Context context, String spName,String mode,String key, String defaultValue) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return defaultValue;
        return sp.getString(key, defaultValue);
    }

    static String getSetString(Context context, String spName,String mode, String key) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return null;
        Set<String> value =sp.getStringSet(key, null);
        if(null==value){
            return null;
        }
        return value.toString();
    }

    static int getInt(Context context, String spName,String mode, String key, int defaultValue) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return defaultValue;
        return sp.getInt(key, defaultValue);
    }

    static float getFloat(Context context, String spName,String mode, String key, float defaultValue) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return defaultValue;
        return sp.getFloat(key, defaultValue);
    }

    static long getLong(Context context, String spName,String mode, String key, long defaultValue) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return defaultValue;
        return sp.getLong(key, defaultValue);
    }

    static boolean contains(Context context, String spName,String mode, String key) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return false;
        return sp.contains(key);
    }

    static void remove(Context context, String spName,String mode, String key) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    static void clear(Context context, String spName,String mode) {
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    static Map<String,?> getAll(Context context, String spName,String mode){
        SharedPreferences sp = getSharedPreferences(context,spName,mode);
        return sp.getAll();
    }

    static void commit(Context context, String spName,String mode,Map<String,Object> map){
        if(null!=map){
            SharedPreferences sp = getSharedPreferences(context,spName,mode);
            Set<String> sets =  map.keySet();
            SharedPreferences.Editor editor=sp.edit();
            if(map.isEmpty()){
                editor.clear();
            }else{
                editor.clear();
                if(Build.VERSION.SDK_INT>=9){
                    editor.apply();
                }else{
                    editor.commit();
                }
                Object value;
                Set<String> set;
                for(String key:sets){
                    value = map.get(key);
                    if (value instanceof String){
                        String valueStr = (String) value;
                        if (valueStr.matches("\\[.*\\]")){
                            String sub=valueStr.substring(1,valueStr.length()-1);
                            String[] spl=sub.split(", ");
                            set=new HashSet<>();
                            for (String t:spl){
                                set.add(t.replace(SPHelper.COMMA_REPLACEMENT,", "));
                            }
                            editor.putStringSet(key, set);
                        }else{
                            editor.putString(key, (String) value);
                        }
                    }
                    else if (value instanceof Integer){
                        editor.putInt(key, (Integer) value);
                    }
                    else if (value instanceof Float){
                        editor.putFloat(key, (Float) value);
                    }
                    else if (value instanceof Long){
                        editor.putLong(key, (Long) value);
                    }
                    else if (value instanceof Boolean){
                        editor.putBoolean(key, (Boolean) value);
                    }
                }
            }
            if(Build.VERSION.SDK_INT>=9){
                editor.apply();
            }else{
                editor.commit();
            }
        }
    }
}