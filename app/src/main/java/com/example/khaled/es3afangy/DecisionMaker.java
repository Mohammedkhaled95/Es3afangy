package com.example.khaled.es3afangy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;

public class DecisionMaker extends AppCompatActivity {

    EditText makerName;
    EditText makerId;
    EditText makerEmail;
    EditText makerPassword;
    EditText makerConfirmPassword;
    EditText makerPhone;


    Button makerSaveBtn;

    ProgressBar makerProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decision_maker);

        makerName = (EditText) findViewById(R.id.decision_maker_name);
        makerId = (EditText) findViewById(R.id.decision_maker_id);
        makerEmail = (EditText) findViewById(R.id.decision_maker_email);
        makerPassword = (EditText) findViewById(R.id.decision_maker_password);
        makerConfirmPassword = (EditText) findViewById(R.id.decision_maker_confirm_password);
        makerPhone = (EditText) findViewById(R.id.decision_maker_phone);

        
        makerProgressBar=(ProgressBar)findViewById(R.id.maker_progress_bar);



        makerSaveBtn = (Button) findViewById(R.id.decision_maker_save_button);



        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(),Defaults.APPLICATION_ID,Defaults.API_KEY);

        makerSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (makerName.getText().toString().isEmpty() || makerId.getText().toString().isEmpty() || makerEmail.getText().toString().isEmpty()
                        || makerPassword.getText().toString().isEmpty() || makerConfirmPassword.getText().toString().isEmpty()
                        || makerPhone.getText().toString().isEmpty()
                        )

                {

                    //empty input
                    Toast.makeText(DecisionMaker.this, "Invalid inputs", Toast.LENGTH_SHORT).show();
                }
                else
                    {

                    //not empty inputs

                    if(validEmail()&&validPassWord()&&isIdenticalpassword()){
                        makerProgressBar.setVisibility(View.VISIBLE);




                         saveDecisionMaker();
                        saveDecisionMakerLogin();
                    }



                }
            }
        });


    }

    public void saveDecisionMaker() {


        HashMap decisionMaker = new HashMap<>();
        decisionMaker.put("name", makerName.getText().toString());
        decisionMaker.put("email", makerEmail.getText().toString());
        decisionMaker.put("password", makerPassword.getText().toString());
        decisionMaker.put("id", makerId.getText().toString());
        decisionMaker.put("phone", makerPhone.getText().toString());


        Backendless.Data.of("DECISIONMAKER").save(decisionMaker, new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map response) {

                Toast.makeText(DecisionMaker.this, "Decision maker Saved  ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DecisionMaker.this, Admin.class);
                makerProgressBar.setVisibility(View.INVISIBLE);

                startActivity(i);
                finish();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(DecisionMaker.this, "Error Saving user Data ", Toast.LENGTH_SHORT).show();
                makerProgressBar.setVisibility(View.INVISIBLE);



            }
        });


    }

    public void saveDecisionMakerLogin() {
        int indicator=3;

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);
        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", makerEmail.getText().toString());
        user.setProperty("name", makerName.getText().toString());
        user.setProperty("phone", makerPhone.getText().toString());
        user.setProperty("indicator", indicator);
        user.setPassword(makerPassword.getText().toString());

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                // user has been registered and now can login
                Toast.makeText(DecisionMaker.this, "Successfully ,account created", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(DecisionMaker.this, Login.class));
                finish();
            }

            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(DecisionMaker.this, "error creating account", Toast.LENGTH_SHORT).show();
                makerProgressBar.setVisibility(View.INVISIBLE);
                Log.e("Registeration", "handleFault: " + fault.toString());
            }
        });






    }

    boolean validEmail() {
        if (!Patterns.EMAIL_ADDRESS.matcher(makerEmail.getText().toString()).matches()) {
            return false;
        }
        return true;
    }

    boolean validPassWord() {

        String password = makerPassword.getText().toString();
        if (!password.isEmpty() && password.length() >= 8) {
            return true;
        }
        return false;

    }
    boolean isIdenticalpassword() {
        if (makerPassword.getText().toString().equals(makerConfirmPassword.getText().toString()))

        {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
