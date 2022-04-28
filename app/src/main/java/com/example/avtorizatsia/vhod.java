package com.example.avtorizatsia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class vhod extends AppCompatActivity implements View.OnClickListener{

    Button btnAdds, btnClears,snazad;
    EditText etFIO, etLogin, etPassword;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhod);
        btnAdds = (Button) findViewById(R.id.btnAdds);
        btnAdds.setOnClickListener(this);


        btnClears = (Button) findViewById(R.id.btnClears);
        btnClears.setOnClickListener(this);

        etFIO = findViewById(R.id.FIO);
        etLogin = findViewById(R.id.Login);
        etPassword = findViewById(R.id.Passwords);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        etFIO.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etFIO.setHint("");
            else
                etFIO.setHint("ФИО");
        });

        etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLogin.setHint("");
            else
                etLogin.setHint("Логин");
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etPassword.setHint("");
            else
                etPassword.setHint("Пароль");
        });
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_IDs);
            int FIOIndex = cursor.getColumnIndex(DBHelper.KEY_FIO);
            int LoginIndex = cursor.getColumnIndex(DBHelper.KEY_USERs);
            int PasswordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORDs);

            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputFIO = new TextView(this);
                params.weight = 3.0f;
                outputFIO.setLayoutParams(params);
                outputFIO.setText(cursor.getString(FIOIndex));
                dbOutputRow.addView(outputFIO);

                TextView outputLogin = new TextView(this);
                params.weight = 3.0f;
                outputLogin.setLayoutParams(params);
                outputLogin.setText(cursor.getString(LoginIndex));
                dbOutputRow.addView(outputLogin);

                TextView outputPassword = new TextView(this);
                params.weight = 3.0f;
                outputPassword.setLayoutParams(params);
                outputPassword.setText(cursor.getString(PasswordIndex));
                dbOutputRow.addView(outputPassword);



                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight=1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAdds:
                String Fio = etFIO.getText().toString();
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();

                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_FIO, Fio);
                contentValues.put(DBHelper.KEY_USERs, login);
                contentValues.put(DBHelper.KEY_PASSWORDs, password);
                database.insert(DBHelper.TABLE_SOTRYDNIK, null, contentValues);
                UpdateTable();
                etFIO.setText("");
                etLogin.setText("");
                etPassword.setText("");

                break;

            case R.id.btnClears:
                database.delete(DBHelper.TABLE_SOTRYDNIK, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();


                database.delete(DBHelper.TABLE_SOTRYDNIK, DBHelper.KEY_IDs+" = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);
                if(cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_IDs);
                    int FIOIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_FIO);
                    int LoginIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_USERs);
                    int PasswordIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PASSWORDs);

                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_IDs, realID);
                            contentValues.put(DBHelper.KEY_FIO, cursorUpdater.getString(FIOIndex));
                            contentValues.put(DBHelper.KEY_USERs, cursorUpdater.getString(LoginIndex));
                            contentValues.put(DBHelper.KEY_PASSWORDs, cursorUpdater.getString(PasswordIndex));


                            database.replace(DBHelper.TABLE_SOTRYDNIK, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()) {
                        database.delete(DBHelper.TABLE_SOTRYDNIK, DBHelper.KEY_IDs + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();

                }
                cursorUpdater.close();
                break;
            case R.id.snazad:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}