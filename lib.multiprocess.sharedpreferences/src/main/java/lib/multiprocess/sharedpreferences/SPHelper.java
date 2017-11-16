package lib.multiprocess.sharedpreferences;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SPHelper {

    public static final String COMMA_REPLACEMENT="__COMMA__";
    public static Context mContext;
    /** SharedPreferences.name */
    private String mSpName;
    private int mMode;
    private String mRootUri = null;

    public SPHelper(Context context, String name,int mode){
        mContext = context;
        mSpName = name;
        mMode = mode;
    }

    /**
     * 提交Boolean数据
     * @param name
     * @param t
     */
    public synchronized void putBoolean(String name, Boolean t) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_BOOLEAN,name);
        ContentValues cv = new ContentValues();
        cv.put(Constants.VALUE, t);
        cr.update(uri, cv, null, null);
    }

    /**
     * 提交String数据
     * @param name
     * @param t
     */
    public synchronized void putString(String name, String t) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_STRING,name);
        ContentValues cv = new ContentValues();
        cv.put(Constants.VALUE, t);
        cr.update(uri, cv, null, null);
    }

    /**
     * 提交Set<String>数据
     * @param name
     * @param t
     */
    public synchronized void putSetString(String name, Set<String> t) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_STRING_SET,name);
        ContentValues cv = new ContentValues();
        Set<String> convert=new HashSet<>();
        for (String string:t){
            convert.add(string.replace(",",COMMA_REPLACEMENT));
        }
        cv.put(Constants.VALUE, convert.toString());
        cr.update(uri, cv, null, null);
    }

    /**
     * 提交Int数据
     * @param name
     * @param t
     */
    public synchronized void putInt(String name, Integer t) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_INT,name);
        ContentValues cv = new ContentValues();
        cv.put(Constants.VALUE, t);
        cr.update(uri, cv, null, null);
    }

    /**
     * 提交Float数据
     * @param name
     * @param t
     */
    public synchronized void putFloat(String name, Float t) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_FLOAT,name);
        ContentValues cv = new ContentValues();
        cv.put(Constants.VALUE, t);
        cr.update(uri, cv, null, null);
    }

    /**
     * 提交Long数据
     * @param name
     * @param t
     */
    public synchronized void putLong(String name, Long t) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_LONG,name);
        ContentValues cv = new ContentValues();
        cv.put(Constants.VALUE, t);
        cr.update(uri, cv, null, null);
    }


    /**
     * 获取Boolean数据
     * @param name
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_BOOLEAN,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(rtn);
    }

    /**
     * 获取String数据
     * @param name
     * @param defaultValue
     * @return
     */
    public String getString(String name, String defaultValue) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_STRING,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return defaultValue;
        }
        return rtn;
    }

    /**
     * 获取Set<String>数据
     * @param name
     * @param defaultValue
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getSetString(String name, Set<String> defaultValue) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_STRING_SET,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return defaultValue;
        }
        if (!rtn.matches("\\[.*\\]")){
            return defaultValue;
        }
        String sub=rtn.substring(1,rtn.length()-1);
        String[] spl=sub.split(", ");
        Set<String> returns=new HashSet<>();
        for (String t:spl){
            returns.add(t.replace(COMMA_REPLACEMENT,", "));
        }
        return returns;
    }

    /**
     * 获取Int数据
     * @param name
     * @param defaultValue
     * @return
     */
    public int getInt(String name, int defaultValue) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_INT,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return defaultValue;
        }
        return Integer.parseInt(rtn);
    }

    /**
     * 获取Float数据
     * @param name
     * @param defaultValue
     * @return
     */
    public float getFloat(String name, float defaultValue) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_FLOAT,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return defaultValue;
        }
        return Float.parseFloat(rtn);
    }

    /**
     * 获取Long数据
     * @param name
     * @param defaultValue
     * @return
     */
    public long getLong(String name, long defaultValue) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_LONG,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return defaultValue;
        }
        return Long.parseLong(rtn);
    }

    /**
     * 是否存在
     * @param name
     * @return
     */
    public boolean contains(String name) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_CONTAIN,name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(Constants.NULL_STRING)) {
            return false;
        } else {
            return Boolean.parseBoolean(rtn);
        }
    }

    /**
     * 移除数据
     * @param name
     */
    public void remove(String name) {
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_LONG,name);
        cr.delete(uri, null, null);
    }

    /**
     * 清除数据
     */
    public void clear(){
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_CLEAN);
        cr.delete(uri,null,null);
    }

    /**
     * 获取所有数据
     * @return
     */
    public Map<String,Object> getAll(){
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = getUri(Constants.TYPE_GET_ALL);
        Cursor cursor=cr.query(uri,null,null,null,null);
        HashMap resultMap=new HashMap();
        if (cursor!=null && cursor.moveToFirst()){
            int nameIndex=cursor.getColumnIndex(Constants.CURSOR_COLUMN_NAME);
            int typeIndex=cursor.getColumnIndex(Constants.CURSOR_COLUMN_TYPE);
            int valueIndex=cursor.getColumnIndex(Constants.CURSOR_COLUMN_VALUE);
            do {
                String key=cursor.getString(nameIndex);
                String type=cursor.getString(typeIndex);
                Object value = null;
                if (type.equalsIgnoreCase(Constants.TYPE_STRING)) {
                    value= cursor.getString(valueIndex);
                }
                else if (type.equalsIgnoreCase(Constants.TYPE_BOOLEAN)) {
                    value= Boolean.valueOf(cursor.getString(valueIndex));
                }
                else if (type.equalsIgnoreCase(Constants.TYPE_INT)) {
                    value= cursor.getInt(valueIndex);
                }
                else if (type.equalsIgnoreCase(Constants.TYPE_LONG)) {
                    value= cursor.getLong(valueIndex);
                }
                else if (type.equalsIgnoreCase(Constants.TYPE_FLOAT)) {
                    value= cursor.getFloat(valueIndex);
                }
                else if (type.equalsIgnoreCase(Constants.TYPE_STRING_SET)) {
                    String str= cursor.getString(valueIndex);
                    if (str.matches("\\[.*\\]")){
                        String sub=str.substring(1,str.length()-1);
                        String[] spl=sub.split(", ");
                        Set<String> returns=new HashSet<>();
                        for (String t:spl){
                            returns.add(t.replace(COMMA_REPLACEMENT,", "));
                        }
                        value=returns;
                    }
                }
                resultMap.put(key,value);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return resultMap;
    }

    /**
     * 提交
     * @param map
     */
    public void commit(Map<String,Object> map){
        if(null!=map){
            ContentResolver cr = mContext.getContentResolver();
            Uri uri = getUri(Constants.TYPE_COMMIT);
            ContentValues cv = new ContentValues();
            try {
                Object value;
                for(String key:map.keySet()){
                    value = map.get(key);
                    if(value instanceof Set){
                        Set<String> convert=new HashSet<>();
                        for (String string:(Set<String>)value){
                            convert.add(string.replace(",",COMMA_REPLACEMENT));
                        }
                        cv.put(key,convert.toString());
                    }
                    else if(value instanceof String){
                        cv.put(key,(String)value);
                    }
                    else if(value instanceof Integer){
                        cv.put(key,(Integer)value);
                    }
                    else if(value instanceof Float){
                        cv.put(key,(Float)value);
                    }
                    else if(value instanceof Long){
                        cv.put(key,(Long)value);
                    }
                    else if(value instanceof Boolean){
                        cv.put(key,(Boolean)value);
                    }
                }
                cr.update(uri, cv, null, null);
            }catch (Exception e){e.printStackTrace();}
        }
    }

    /**
     * 获取Uri
     * @param type
     * @return
     */
    private Uri getUri(String type){
        return getUri(type,null);
    }

    /**
     * 获取Uri
     * @param type
     * @param name
     * @return
     */
    private Uri getUri(String type, String name){
        String uriStr = getRootUri();
        if(!TextUtils.isEmpty(type)){
            uriStr=uriStr  + "&" + Constants.URI_QUERY_PARAMETER_TYPE + "=" + type;
        }
        if(!TextUtils.isEmpty(name)){
            uriStr=uriStr  + "&" + Constants.URI_QUERY_PARAMETER_KEY + "=" + name;
        }
        return Uri.parse(uriStr);
    }

    /**
     * 获取跟Uri
     * content://io.dcloud.streamapps.hostcontentprovider/SharedPreferences?name=xx&mode=1
     * @return
     */
    private String getRootUri(){
        if(null==mRootUri){
            mRootUri = Constants.CONTENT_URI+ Constants.SEPARATOR + Constants.URI_PATH_SHARED_PREFERENCES +"?"+
                    Constants.URI_QUERY_PARAMETER_NAME + "=" + mSpName + "&"+
                    Constants.URI_QUERY_PARAMETER_MODE + "=" + mMode ;
        }
        return mRootUri ;
    }
}