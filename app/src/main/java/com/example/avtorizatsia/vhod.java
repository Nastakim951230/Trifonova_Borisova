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

    Button btnAdds,  btnClears, btnNazads;
    EditText etFio, etLogin, etParol;
    SQLiteDatabase database;
    DBHelper dbHelper;
    ContentValues contentValuesw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhod);
        btnAdds = (Button) findViewById(R.id.btnAdds);
        btnAdds.setOnClickListener(this);

        btnNazads=(Button)  findViewById(R.id.snazad);
        btnNazads.setOnClickListener(this);

        btnClears = (Button) findViewById(R.id.btnClears);
        btnClears.setOnClickListener(this);


        etFio=(EditText) findViewById(R.id.etFio);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etParol=(EditText) findViewById(R.id.etParol);
        dbHelper = new DBHelper(this);
        database=dbHelper.getWritableDatabase();

        UpdateTable();
    }
    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);

        TableLayout dbOutput=findViewById(R.id.Outrut);
        dbOutput.removeAllViews();
        if (cursor.moveToFirst()) {
            int idIndexs = cursor.getColumnIndex(DBHelper.KEY_IDs);
            int fioIndex= cursor.getColumnIndex(DBHelper.KEY_FIO);
            int LOGINIndex = cursor.getColumnIndex(DBHelper.KEY_USERs);
            int ParolIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORDs);
            do{
                TableRow dbOutputRow=new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID= new TextView(this);
                params.weight=1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndexs));
                dbOutputRow.addView(outputID);


                TextView outputFio= new TextView(this);
                params.weight=3.0f;
                outputFio.setLayoutParams(params);
                outputFio.setText(cursor.getString(fioIndex));
                dbOutputRow.addView(outputFio);

                TextView outputLogin= new TextView(this);
                params.weight=3.0f;
                outputLogin.setLayoutParams(params);
                outputLogin.setText(cursor.getString(LOGINIndex));
                dbOutputRow.addView(outputLogin);

                TextView outputParol= new TextView(this);
                params.weight=3.0f;
                outputParol.setLayoutParams(params);
                outputParol.setText(cursor.getString(ParolIndex));
                dbOutputRow.addView(outputParol);

                Button deleteBtn= new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight=1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setId(cursor.getInt(idIndexs));
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

                String fio = etFio.getText().toString();
                String login = etLogin.getText().toString();
                String parol = etParol.getText().toString();

                contentValuesw = new ContentValues();

                contentValuesw.put(DBHelper.KEY_FIO, fio);
                contentValuesw.put(DBHelper.KEY_USERs, login);
                contentValuesw.put(DBHelper.KEY_PASSWORDs, parol);

                database.insert(DBHelper.TABLE_SOTRYDNIK, null, contentValuesw);

                UpdateTable();
                break;

            case R.id.btnClears:
                database.delete(DBHelper.TABLE_SOTRYDNIK, null, null);
                TableLayout dbOutput=findViewById(R.id.Outrut);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            default:
                View outputDBRow =(View) v.getParent();
                ViewGroup outputDB =(ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_SOTRYDNIK, DBHelper.KEY_ID+"=?", new String[]{String.valueOf(v.getId())});

                contentValuesw=new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idsIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_IDs);

                    int fioIndex=cursorUpdater.getColumnIndex(DBHelper.KEY_FIO);
                    int LoginIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_USERs);
                    int ParolIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PASSWORDs);
                    int realID=1;
                    do{
                        if(cursorUpdater.getInt(idsIndex)>realID)
                        {
                            contentValuesw.put(DBHelper.KEY_IDs,realID);

                            contentValuesw.put(DBHelper.KEY_FIO,cursorUpdater.getString(fioIndex));
                            contentValuesw.put(DBHelper.KEY_USERs,cursorUpdater.getString(LoginIndex));
                            contentValuesw.put(DBHelper.KEY_PASSWORDs,cursorUpdater.getString(ParolIndex));
                            database.replace(DBHelper.TABLE_SOTRYDNIK,null,contentValuesw);
                        }
                        realID++;
                    }while (cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast()){
                        database.delete(DBHelper.TABLE_SOTRYDNIK,DBHelper.KEY_IDs+"=?",new String[]{cursorUpdater.getString(idsIndex)});
                    }
                    UpdateTable();
                }

                break;
            case R.id.snazad:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}