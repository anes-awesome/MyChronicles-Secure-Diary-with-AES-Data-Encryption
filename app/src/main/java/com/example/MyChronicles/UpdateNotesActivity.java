package com.example.MyChronicles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UpdateNotesActivity extends AppCompatActivity {

    private static final String AES = "AES";

    private static final String ALGORITHM = "Blowfish";
    private static final String MODE = "Blowfish/CBC/PKCS5Padding";
    private static final String IV = "abcdefgh";
    private static String KEY;
    private String SECRET_KEY;

    String encryptedString;


    EditText date, title,description,key;
    Button decryptData, updateNotes;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        date=findViewById(R.id.date);
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        decryptData=findViewById(R.id.decryptData);
        updateNotes=findViewById(R.id.updateNote);
        key = findViewById(R.id.key);

        Intent i =getIntent();
        title.setText(i.getStringExtra("title"));
        description.setText(i.getStringExtra("description"));
        id=i.getStringExtra("id");

        decryptData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                Cursor res = db.getAllData(title.getText().toString());
                if (res.getCount() == 0) {
                    Toast.makeText(UpdateNotesActivity.this, "error", Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                   buffer.append(res.getString(2));
                }
                SECRET_KEY = key.getText().toString();
                String enc_test = buffer.toString();
                encryptedString = enc_test;
                String decryptedString = "empty";
                try{
                    decryptedString = decrypt(encryptedString, SECRET_KEY);
                } catch (Exception e) {
                }
                description.setText((decryptedString));
            }
        });

        updateNotes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()))
                {
                    DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                    String textForEncryption = description.getText().toString();
                    SECRET_KEY = key.getText().toString();
                    try {
                        encryptedString = encrypt(textForEncryption, SECRET_KEY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    db.updateNotes(title.getText().toString(),encryptedString,id);
                    Intent i=new Intent(UpdateNotesActivity.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(UpdateNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String value, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(value);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

}