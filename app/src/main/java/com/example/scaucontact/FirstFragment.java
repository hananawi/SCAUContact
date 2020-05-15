package com.example.scaucontact;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class FirstFragment extends Fragment {

    private LinkedList<Contact> mContactList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private ContactListAdapter mAdapter;
    private String keyWord;
    private int mode;

    private FragmentTransaction transaction;
    private View view;

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

        this.view = view;

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                NavHostFragment.findNavController(FirstFragment.this)
////                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//                ((MainActivity)getActivity()).checkWriteExternalPermission();
//                writeFile("ContactInfo.txt");
//            }
//        });
//
//        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                ((MainActivity)getActivity()).checkWriteExternalPermission();
//                readFile("ContactInfo.txt");
//            }
//        });

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
    }

//    @Override
//    public void onAttach(@NonNull Context context){
//        super.onAttach(context);
//
//    }


    // Context.openFileInput(fileName)  储存在内部存储  /data/data/<packagename>/files/
    // Context.getExternalFilesDir(fileType)  储存在内部存储(外部私有文件)
//    private boolean readFile(String fileName){
//        File file = new File(getActivity().getExternalFilesDir(null), fileName);
//        try(FileInputStream fis = new FileInputStream(file);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr)){
//            String tmp = "";
//            String line;
//            while((line = br.readLine()) != null){
//                tmp += line + "\n";
//            }
//            toastPrint(tmp + "\n" + getActivity().getExternalFilesDir(null));
////            toastPrint("功能待完善");
//            return true;
//        }
//        catch (Exception e){
//            toastPrint("file write failed:(");
//            return false;
//        }
//    }

//    private boolean writeFile(String fileName){
//        File file = new File(getActivity().getExternalFilesDir(null), fileName);
//        try(FileOutputStream fos = new FileOutputStream(file);
//            OutputStreamWriter osw = new OutputStreamWriter(fos);
//            BufferedWriter bw = new BufferedWriter(osw)){
//            bw.write("家人1号\t123456789\t家人\n");
//            toastPrint("file created successfully!!!");
//            return true;
//        }
//        catch(Exception e){
//            toastPrint("file read failed:(");
//            return false;
//        }
//    }

    // mode 0: groupName, mode 1: contactName, mode 2: contactPhone
    private LinkedList<Contact> chooseContact(LinkedList<Contact> contacts, String keyWord, int mode){
        Log.d("nmslTag", "chooseContact: change contact");
        LinkedList<Contact> ret = new LinkedList<>();
        if(mode == 0){
            if(keyWord == null){
                return (LinkedList<Contact>) contacts.clone();
            }
            for(Contact i: contacts){
                if(i.getGroupName().equals(keyWord)){
                    ret.addLast(i);
                }
            }
        }
        else if(mode == 1){
            char[] tmp = keyWord.toCharArray();
            for(Contact i: contacts){
                for(char j: tmp){
                    if(i.getName().contains(String.valueOf(j))){
                        ret.add(i);
                        break;
                    }
                }
            }
        }
        else if(mode == 2){
            for(Contact i: contacts){
                if(i.getPhone().startsWith(keyWord)){
                    ret.add(i);
                }
            }
        }
        return ret;
    }

    private void initContactList(@NonNull View view){
//        if(mContactList.size() == 0){
//            for(int i=0; i<5; i++){
//                mContactList.addLast("Contact "+i);
//            }
//        }
//        TypedArray avatar = getResources().obtainTypedArray(R.array.contact_avatar);

        LinkedList<Contact> contacts = ((MainActivity)getActivity()).mContactList;
        mContactList.clear();

//        File file = new File(getActivity().getExternalFilesDir(null), "ContactInfo.txt");
//        try(FileInputStream fis = new FileInputStream(file);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr)){
//            String line;
//            while((line = br.readLine()) != null){
//                String[] info = line.split("\\s+");
//
//                mContactList.addLast(new Contact(
//                        info[0],  // name
//                        info[1],  // phone
//                        info[2]   // groupName
//                ));
//            }
//        }
//        catch (Exception e){
//            toastPrint("init contact failed:(\n"+ e.getMessage()+"\n" + e.getLocalizedMessage());
//            System.out.println("------------------------------------------------------------------------------");
//            e.printStackTrace();
//            System.out.println("------------------------------------------------------------------------------");
//        }

        mContactList = chooseContact(contacts, keyWord, mode);

        mRecyclerView = view.findViewById(R.id.contact_recyclerview);
        mAdapter = new ContactListAdapter(view.getContext(), mContactList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        avatar.recycle();

        mAdapter.setOnEditButtonClickListener(transaction);
        mAdapter.notifyDataSetChanged();

        if(mode != 0 && mContactList.size() == 0){
            toastPrint("找不到该联系人:(");
        }

//        getActivity().onAttachFragment();
    }



    public void toastPrint(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
