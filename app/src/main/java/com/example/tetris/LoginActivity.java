package com.example.tetris;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView text_title;
    private EditText edit_account, edit_password;
    private TextView text_msg, text_delete;
    private Button btn_login, btn_register;
    private ImageButton openpwd;
    private boolean flag = false;
    private String account, password;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }
    private void init() {
        text_title = (TextView) findViewById(R.id.text_title);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.yan);
        text_title.setTypeface(typeface);

        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_account.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_account.clearFocus();
                }
                return false;
            }
        });
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_password.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0);
                }
                return false;
            }
        });
        text_msg = (TextView) findViewById(R.id.text_msg);
        text_delete = (TextView) findViewById(R.id.text_delete);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        openpwd = (ImageButton) findViewById(R.id.btn_openpwd);
        text_msg.setOnClickListener(this);
        text_delete.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        openpwd.setOnClickListener(this);
        dbHelper = new DBHelper(this, "Data.db", null, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edit_account.getText().toString().trim().equals("") | edit_password.getText().
                        toString().trim().equals("")) {
                    Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    readUserInfo();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_openpwd:
                if (flag == true) {//???????????????
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    openpwd.setBackgroundResource(R.drawable.invisible);
                } else { //????????????
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    openpwd.setBackgroundResource(R.drawable.visible);
                }
                break;
            case R.id.text_msg:
                Intent i = new Intent(LoginActivity.this, ForgotInfoActivity.class);
                startActivity(i);
                break;
                //?????????DeleteActivity
            case R.id.text_delete:
                Intent intent1 = new Intent(LoginActivity.this, DeleteActivity.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * ??????UserData.db??????????????????
     * */
    protected void readUserInfo() {
        if (login(edit_account.getText().toString(), edit_password.getText().toString())) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, UsersInfoActivity.class);
            intent.putExtra("Username",edit_account.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * ??????????????????
     * */
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from usertable where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}