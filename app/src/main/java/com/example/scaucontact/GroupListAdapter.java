package com.example.scaucontact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


interface Communicate{
    DrawerLayout work();
}

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private GroupManager mGroupManager;
    private LayoutInflater mInflater;
    private String chosenGroupName;
    private Context mContext;

    private Communicate communicate;

    public GroupListAdapter(Context context, GroupManager mGroupManager){
        this.mInflater = LayoutInflater.from(context);
        this.mGroupManager = mGroupManager;
        this.mContext = context;
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
        String groupName;

        public GroupViewHolder(View itemView, GroupListAdapter adapter){
            super(itemView);
            this.groupItemView = itemView.findViewById(R.id.group_item);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.delete_group_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("确认删除吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    groupName = groupItemView.getText().toString();
                                    deleteGroup();
                                    ((MainActivity)mContext).init();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
            });
        }

        private void deleteGroup(){
            File file = new File(mContext.getExternalFilesDir(null), "ContactInfo.txt");
            try(FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw)){
                for(Group i: mGroupManager.getAteam()){
                    if(i.getTeamname().equals(groupName)) continue;
                    bw.write(i.getTeamname()+"\t"+i.getSteam().size());
                    bw.newLine();
                    String line;
                    for(Contact j: i.getSteam()){
                        line = j.getName()+"\t";
                        line += j.getPhone()+"\t";
                        line += j.getEmail()+"\t";
                        line += j.getWorkUnit()+"\t";
                        line += j.getAddress()+"\t";
                        line += j.getZipCode()+"\t";
                        line += j.getRemarks()+"\t";
                        // birthday
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            Group item = mGroupManager.getAteam().get(getLayoutPosition());
            Toast.makeText(v.getContext(), item.getTeamname(), Toast.LENGTH_SHORT).show();
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
        Group mCurrent = mGroupManager.getAteam().get(position);
        holder.groupItemView.setText(mCurrent.getTeamname());
    }

    @Override
    public int getItemCount() {
        return this.mGroupManager.getAteam().size();
    }
}
