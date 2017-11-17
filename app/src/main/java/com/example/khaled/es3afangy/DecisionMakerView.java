package com.example.khaled.es3afangy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecisionMakerView extends AppCompatActivity {

    RecyclerView organizationList;
    OrganizationAdapter organizationAdapter;
    int x;
    String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_maker_view);
        organizationList = (RecyclerView) findViewById(R.id.recyclerview);

        getData();


    }


    void getData() {
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);


        IDataStore<Map> contactStorage = Backendless.Data.of("ORGANIZATION");
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();


        /*// set where clause
                queryBuilder.setWhereClause( "age > 30" );

        // set related columns
                queryBuilder.setRelated( "address", "preferences" );

        // request sorting
                queryBuilder.setSortBy( "name" );

        // set offset and page size
                queryBuilder.setPageSize( 20 );
                queryBuilder.setOffset( 40 );*/

        contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> records) {

                x = records.size();
                s = records.get(3).get("managerEmail").toString();
                Log.i("MYAPP", "Retrieved " + records.size() + " objects");

                List<OrganizationData> organizationDataList = new ArrayList<OrganizationData>();
                for (int i = 0; i < records.size(); i++) {
                    OrganizationData data = new OrganizationData();

                    data.setNumber(i + 1 + "");
                    if (records.get(0).get("updated")==null){
                        data.setLastUpdate(records.get(0).get("created").toString());

                    }
                    else
                    {
                        data.setLastUpdate(records.get(0).get("updated").toString());

                    }

                    data.setLastUpdate(records.get(i).get("created").toString());

                    data.setAvailableBeds(records.get(i).get("availableBeds").toString());
                    data.setName(records.get(i).get("orgName").toString());


                    organizationDataList.add(data);


                }

                organizationAdapter = new OrganizationAdapter(organizationDataList, getApplication());
                organizationList.setAdapter(organizationAdapter);
                organizationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("MYAPP", "Server reported an error " + fault.getMessage());

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
