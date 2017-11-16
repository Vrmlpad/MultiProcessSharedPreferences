package lib.multiprocess.sharedpreferences;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HostContentProvider extends ContentProvider{

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(null!=uri){
            String path = filterPath(uri.getPath());
            if(Constants.URI_PATH_SHARED_PREFERENCES.equals(path)){
                String spName=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_NAME);
                String mode=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_MODE);
                String type=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_TYPE);
                if (type.equals(Constants.TYPE_GET_ALL)){
                    Map<String, ?> all = SPHelperImpl.getAll(getContext(),spName,mode);
                    if (all!=null) {
                        MatrixCursor cursor = new MatrixCursor(new String[]{Constants.CURSOR_COLUMN_NAME, Constants.CURSOR_COLUMN_TYPE, Constants.CURSOR_COLUMN_VALUE});
                        Set<String> keySet = all.keySet();
                        for (String key:keySet){
                            Object[] rows=new Object[3];
                            rows[0]=key;
                            rows[2]=all.get(key);
                            if (rows[2] instanceof Boolean) {
                                rows[1]= Constants.TYPE_BOOLEAN;
                            }
                            else if (rows[2] instanceof String) {
                                rows[1]= Constants.TYPE_STRING;
                            }
                            else if (rows[2] instanceof Integer) {
                                rows[1]= Constants.TYPE_INT;
                            }
                            else if (rows[2] instanceof Long) {
                                rows[1]= Constants.TYPE_LONG;
                            }
                            else if (rows[2] instanceof Float) {
                                rows[1]= Constants.TYPE_FLOAT;
                            }
                            else if (rows[2] instanceof Set) {
                                rows[2] = rows[2].toString();
                                rows[1]= Constants.TYPE_STRING_SET;
                            }
                            cursor.addRow(rows);
                        }
                        return cursor;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if(null!=uri){
            String path = filterPath(uri.getPath());
            if(Constants.URI_PATH_SHARED_PREFERENCES.equals(path)){
                String spName=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_NAME);
                String mode=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_MODE);
                String type=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_TYPE);
                String key=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_KEY);
                if (type.equals(Constants.TYPE_CONTAIN)){
                    return SPHelperImpl.contains(getContext(),spName,mode,key)+"";
                }
                return ""+SPHelperImpl.get(getContext(),spName,mode,key,type);
            }
        }
        return "";
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(null!=uri){
            String path = filterPath(uri.getPath());
            if(Constants.URI_PATH_SHARED_PREFERENCES.equals(path)){
                String spName=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_NAME);
                String mode=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_MODE);
                String type=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_TYPE);
                String key=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_KEY);
                if(Constants.TYPE_COMMIT.equals(type)){
                    Map<String,Object> map= new HashMap<String,Object>();
                    if(null!=values && 0<values.size()){
                        Set<String> sets=values.keySet();
                        for(String keyT:sets){
                            map.put(keyT,values.get(keyT));
                        }
                    }
                    SPHelperImpl.commit(getContext(),spName,mode,map);
                }else {
                    SPHelperImpl.saveValue(getContext(),spName,type,mode,key,values.get(Constants.VALUE));
                }
            }
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(null!=uri){
            String path = filterPath(uri.getPath());
            if(Constants.URI_PATH_SHARED_PREFERENCES.equals(path)){
                String spName=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_NAME);
                String mode=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_MODE);
                String type=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_TYPE);
                if (type.equals(Constants.TYPE_CLEAN)){
                    SPHelperImpl.clear(getContext(),spName,mode);
                    return 0;
                }
                String key=uri.getQueryParameter(Constants.URI_QUERY_PARAMETER_KEY);
                if (SPHelperImpl.contains(getContext(),spName,mode,key)){
                    SPHelperImpl.remove(getContext(),spName,mode,key);
                }
            }
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
       insert(uri,values);
       return 0;
    }

    /**
     * 过滤从Uri中获取的path
     * @param path
     * @return
     */
    private String filterPath(String path){
        if(!TextUtils.isEmpty(path)){
            return path.replace(Constants.SEPARATOR,"");
        }
        return null;
    }
}