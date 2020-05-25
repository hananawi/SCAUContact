package com.example.scaucontact;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;

import java.util.LinkedList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private LinkedList<Contact> mContactList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String groupName;
    private int mode;

    private FragmentTransaction transaction;


    public ContactListAdapter(Context context, LinkedList<Contact> mContactList, int mode, String groupName){
        this.mContactList = mContactList;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.groupName = groupName;
        this.mode = mode;
    }

    public void setOnEditButtonClickListener(FragmentTransaction transaction){
        Log.d("nmslTag", "setOnEditButtonClickListener: Navigation");
        this.transaction = transaction;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView name;
        private TextView phone;
        private TextView editButton;
        private ImageView avatar;
//        private ContactListAdapter mAdapter;

        public ContactViewHolder(View itemView, ContactListAdapter mAdapter){
            super(itemView);
//            this.contactItemView = itemView.findViewById(R.id.contact_item);
            this.name = itemView.findViewById(R.id.name);
            this.phone = itemView.findViewById(R.id.phone);
            this.editButton = itemView.findViewById(R.id.edit_button);
            this.avatar = itemView.findViewById(R.id.contact_avatar);
//            this.mAdapter = mAdapter;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Contact item = mContactList.get(getLayoutPosition());
                    Toast.makeText(v.getContext(), item+" Clicked!", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.findViewById(R.id.edit_button).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("nmslTag", "onViewCreated: work");
            SecondFragment secondFragment = new SecondFragment();
            secondFragment.setGetContactInfo(new GetContactInfo() {
                @Override
                public Contact work() {
                    return mContactList.get(getLayoutPosition());
                }
                @Override
                public String work2(){
                    return groupName;
                }
                @Override
                public int work3(){
                    return mode;
                }
            });
            transaction.replace(R.id.fragment_container, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @NonNull
    @Override
    public ContactListAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.contactlist_item, parent, false);
        return new ContactViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ContactViewHolder holder, int position) {
        final Contact mCurrent = mContactList.get(position);
        holder.name.setText(mCurrent.getName());
        holder.phone.setText(mCurrent.getPhone());
        holder.editButton.setText("修改");

        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(mCurrent.getName().charAt(0)), Color.YELLOW);
        holder.avatar.setImageDrawable(drawable);
//        Glide.with(mContext).load(mCurrent.getAvatarSource()).into(holder.avatar);

//        holder.editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editButtonListener.onEditButtonClick();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}
