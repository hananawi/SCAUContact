package com.example.scaucontact;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

interface GetContactInfo{
    Contact work();
}

public class SecondFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    private EditText name;
    private EditText phone;
    private GetContactInfo getContactInfo;
    private FragmentTransaction transaction;
    private LinkedList<Contact> mContactList;
    private Contact contact;
    private Spinner spinner;
    private String groupLabel;
    private EditText newGroupName;
    private GroupManager groupManager;

    public void setGetContactInfo(GetContactInfo getContactInfo){
        this.getContactInfo = getContactInfo;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d("nmslTag", "onCreateView: SecondFragment");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("nmslTag", "onViewCreated: SecondFragment");
        getActivity().findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        super.onViewCreated(view, savedInstanceState);

        groupManager = ((MainActivity)getActivity()).groupManager;

        newGroupName = view.findViewById(R.id.new_group_name);
        newGroupName.setVisibility(View.INVISIBLE);

        spinner = view.findViewById(R.id.group_spinner);
        spinner.setOnItemSelectedListener(this);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.group_spinner, android.R.layout.simple_spinner_item);
        ArrayList<CharSequence> arrayList = new ArrayList<>();
        for(Group i: groupManager.getAteam()){
            arrayList.add(i.getTeamname());
        }
        arrayList.add("新增");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        name = view.findViewById(R.id.edit_name);
        phone = view.findViewById(R.id.edit_phone);

        if(this.getContactInfo != null){
            contact = getContactInfo.work();
            name.setText(contact.getName());
            phone.setText(contact.getPhone());
        }
        if(contact == null){
            view.findViewById(R.id.delete_confirm).setVisibility(View.INVISIBLE);
        }

        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        view.findViewById(R.id.save_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//                Snackbar.make(view, "保存功能待完成", Snackbar.LENGTH_LONG).setAction("nmsl", null).show();

//                readExternalFile("nmsl.txt");

                String tmp = newGroupName.getText().toString();
                if(!tmp.equals("")){  // 新建了组别
                    groupLabel = tmp;
                    groupManager.addSingleteam(new Group(groupLabel));
                }
                if(contact != null){  // 编辑现有的联系人
                    contact.setName(name.getText().toString());
                    contact.setPhone(phone.getText().toString());
                    if(!tmp.equals("")){  // 新建了组别
                        contact.setGroupName(groupLabel);
                    }
                }
//                contact.setGroupName();

                boolean flag =  writeExternalFile("ContactInfo.txt");
                if(flag){
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null && imm.isActive()){
//                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  打开软件盘
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    toastPrint("联系人创建成功");
//                    NavHostFragment.findNavController(SecondFragment.this)
//                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    ((MainActivity)getActivity()).init();
//                    transaction.replace(R.id.fragment_container, new FirstFragment(null));
//                    transaction.addToBackStack(null);
//                    transaction.commit();
                }
            }
        });

        view.findViewById(R.id.delete_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setGroupName("delete!@#$%");
                writeExternalFile("ContactInfo.txt");
                ((MainActivity)getActivity()).init();
//                transaction.replace(R.id.fragment_container, new FirstFragment(null));
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
    }


//    私有文件：存储在特定于应用的目录中的文件（使用 Context.getExternalFilesDir() 来访问）。这些文件在用户卸载您的应用时会被清除。
//    尽管这些文件在技术上可被用户和其他应用访问（因为它们存储在外部存储上），但它们不能为应用之外的用户提供价值。
//    可以使用此目录来存储您不想与其他应用共享的文件。
    private boolean readExternalFile(String FileName){
//        toastPrint(((MainActivity)getActivity()).getExternalFilesDir(null).toString());
        File file = new File(getActivity().getExternalFilesDir(null), FileName);
        try(FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr)){
            String line;
            while((line = br.readLine()) != null){
                String[] info = line.split("\\s+");
            }
        }
        catch (Exception e){
            toastPrint("Read File Failed:(");
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean writeExternalFile(String FileName){
        mContactList = ((MainActivity)getActivity()).mContactList;
        File file = new File(getActivity().getExternalFilesDir(null), FileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try(FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw)){
            String line;

            for(Contact i: mContactList){
                if(i.getGroupName().equals("delete!@#$%")) continue;
                line = i.getName() + "\t";
                line += i.getPhone() + "\t";
                line += i.getGroupName() + "\t";
                line += i.getAddress() + "\t";
                line += i.getRemarks() + "\t";
                bw.write(line);
                bw.newLine();
            }
            if(getContactInfo != null){  // 修改已有联系人，而不是新增
                return true;
            }
//            Field[] fields = contact.getClass().getFields();
            Contact contact = getContactInfo();
            line = contact.getName() + "\t";
            line += contact.getPhone() + "\t";
            line += groupLabel + "\t";
            bw.write(line);
            bw.newLine();
            return true;
        }
        catch (Exception e){
            toastPrint("Write File Failed:(");
            return false;
        }
    }

    private Contact getContactInfo(){
//        TypedArray avatarResources = getResources().obtainTypedArray(R.array.contact_avatar);

        return new Contact(this.name.getText().toString(),
                            this.phone.getText().toString(), 0);
    }

    public void toastPrint(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        groupLabel = parent.getItemAtPosition(position).toString();
        if(groupLabel.equals("新增")){
            newGroupName.setVisibility(View.VISIBLE);
        }
        else if(contact != null){
            contact.setGroupName(groupLabel);
        }
//        toastPrint(groupLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
