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

public class vhod extends AppCompatActivity implements View.OnClickListener{

    Button btnAdds, btnClears,snazad;
    EditText etFIO, etLogin, etPassword;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValuesv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhod);
        btnAdds = (Button) findViewById(R.id.btnAdds);
        btnAdds.setOnClickListener(this);

        snazad=(Button)findViewById(R.id.snazad);
        snazad.setOnClickListener(this);

        btnClears = (Button) findViewById(R.id.btnClears);
        btnClears.setOnClickListener(this);

        etFIO = findViewById(R.id.FIO);
        etLogin = findViewById(R.id.Login);
        etPassword = findViewById(R.id.Passwords);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();


        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);
        TableLayout Output = findViewById(R.id.Outrut);
        Output.removeAllViews();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_IDs);
            int FIOIndex = cursor.getColumnIndex(DBHelper.KEY_FIO);
            int LoginIndex = cursor.getColumnIndex(DBHelper.KEY_USERs);
            int PasswordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORDs);
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

                Output.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAdds: //Добавление данных в БД

                String fio = etFIO.getText().toString();
                String logins = etLogin.getText().toString();
                String passwords = etPassword.getText().toString();

                contentValuesv = new ContentValues();

                contentValuesv.put(DBHelper.KEY_FIO, fio);
                contentValuesv.put(DBHelper.KEY_USERs, logins);
                contentValuesv.put(DBHelper.KEY_PASSWORDs, passwords);

                database.insert(DBHelper.TABLE_SOTRYDNIK, null, contentValuesv);

                UpdateTable();
                break;

            case R.id.btnClears: //Удаление данных из БД
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
                contentValuesv = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);
                if(cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_IDs);
                    int FIOIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_FIO);
                    int LoginIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_USERs);
                    int PasswordIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PASSWORDs);

                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValuesv.put(DBHelper.KEY_IDs, realID);
                            contentValuesv.put(DBHelper.KEY_FIO, cursorUpdater.getString(FIOIndex));
                            contentValuesv.put(DBHelper.KEY_USERs, cursorUpdater.getString(LoginIndex));
                            contentValuesv.put(DBHelper.KEY_PASSWORDs, cursorUpdater.getString(PasswordIndex));


                            database.replace(DBHelper.TABLE_SOTRYDNIK, null, contentValuesv);
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
            case R.id.snazad: //выход назад
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}