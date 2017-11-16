package com.vp.lp.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.sphelpersample.R;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lib.multiprocess.sharedpreferences.SPMultiProcessImpl;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private SPMultiProcessImpl mSPMultiProcessImpl =null;
    private Button mJump,mCommit,mQuery,mRemove,mCommitSetString,mQuerySetString,mQueryAll,mClearAll;
    private EditText mKey,mValue;
    private TextView mResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.title)).setText(getString(R.string.title)+this.getClass().getSimpleName());
        initUI();
        mSPMultiProcessImpl = new SPMultiProcessImpl(getApplicationContext(),"test");
    }

    /**
     * 初始化UI
     */
    private void initUI(){
        mJump = (Button)findViewById(R.id.jump);
        mCommit = (Button)findViewById(R.id.commit);
        mQuery = (Button)findViewById(R.id.query);
        mRemove = (Button)findViewById(R.id.remove);
        mCommitSetString = (Button)findViewById(R.id.commitSetString);
        mQuerySetString = (Button)findViewById(R.id.querySetString);
        mQueryAll = (Button)findViewById(R.id.queryAll);
        mClearAll = (Button)findViewById(R.id.clearAll);
        mKey = (EditText)findViewById(R.id.key);
        mValue = (EditText)findViewById(R.id.value);
        mResult = (TextView)findViewById(R.id.result);

        mJump.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mQuery.setOnClickListener(this);
        mRemove.setOnClickListener(this);
        mCommitSetString.setOnClickListener(this);
        mQuerySetString.setOnClickListener(this);
        mQueryAll.setOnClickListener(this);
        mClearAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jump:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ChildProcessActivity.class);
                startActivity(intent);
                break;
            case R.id.commit:
                {
                    String key = mKey.getText().toString();
                    String value = mValue.getText().toString();
                    if(!TextUtils.isEmpty(key)){
                        if(null==value){
                            value = "";
                        }
                        mSPMultiProcessImpl.edit().putString(key,value).apply();
                    }
                }
                break;
            case R.id.query:
                {
                    String key = mKey.getText().toString();
                    if(!TextUtils.isEmpty(key)){
                        mResult.setText(mSPMultiProcessImpl.getString(key,""));
                    }
                }
                break;
            case R.id.remove:
                {
                    String key = mKey.getText().toString();
                    if(!TextUtils.isEmpty(key)){
                        mSPMultiProcessImpl.edit().remove(key).apply();
                    }
                }
                break;
            case R.id.commitSetString:
                {
                    Set set=new HashSet();
                    set.add("123");
                    set.add("Hello");
                    set.add("你好");
                    mSPMultiProcessImpl.edit().putStringSet("SetString",set).apply();
                }
                break;
            case R.id.querySetString:
                Set set= mSPMultiProcessImpl.getStringSet("SetString",null);
                if(null!=set){
                    mResult.setText(set.toString());
                }
                break;
            case R.id.queryAll:
                {
                    Map<String,?> all= mSPMultiProcessImpl.getAll();
                    if(null!=all){
                        Set<String> sets=all.keySet();
                        StringBuffer sb=new StringBuffer();
                        for(String key:sets){
                            sb.append(key).append(":").append(all.get(key)).append("\n");
                        }
                        mResult.setText(sb.toString());
                    }
                }
                break;
            case R.id.clearAll:
                {
                    mSPMultiProcessImpl.edit().clear().apply();
                    mResult.setText("");
                }
                break;
        }
    }
}
