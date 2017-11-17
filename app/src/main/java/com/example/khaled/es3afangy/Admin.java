package com.example.khaled.es3afangy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {
    Button addOrganization;
    Button addAmbulance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addOrganization = (Button) findViewById(R.id.add_organiztion);
        addAmbulance = (Button) findViewById(R.id.add_ambulance);

        addOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this, Organiztion.class));

            }
        });


        addAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this, DecisionMaker.class));

            }
        });



    }


}
