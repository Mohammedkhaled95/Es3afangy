package com.example.khaled.es3afangy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkhaled on 01/10/17.
 */

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.myViewHolder> {

    private LayoutInflater inflater;
    List<OrganizationData> organizationDataList =new ArrayList<OrganizationData>();// Collections.emptyList();
    Context con;


    public OrganizationAdapter(List<OrganizationData> organizationDataList, Context con) {
        this.inflater=LayoutInflater.from(con);
        this.organizationDataList = organizationDataList;
        this.con = con;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.organization_item, parent, false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        OrganizationData eventData = organizationDataList.get(position);
        holder.listNumbering.setText(eventData.getNumber());
        holder.listOrganizationName.setText(eventData.getName());
        holder.listAvailableBeds.setText(eventData.getAvailableBeds());
        holder.listLastUpdate.setText(eventData.getLastUpdate());

    }

    @Override
    public int getItemCount() {
        return organizationDataList.size(); // organizationDataList.size()
    }



    //viewholder class

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listNumbering;
        TextView listOrganizationName;
        TextView listAvailableBeds;
        TextView listLastUpdate;


        public myViewHolder(View itemView) {
            super(itemView);
            listNumbering = (TextView) itemView.findViewById(R.id.list_numbering);
            listOrganizationName = (TextView) itemView.findViewById(R.id.lis_organization_name);
            listAvailableBeds = (TextView) itemView.findViewById(R.id.list_available_beds);
            listLastUpdate = (TextView) itemView.findViewById(R.id.list_last_update);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
          /*  Intent i = new Intent(view.getContext(), //displayActivity);
            view.getContext().startActivity(i);*/

        }
    }
}
