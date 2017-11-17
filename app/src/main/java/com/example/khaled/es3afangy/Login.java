package com.example.khaled.es3afangy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Login extends AppCompatActivity {

    Button loginButton;
    EditText emailEditText;
    EditText passwordEditText;
    ProgressBar nproProgressBar;

    int indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.email_editTxt);
        passwordEditText = (EditText) findViewById(R.id.password_editTxt);
        loginButton = (Button) findViewById(R.id.Login_button);
        nproProgressBar = (ProgressBar) findViewById(R.id.progressBar_login);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //LOGIN
                Backendless.setUrl(Defaults.SERVER_URL);
                Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);


                if (validEmail() && validPassWord()) {
                    nproProgressBar.setVisibility(View.VISIBLE);


                    Backendless.UserService.login(emailEditText.getText().toString(), passwordEditText.getText().toString(), new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {
                                    Log.e("Response",  response.getProperty("indicator").toString());
                                    indicator= (int) response.getProperty("indicator");

                                    if(indicator==1){
                                        //login as Super Admin
                                        Intent i = new Intent(Login.this, Admin.class);
                                        i.putExtra("response", response);
                                        startActivity(i);
                                        finish();
                                        nproProgressBar.setVisibility(View.INVISIBLE);

                                    }
                                    else if (indicator==2){
                                        //login as Organization Manager
                                        Intent i = new Intent(Login.this, OrganizationManagerView.class);
                                        i.putExtra("response", response);
                                        startActivity(i);
                                        finish();
                                        nproProgressBar.setVisibility(View.INVISIBLE);



                                    }
                                    else if(indicator==3){
                                        //login as Decision Maker
                                        Intent i = new Intent(Login.this, DecisionMakerView.class);
                                        i.putExtra("response", response);
                                        startActivity(i);
                                        finish();
                                        nproProgressBar.setVisibility(View.INVISIBLE);

                                    }


                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Toast.makeText(Login.this, "wrong Username or Password", Toast.LENGTH_SHORT).show();
                                    nproProgressBar.setVisibility(View.INVISIBLE);


                                    Log.e("getMessage", "handleFault: " + fault.toString());


                                }
                            }
                            , true);
                } else {
                    emailEditText.setError("invalid username");
                    passwordEditText.setError("invalid password");

                }


            }
        });

    }
    boolean validEmail() {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            return false;
        }
        return true;
    }

    boolean validPassWord() {

        String password = emailEditText.getText().toString();
        if (!password.isEmpty() && password.length() >= 8) {
            return true;
        }
        return false;

    }
}
