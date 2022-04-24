package com.example.avtorizatsia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class vhod extends AppCompatActivity implements View.OnClickListener{

    TextView nameField, familioField, othestoField, usernameField, passwordField;
    Button  signinBtn;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhod);

        familioField=findViewById(R.id.familio);
        nameField=findViewById(R.id.name);
        othestoField=findViewById(R.id.othestvo);
        usernameField=findViewById(R.id.Username);
        passwordField=findViewById(R.id.Password);


        signinBtn=findViewById(R.id.registr2);
        signinBtn.setOnClickListener(this);

        dbHelper= new DBHelper(this);
        database=dbHelper.getWritableDatabase();

}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registr:
                Cursor Signcursor=database.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);

                boolean finded=false;
                if(Signcursor.moveToFirst()){
                    int usernameIndex=Signcursor.getColumnIndex(DBHelper.KEY_USERs);
                    do{
                        if (usernameField.getText().toString().equals(Signcursor.getString(usernameIndex))){
                            Toast.makeText(this,"Введенный вами логин уже зарегистрирован.",Toast.LENGTH_LONG).show();
                            finded=true;
                            break;
                        }
                    }while (Signcursor.moveToNext());
                }
                if(!finded){
                    ContentValues contentValues= new ContentValues();
                    contentValues.put(DBHelper.KEY_FAMILIA,familioField.getText().toString());
                    contentValues.put(DBHelper.KEY_NAME,nameField.getText().toString());
                    contentValues.put(DBHelper.KEY_OTHESTVO,othestoField.getText().toString());
                    contentValues.put(DBHelper.KEY_USERs,usernameField.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORDs,passwordField.getText().toString());
                    database.insert(DBHelper.TABLE_CONTACTS,null,contentValues);
                    Toast.makeText(this,"Вы успешно зарегистрировались",Toast.LENGTH_LONG).show();
                }
                Signcursor.close();
                break;
        }
    }
}