package com.example.MyChronicles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AddNotesActivity extends AppCompatActivity {

    private static final String AES = "AES";

    EditText title, description, key;
    Button addNote;
    DatabaseClass db;
    private static final String ALGORITHM = "Blowfish";
    private static final String MODE = "Blowfish/CBC/PKCS5Padding";
    private static final String IV = "abcdefgh";
    private static String KEY;
    private String SECRET_KEY;

    String encryptedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        key = findViewById(R.id.key);
        addNote = findViewById(R.id.addNote);
        KEY = key.toString();
        db = new DatabaseClass(this);


        addNote.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()))
                {

                    String textForEncryption = description.getText().toString();
                    SECRET_KEY = key.getText().toString();
                    try {
                        encryptedString = encrypt(textForEncryption, SECRET_KEY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String titleboy = title.getText().toString();
                    boolean isInserted = ds.addmynotes(titleboy,encryptedString);
                    DatabaseClass db = new DatabaseClass(AddNotesActivity.this);

                    Intent intent = new Intent(AddNotesActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(AddNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] enval = c.doFinal(Data.getBytes(StandardCharsets.UTF_8));
        String encryptedValue = Base64.getEncoder().encodeToString(enval);
        return encryptedValue;
    }
    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
    boolean isBound = false;
    private databaseservice ds;
    private final ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder)
        {
            databaseservice.MyLocalBinder mlb = (databaseservice.MyLocalBinder) iBinder;
            ds = mlb.getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    @Override
    public void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, databaseservice.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
    public void onStop() {
        super.onStop();
        if (isBound)
        {
            unbindService(mConnection);
            isBound = false;
        }
    }


}