package com.example.scaucontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;


interface Communicate{
    DrawerLayout work();
}

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private GroupManager mGroupList;
    private LayoutInflater mInflater;
    private String chosenGroupName;

    private Communicate communicate;

    public GroupListAdapter(Context context, GroupManager mGroupList){
        this.mInflater = LayoutInflater.from(context);
        this.mGroupList = mGroupList;
    }

    public void setCommunicate(Communicate communicate) {
        this.communicate = communicate;
    }

    public String getChosenGroupName() {
        return chosenGroupName;
    }

    class GroupViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView groupItemView;
        GroupListAdapter mAdapter;

        public GroupViewHolder(View itemView, GroupListAdapter adapter){
            super(itemView);
            this.groupItemView = itemView.findViewById(R.id.group_item);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Group item = mGroupList.getAteam().get(getLayoutPosition());
            Toast.makeText(v.getContext(), item+" Clicked!", Toast.LENGTH_SHORT).show();
            chosenGroupName = item.getTeamname();
            communicate.work().closeDrawer(GravityCompat.START);
        }
    }

    @NonNull
    @Override
    public GroupListAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.grouplist_item, parent, false);
        return new GroupViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.GroupViewHolder holder, int position) {
        Group mCurrent = mGroupList.getAteam().get(position);
        holder.groupItemView.setText(mCurrent.getTeamname());
    }

    @Override
    public int getItemCount() {
        return this.mGroupList.getAteam().size();
    }
}
