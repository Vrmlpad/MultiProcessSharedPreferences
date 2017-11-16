package lib.multiprocess.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by DCloud on 2016/6/17.<br/>
 * SharedPreferences的多进程实现<br/>
 * 通过{@link SPHelper}中转到{@link lib.multiprocess.sharedpreferences.HostContentProvider}中调用{@link SPHelperImpl}实现<br/>
 * {@link SPHelper}封装SharedPreferences、SharedPreferences.Editor的方法并调用{@link lib.multiprocess.sharedpreferences.HostContentProvider}
 */
public class SPMultiProcessImpl implements SharedPreferences{

    public SPHelper mSPHelperExt;

    public SPMultiProcessImpl(Context context, String name){
        this(context,name,Context.MODE_PRIVATE);
    }

    public SPMultiProcessImpl(Context context, String name, int mode){
        mSPHelperExt = new SPHelper(context, name,mode);
    }

    @Override
    public Map<String, ?> getAll() {
        return mSPHelperExt.getAll();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        try {
            return  mSPHelperExt.getBoolean(key,defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Override
    public String getString(String key, String defValue) {
        try {
            return  mSPHelperExt.getString(key,defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        try {
            return  mSPHelperExt.getSetString(key,defValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValues;
    }

    @Override
    public int getInt(String key, int defValue) {
        try {
            return  mSPHelperExt.getInt(key,defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        try {
            return  mSPHelperExt.getFloat(key,defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        try {
            return  mSPHelperExt.getLong(key,defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public boolean contains(String key){
        return getAll().containsKey(key);
    }

    @Override
    public Editor edit() {
        return new EditorMultiProcessImpl(this);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    public static class EditorMultiProcessImpl implements Editor{
        SPMultiProcessImpl mSP = null;
        private Map<String,Object> mCache = new HashMap<String,Object>();
        private EditorMultiProcessImpl(SPMultiProcessImpl sp){
            mSP = sp;
            mCache = (Map<String, Object>) mSP.getAll();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            mCache.put(key,value);
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            mCache.put(key,value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            mCache.put(key,values);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            mCache.put(key,value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            mCache.put(key,value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            mCache.put(key,value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            mCache.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            mSP.mSPHelperExt.clear();
            mCache.clear();
            return this;
        }

        @Override
        public boolean commit() {
            mSP.mSPHelperExt.commit(mCache);
            mCache.clear();
            return false;
        }

        @Override
        public void apply() {
            commit();
        }
    }
}