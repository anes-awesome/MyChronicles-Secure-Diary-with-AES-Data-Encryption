package com.example.MyChronicles;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class databaseservice extends Service
{
    private final MyLocalBinder mlb = new MyLocalBinder();
    public databaseservice()
    {
    }
    public class MyLocalBinder extends Binder
    {
        databaseservice getService()
        {
            return databaseservice.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent i)
    {
        return mlb;
    }

public Boolean addmynotes(String titleboy,String encryptedString)
{
    DatabaseClass db = new DatabaseClass(this);
     return db.addNotes(titleboy, encryptedString);
}
}