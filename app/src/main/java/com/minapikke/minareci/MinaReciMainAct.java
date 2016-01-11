package com.minapikke.minareci;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;


public class MinaReciMainAct extends ActionBarActivity {
//public class MinaReciMainAct extends Activity {
    private EditText editText;
    private InputMethodManager inputMethodManager;
    private Button updateButton;
    private ArrayList<MinaReciItem> reciItemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mina_reci_main);

        this.reciItemArray = null;

        //キーボードを閉じたいEditTextオブジェクト
        editText = (EditText) findViewById(R.id.editText);
        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //EditTextにリスナーをセット
        editText.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    AsyncHttpRequest task = new AsyncHttpRequest(MinaReciMainAct.this);
                    task.setSearchWord(editText.getText().toString());

                    if(task.getSearchWord().equals("")){ return false; }

                    if(MinaReciMainAct.this.reciItemArray != null){
                        task.setReciItemArrayPrev(MinaReciMainAct.this.reciItemArray);
                    }
                    task.execute();
                    MinaReciMainAct.this.reciItemArray = task.getReciItemArray();
                    return true;
                }

                return false;
            }
        });

        //レシピ更新ボタンオブジェクト
        updateButton = (Button) findViewById(R.id.button1);

        //Buttonにリスナーをセット
        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AsyncHttpRequest task = new AsyncHttpRequest(MinaReciMainAct.this);
                task.setSearchWord(editText.getText().toString());

                if (task.getSearchWord().equals("")) { return; }

                if(MinaReciMainAct.this.reciItemArray != null){
                    task.setReciItemArrayPrev(MinaReciMainAct.this.reciItemArray);
                }
                task.execute();
                MinaReciMainAct.this.reciItemArray = task.getReciItemArray();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mina_reci_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClickTextView1(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(0).getReciUrl()));
        startActivity(i);
    }

    public void onClickTextView2(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(1).getReciUrl()));
        startActivity(i);
    }

    public void onClickTextView3(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(2).getReciUrl()));
        startActivity(i);
    }

    public void onClickTextView4(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(3).getReciUrl()));
        startActivity(i);
    }

    public void onClickTextView5(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(4).getReciUrl()));
        startActivity(i);
    }

    public void onClickTextView6(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(5).getReciUrl()));
        startActivity(i);
    }

    public void onClickTextView7(View view) {
        if(this.reciItemArray == null) return;
        if(this.reciItemArray.size() == 0) return;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cookpad.com" + this.reciItemArray.get(6).getReciUrl()));
        startActivity(i);
    }

    public boolean appInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();

        try{
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    public boolean sendTextToLine(Context context, String lineComment){
        try {
            String lineString = "line://msg/text/" + lineComment;
            Intent intent = Intent.parseUri(lineString, Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(this, "LINEの投稿に失敗しました", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void onClickButton2(View view) {
        if(this.reciItemArray == null) return;

        if (!this.appInstalled(this, "jp.naver.line.android")){
            Toast.makeText(this, "LINEアプリのインストールが必要です", Toast.LENGTH_SHORT).show();
            return;
        }

        this.sendTextToLine(this,
                  this.reciItemArray.get(0).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(0).getReciUrl()
                + this.reciItemArray.get(1).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(1).getReciUrl()
                + this.reciItemArray.get(2).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(2).getReciUrl()
                + this.reciItemArray.get(3).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(3).getReciUrl()
                + this.reciItemArray.get(4).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(4).getReciUrl()
                + this.reciItemArray.get(5).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(5).getReciUrl()
                + this.reciItemArray.get(6).getReciTitle() + "http://cookpad.com" + this.reciItemArray.get(6).getReciUrl()
        );
    }
}
