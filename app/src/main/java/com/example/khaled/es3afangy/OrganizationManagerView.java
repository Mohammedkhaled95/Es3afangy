package com.example.khaled.es3afangy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.util.Log.e;

public class OrganizationManagerView extends AppCompatActivity {

    TextView max;
    EditText available;
    RecyclerView currentOrgList;
    TextView currentOrgName;
    OrganizationAdapter currentOrgAdapter;

    List<OrganizationData> organizationDataList;
    Button update;
    Intent i;
    ProgressBar organizationproProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_manager_view);

        max = (TextView) findViewById(R.id.edit_organization_max_beds);
        available = (EditText) findViewById(R.id.edit_organization_available_beds);
        update = (Button) findViewById(R.id.edit_organization_update);
        currentOrgList=(RecyclerView)findViewById(R.id.current_organization_list);
        currentOrgName=(TextView)findViewById(R.id.current_organization_name);
        organizationproProgressBar=(ProgressBar)findViewById(R.id.organization_progress_bar);

        i=getIntent();


        //display the data of loggedIn Organization manager
        currentOrganization();

        //update number of avaiable beds
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (!available.getText().toString().isEmpty()){
                   if (Integer.valueOf( max.getText().toString())>Integer.valueOf( available.getText().toString())){
                       organizationproProgressBar.setVisibility(View.VISIBLE);

                       updateData();

                   }
                    else {
                       Toast.makeText(OrganizationManagerView.this, "How Come ! \n enter another correct value", Toast.LENGTH_SHORT).show();
                  available.setText("");
                   }
               }
               else {
                   Toast.makeText(OrganizationManagerView.this, "enter a correct value", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }

    void currentOrganization() {
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);

        IDataStore<Map> contactStorage = Backendless.Data.of("ORGANIZATION");
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();


         BackendlessUser currentManager = Backendless.UserService.CurrentUser();



        if (currentManager != null) {
             String currentManagerID = Backendless.UserService.CurrentUser().getEmail();



           String whereClause="managerEmail ='"+ currentManagerID+"'";
            queryBuilder.setWhereClause(whereClause);


            contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {

                @Override
                public void handleResponse(List<Map> response) {

                   e("Loggedin User",Backendless.UserService.CurrentUser().toString() );

                    OrganizationData data=new OrganizationData();
                    data.setNumber("1");
                    if (response.get(0).get("updated")==null){
                        data.setLastUpdate(response.get(0).get("created").toString());

                    }
                    else
                    {
                        data.setLastUpdate(response.get(0).get("updated").toString());

                    }                    data.setName(response.get(0).get("orgName").toString());
                    data.setAvailableBeds(response.get(0).get("availableBeds").toString());
                    max.setText(response.get(0).get("maxBeds").toString());



                    List<OrganizationData> currentOrgArrayList=new ArrayList<OrganizationData>();
                    currentOrgArrayList.add(data);


                    currentOrgAdapter=new OrganizationAdapter(currentOrgArrayList,getApplication());
                    currentOrgList.setAdapter(currentOrgAdapter);
                    currentOrgList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    currentOrgName.setText("مستشفى "+response.get(0).get("orgName").toString());
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    e("Loggedin user Error", fault.toString());
                    Toast.makeText(OrganizationManagerView.this, "some error occured", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }

    void updateData(){



        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);

        IDataStore<Map> contactStorage = Backendless.Data.of("ORGANIZATION");
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();

        String currentManagerID = Backendless.UserService.CurrentUser().getEmail();
        String whereClause="managerEmail ='"+ currentManagerID+"'";
        queryBuilder.setWhereClause(whereClause);


        contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {
           
            @Override
            public void handleResponse(List<Map> response) {
                e("EditText", available.getText().toString());
                e("EditText", response.size()+"");
                HashMap m= (HashMap) response.get(0);
                m.put("availableBeds",available.getText().toString());


                Backendless.Persistence.of("ORGANIZATION").save(m, new AsyncCallback<Map>() {
                    @Override
                    public void handleResponse(Map response) {

                        Toast.makeText(OrganizationManagerView.this, "Successfuly Updated", Toast.LENGTH_SHORT).show();
                        organizationproProgressBar.setVisibility(View.INVISIBLE);

                        OrganizationData data=new OrganizationData();
                        data.setNumber("1");
                        if (response.get("updated")==null){
                            data.setLastUpdate(response.get("created").toString());

                        }
                        else
                        {
                            data.setLastUpdate(response.get("updated").toString());

                        }
                        data.setName(response.get("orgName").toString());
                        data.setAvailableBeds(response.get("availableBeds").toString());
                        max.setText(response.get("maxBeds").toString());



                        List<OrganizationData> currentOrgArrayList=new ArrayList<OrganizationData>();
                        currentOrgArrayList.add(data);
                        currentOrgAdapter=new OrganizationAdapter(currentOrgArrayList,getApplication());
                        currentOrgList.setAdapter(currentOrgAdapter);
                        currentOrgList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        Log.e("UPDATE RESPONSE", response.toString());
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(OrganizationManagerView.this, "Error Updating", Toast.LENGTH_SHORT).show();
                        e("UPDATE Fault", fault.toString());


                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
