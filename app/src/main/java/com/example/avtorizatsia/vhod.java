package com.example.avtorizatsia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class vhod extends AppCompatActivity implements View.OnClickListener{

    TextView nameField, familioField, othestoField, usernameFields, passwordFields;
    Button  signinBtn,loginBtn;

    DBHelper dbHelper;
    SQLiteDatabase databases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhod);

        familioField=findViewById(R.id.familio);
        nameField=findViewById(R.id.name);
        othestoField=findViewById(R.id.othestvo);
        usernameFields=findViewById(R.id.username2);
        passwordFields=findViewById(R.id.password2);


        signinBtn=findViewById(R.id.registr2);
        signinBtn.setOnClickListener(this);

        loginBtn=findViewById(R.id.vhod2);
        loginBtn.setOnClickListener(this);

        dbHelper= new DBHelper(this);
        databases=dbHelper.getWritableDatabase();

}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vhod2:
                Cursor logcursorn=databases.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);

                boolean loqqed=false;
                if (logcursorn.moveToFirst()){

                    int usernameSIndex=logcursorn.getColumnIndex(DBHelper.KEY_USERs);
                    int passwordSIndex=logcursorn.getColumnIndex(DBHelper.KEY_PASSWORDs);
                    do{
                        if(usernameFields.getText().toString().equals(logcursorn.getString(usernameSIndex))&& passwordFields.getText().toString().equals(logcursorn.getString(passwordSIndex))){
                            startActivity(new Intent(this,BD.class));
                            loqqed=true;
                            break;
                        }
                    }while (logcursorn.moveToNext());
                }
                logcursorn.close();
                if(!loqqed) Toast.makeText(this,"Введенная комбинация логина и пароля не была найдина.",Toast.LENGTH_LONG).show();

                break;
            case R.id.registr2:
                Cursor Signcursor=databases.query(DBHelper.TABLE_SOTRYDNIK, null, null, null, null, null, null);

                boolean finded=false;
                if(Signcursor.moveToFirst()){
                    int usernameIndexS=Signcursor.getColumnIndex(DBHelper.KEY_USERs);
                    do{
                        if (usernameFields.getText().toString().equals(Signcursor.getString(usernameIndexS))){
                            Toast.makeText(this,"Введенный вами логин уже зарегистрирован.",Toast.LENGTH_LONG).show();
                            finded=true;
                            break;
                        }
                    }while (Signcursor.moveToNext());
                }
                if(!finded){
                    ContentValues contentValuesn= new ContentValues();
                    contentValuesn.put(DBHelper.KEY_FAMILIA,familioField.getText().toString());
                    contentValuesn.put(DBHelper.KEY_NAME,nameField.getText().toString());
                    contentValuesn.put(DBHelper.KEY_OTHESTVO,othestoField.getText().toString());
                    contentValuesn.put(DBHelper.KEY_USERs,usernameFields.getText().toString());
                    contentValuesn.put(DBHelper.KEY_PASSWORDs,passwordFields.getText().toString());
                    databases.insert(DBHelper.TABLE_SOTRYDNIK,null,contentValuesn);
                    Toast.makeText(this,"Вы успешно зарегистрировались",Toast.LENGTH_LONG).show();
                }
                Signcursor.close();
                break;
        }
    }
}