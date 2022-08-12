package com.example.MyChronicles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    EditText et_name, et_email, et_password;
    Button btn_register;
    String userName,email,pass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context)
    {
        sharedPreferences = context.getSharedPreferences("usersFile",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        et_name = view .findViewById(R.id.et_name);
        et_email = view .findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);

        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = et_email.getText().toString();
                email = et_name.getText().toString();
                pass = et_password.getText().toString();

                editor.putString("userName",userName);
                editor.putString("pass",pass);
                editor.putString("email",email);
                editor.apply();
                Toast.makeText(getContext(),"User Registered Successful", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}