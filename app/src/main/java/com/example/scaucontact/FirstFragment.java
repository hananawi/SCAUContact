package com.example.scaucontact;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class FirstFragment extends Fragment {

    private LinkedList<Contact> mContactList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private ContactListAdapter mAdapter;
    private String keyWord;
    private int mode;

    private FragmentActivity fragmentActivity;
    private FragmentTransaction transaction;
    private View view;
    private SearchView searchView;

    public FirstFragment(String keyWord, int mode){
        this.keyWord = keyWord;
        this.mode = mode;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d("nmslTag", "onCreateView: FirstSegment");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("nmslTag", "onViewCreated: FirstSegment");
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        super.onViewCreated(view, savedInstanceState);

        fragmentActivity = getActivity();
        this.view = view;
        searchView = ((MainActivity)getActivity()).searchView;

        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.fragment_container, new SecondFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Log.d("nmslTag", "onViewCreated: change title");
        if(keyWord == null){
            ((TextView)view.findViewById(R.id.group_name)).setText("全部");
        }
        else {
            ((TextView)view.findViewById(R.id.group_name)).setText(keyWord);
        }
        initContactList(view);

        changeOptionsMenu(((MainActivity)getActivity()).menu);
    }

    // mode 0: groupName, mode 1: contactName, mode 2: contactPhone
    private LinkedList<Contact> chooseContact(GroupManager groupManager, String keyWord, int mode){
        Log.d("nmslTag", "chooseContact: change contact");
        LinkedList<Contact> ret = new LinkedList<>();
        if(mode == 0){
            if(keyWord == null){  // return all Contacts
                return groupManager.getAllContacts();
            }
            return groupManager.getSingleteam(keyWord).getSteam();
        }
        else if(mode == 1){
            char[] tmp = keyWord.toCharArray();
            for(Group i: groupManager.getAteam()){
                for(Contact j: i.getSteam()){
                    for(char k: tmp){
                        if(j.getName().contains(String.valueOf(k)) ||
                                j.getPhone().contains(String.valueOf(k))){
                            ret.add(j);
                            break;
                        }
                    }
                }
            }
        }
        return ret;
    }

    private void initContactList(@NonNull View view){
        mContactList.clear();

        mContactList = chooseContact(((MainActivity)fragmentActivity).groupManager, keyWord, mode);

        mRecyclerView = view.findViewById(R.id.contact_recyclerview);
        mAdapter = new ContactListAdapter(view.getContext(), mContactList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAdapter.setOnEditButtonClickListener(transaction);
        mAdapter.notifyDataSetChanged();

        if(mode != 0 && mContactList.size() == 0){
            toastPrint("找不到该联系人:(");
        }
    }

    private void changeOptionsMenu(Menu menu){
        menu.findItem(R.id.search).setIcon(ResourcesCompat
                .getDrawable(getResources(), R.drawable.ic_search_button, null));
        menu.findItem(R.id.search).setVisible(true);
        menu.findItem(R.id.export_to_csv).setVisible(true);
        menu.findItem(R.id.action_settings).setVisible(true);
        menu.findItem(R.id.confirm).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_container, new FirstFragment(query, 1));
                transaction.commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ((MainActivity)fragmentActivity).menu.findItem(R.id.search)
                .setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new Fragment());
                        transaction.commit();
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new FirstFragment(keyWord, mode));
                        transaction.commit();
                        return true;
                    }
                });
    }

    public void toastPrint(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
