package com.example.MyChronicles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    EditText et_email, et_password;
    Button btn_login;
    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userName,pass;
    @Override
    public void onAttach(Context context)
    {
        sharedPreferences = context.getSharedPreferences("usersFile",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        super.onAttach(context);
    }
    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        intent = new Intent(getActivity(), MainActivity.class);
        et_email = view .findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);

        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = et_email.getText().toString();
                pass = et_password.getText().toString();
                String uName, uPass;
                uName = sharedPreferences.getString("userName",null);
                uPass = sharedPreferences.getString("pass",null);

                if (userName.equals(uName) && pass.equals(uPass))
                {
                    startActivity(intent);
                    Toast.makeText(getContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Invalid Username and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}