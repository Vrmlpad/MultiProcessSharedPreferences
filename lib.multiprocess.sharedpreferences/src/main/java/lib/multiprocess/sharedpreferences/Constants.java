package lib.multiprocess.sharedpreferences;
/**
 * Created by l4656_000 on 2015/11/30.
 */
public class Constants {
    // normal constants
    public static final String CONTENT="content://";
    public static final String AUTHORITY="lib.multiprocess.sharedpreferences.hostcontentprovider";
    public static final String SEPARATOR= "/";
    public static final String CONTENT_URI =CONTENT+AUTHORITY;
    public static final String TYPE_STRING_SET="string_set";
    public static final String TYPE_STRING="string";
    public static final String TYPE_INT="int";
    public static final String TYPE_LONG="long";
    public static final String TYPE_FLOAT="float";
    public static final String TYPE_BOOLEAN="boolean";
    public static final String VALUE= "value";

    public static final String NULL_STRING= "null";
    public static final String TYPE_CONTAIN="contain";
    public static final String TYPE_CLEAN="clean";
    public static final String TYPE_COMMIT="commit";
    public static final String TYPE_GET_ALL="get_all";

    public static final String CURSOR_COLUMN_NAME = "cursor_name";
    public static final String CURSOR_COLUMN_TYPE = "cursor_type";
    public static final String CURSOR_COLUMN_VALUE = "cursor_value";

    public static final String URI_PATH_SHARED_PREFERENCES ="SharedPreferences";
    public static final String URI_QUERY_PARAMETER_NAME ="name";
    public static final String URI_QUERY_PARAMETER_MODE ="mode";
    public static final String URI_QUERY_PARAMETER_KEY ="key";
    public static final String URI_QUERY_PARAMETER_TYPE ="type";
}
