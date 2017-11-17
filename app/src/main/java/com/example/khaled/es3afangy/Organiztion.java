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

public class Organiztion extends AppCompatActivity {

    Map saveddata;
    EditText organiztionName;
    EditText organiztionAddress;
    EditText organiztionContact;
    EditText organiztionMaxBeds;
    EditText organiztionAvailableBeds;
    EditText managerName;
    EditText managerId;
    EditText managerPhone;
    EditText managerEmail;
    EditText managerPassword;
    EditText managerConfirmPassword;

    ProgressBar organizationproProgressBar;


    Button save;

    String org_name;
    String org_address;
    String org_phone;
    String org_max;
    String org_available;
    String mnger_name;
    String mnger_id;
    String mnger_email;
    String mnger_phone;
    String mnger_password;
    String mnger_confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiztion);

        organiztionAddress = (EditText) findViewById(R.id.organiztion_address);
        organiztionName = (EditText) findViewById(R.id.organiztion_name);
        organiztionContact = (EditText) findViewById(R.id.organiztion_contact);
        organiztionMaxBeds = (EditText) findViewById(R.id.organiztion_maximum_beds);
        organiztionAvailableBeds = (EditText) findViewById(R.id.organiztion_available_beds);
        managerName = (EditText) findViewById(R.id.manager_name);
        managerId = (EditText) findViewById(R.id.manager_id);
        managerPhone = (EditText) findViewById(R.id.manager_phone);
        managerEmail = (EditText) findViewById(R.id.manager_email);
        managerPassword = (EditText) findViewById(R.id.manager_password);
        managerConfirmPassword = (EditText) findViewById(R.id.manager_confirm_password);

        organizationproProgressBar=(ProgressBar)findViewById(R.id.organization_progress_bar);



        save = (Button) findViewById(R.id.save_Organization);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (organiztionAddress.getText().toString().isEmpty() || organiztionAvailableBeds.getText().toString().isEmpty() ||
                        organiztionMaxBeds.getText().toString().isEmpty() || organiztionContact.getText().toString().isEmpty()
                        || organiztionName.getText().toString().isEmpty()
                        || managerName.getText().toString().isEmpty()
                        || managerPhone.getText().toString().isEmpty()
                        || managerEmail.getText().toString().isEmpty()
                        || managerId.getText().toString().isEmpty()
                        || managerPassword.getText().toString().isEmpty()
                        || managerConfirmPassword.getText().toString().isEmpty()
                        ) {
                    //empty input
                    Toast.makeText(Organiztion.this, "Invalid Values", Toast.LENGTH_SHORT).show();

                } else {
                    //non empty inputs

                    org_address = organiztionAddress.getText().toString();
                    org_name = organiztionName.getText().toString();
                    org_phone = organiztionContact.getText().toString();
                    org_max = organiztionMaxBeds.getText().toString();
                    org_available = organiztionAvailableBeds.getText().toString();
                    mnger_name = managerName.getText().toString();
                    mnger_email = managerEmail.getText().toString();
                    mnger_password = managerPassword.getText().toString();
                    mnger_confirmPassword = managerConfirmPassword.getText().toString();
                    mnger_id = managerId.getText().toString();
                    mnger_phone = managerPhone.getText().toString();


                    if (validEmail() && validPassWord() && isIdenticalpassword()) {
                        organizationproProgressBar.setVisibility(View.VISIBLE);



                        saveOrganiztionData();
                        saveOrganiztionManager();
                    }


                }


            }
        });
    }

    void saveOrganiztionData() {

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                Defaults.APPLICATION_ID,
                Defaults.API_KEY);


        HashMap organiztiondata = new HashMap<>();
        organiztiondata.put("orgAddress", org_address);
        organiztiondata.put("availableBeds", org_available);
        organiztiondata.put("maxBeds", org_max);
        organiztiondata.put("orgName", org_name);
        organiztiondata.put("orgPhone", org_phone);
        organiztiondata.put("managerName", mnger_name);
        organiztiondata.put("managerID", mnger_id);
        organiztiondata.put("managerPhone", mnger_phone);
        organiztiondata.put("managerEmail", mnger_email);
        organiztiondata.put("managerPassword", mnger_password);


        Backendless.Persistence.of("ORGANIZATION").save(organiztiondata, new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map response) {

                Toast.makeText(Organiztion.this, "Organization Data Saved Succefully ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Organiztion.this, Admin.class);
                startActivity(i);
                organizationproProgressBar.setVisibility(View.INVISIBLE);
                saveddata=response;
                finish();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                organizationproProgressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(Organiztion.this, "Error Saving organization Data", Toast.LENGTH_SHORT).show();



            }
        });

    }

    public void saveOrganiztionManager() {

        int indicator=2;

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID,Defaults.API_KEY);


        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", managerEmail.getText().toString());
        user.setProperty("name", managerName.getText().toString());
        user.setProperty("phone", managerPhone.getText().toString());
        user.setProperty("indicator", indicator);
        user.setPassword(managerPassword.getText().toString());

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                // user has been registered and now can login
                Toast.makeText(Organiztion.this, "Manager account created", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Organiztion.this, Login.class));
                finish();
            }

            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(Organiztion.this, "error creating Manager Account", Toast.LENGTH_SHORT).show();
                organizationproProgressBar.setVisibility(View.INVISIBLE);
                Log.e("Registeration", "handleFault: " + fault.toString());
            }
        });

    }

    boolean validEmail() {
        if (!Patterns.EMAIL_ADDRESS.matcher(managerEmail.getText().toString()).matches()) {
            return false;
        }
        return true;
    }

    boolean validPassWord() {

        String password = managerPassword.getText().toString();
        if (!password.isEmpty() && password.length() >= 8) {
            return true;
        }
        return false;

    }

    boolean isIdenticalpassword() {
        if (managerPassword.getText().toString().equals(managerConfirmPassword.getText().toString()))

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
