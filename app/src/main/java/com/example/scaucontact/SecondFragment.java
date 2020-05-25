package com.example.scaucontact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;

interface GetContactInfo{
    Contact work();  // contact
    String work2();  // groupName
    int work3();  // mode
}

public class SecondFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText workUnit;  // :)
    private EditText address;
    private EditText zipCode;
    private EditText remarks;
    private TextView groupName;

    private GetContactInfo getContactInfo;
    private Contact contact;
//    private Spinner spinner;
    private GroupManager groupManager;
    private Button chooseGroup;
    private ArrayList<Integer> selectedItems;
    private boolean[] checkedItems;
    private View view;

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

        changeOptionsMenu(((MainActivity)getActivity()).menu);

        this.view = view;
        getHandles(view);  // 在视图中找到相应的控件

        groupManager = ((MainActivity)getActivity()).groupManager;

// spinner view
//        newGroupName = view.findViewById(R.id.new_group_name);
//        newGroupName.setVisibility(View.INVISIBLE);

//        spinner = view.findViewById(R.id.group_spinner);
//        spinner.setOnItemSelectedListener(this);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.group_spinner, android.R.layout.simple_spinner_item);
//        ArrayList<CharSequence> arrayList = new ArrayList<>();
//        for(Group i: groupManager.getAteam()){
//            arrayList.add(i.getTeamname());
//        }
//        arrayList.add("新增");
//        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_item, arrayList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        createAlertDialog();  // 设置对话窗口，用于选择分组

        if(this.getContactInfo != null){  // 编辑已有的联系人
            contact = getContactInfo.work();
            name.setText(contact.getName());
            phone.setText(contact.getPhone());
            if(!contact.getEmail().equals("null")){
                email.setText(contact.getEmail());
            }
            if(!contact.getWorkUnit().equals("null")){
                workUnit.setText(contact.getWorkUnit());
            }
            if(!contact.getAddress().equals("null")){
                address.setText(contact.getAddress());
            }
            if(!contact.getZipCode().equals("null")){
                zipCode.setText(contact.getZipCode());
            }
            if(!contact.getRemarks().equals("null")){
                remarks.setText(contact.getRemarks());
            }
            String tmp = "";
            for(String i: contact.getGroups()){
                tmp += i+",";
            }
            groupName.setText(tmp);
            for(int i = 0; i < groupManager.getAllGroupName().length; i++){
                for(String j: contact.getGroups()){
                    if(j.equals(groupManager.getAllGroupName()[i])){
                        selectedItems.add(i);
                        checkedItems[i] = true;
                        break;
                    }
                }
            }
        }

//        view.findViewById(R.id.save_confirm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                NavHostFragment.findNavController(SecondFragment.this)
////                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
////                Snackbar.make(view, "保存功能待完成", Snackbar.LENGTH_LONG).setAction("nmsl", null).show();
//
////                readExternalFile("nmsl.txt");
//
////                if(contact == null){  // 新增的联系人
////                    contact = getContactInfo(0);
////                }
////                else{  // 编辑已有的联系人
////                    contact = getContactInfo(1);
////                }
////                for(int i: selectedItem){  // 把联系人添加进选中的组别中
////                    Group tmp = groupManager.getAteam().get(i);
////                    if(!tmp.getSteam().contains(contact)){
////                        tmp.addPerson(contact);
////                    }
////                }
////
////                boolean flag =  writeExternalFile("ContactInfo.txt");
////                if(flag){
////                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                    if(imm != null && imm.isActive()){
//////                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  // 打开软件盘
////                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
////                    }
////                    toastPrint("联系人创建成功");
//////                    NavHostFragment.findNavController(SecondFragment.this)
//////                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
////                    ((MainActivity)getActivity()).init();
//////                    transaction.replace(R.id.fragment_container, new FirstFragment(null));
//////                    transaction.addToBackStack(null);
//////                    transaction.commit();
////                }
//            }
//        });
    }


//    私有文件：存储在特定于应用的目录中的文件（使用 Context.getExternalFilesDir() 来访问）。这些文件在用户卸载您的应用时会被清除。
//    尽管这些文件在技术上可被用户和其他应用访问（因为它们存储在外部存储上），但它们不能为应用之外的用户提供价值。
//    可以使用此目录来存储您不想与其他应用共享的文件。
// Context.openFileInput(fileName)  储存在内部存储  /data/data/<packagename>/files/
// Context.getExternalFilesDir(fileType)  储存在内部存储(外部私有文件)
    private boolean writeExternalFile(String FileName){
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

            for(Group i: groupManager.getAteam()){
                boolean flag = true;
                for(Contact j: i.getSteam()){
                    if(j.getDelete()){
                        flag = false;
                        break;
                    }
                }
                if(flag) bw.write(i.getTeamname()+"\t"+(i.getSteam().size()));
                else bw.write(i.getTeamname()+"\t"+(i.getSteam().size()-1));
                bw.newLine();
                for(Contact j: i.getSteam()){
                    if(j.getDelete()) continue;
                    line = j.getName()+"\t";
                    line += j.getPhone()+"\t";
                    line += j.getEmail()+"\t";
                    line += j.getWorkUnit()+"\t";
                    line += j.getAddress()+"\t";
                    line += j.getZipCode()+"\t";
                    line += j.getRemarks()+"\t";
                    for(String k: j.getGroups()){
                        line += k+"\t";
                    }
                    // birthday
                    bw.write(line);
                    bw.newLine();
                }
            }
            return true;
        }
        catch (Exception e){
            toastPrint("Write File Failed:(");
            e.printStackTrace();
            return false;
        }
    }

    private Contact getContactInfo(int mode){
//        TypedArray avatarResources = getResources().obtainTypedArray(R.array.contact_avatar);

        String mName = name.getText().toString();
        String mPhone = phone.getText().toString();
        String mEmail = email.getText().toString();
        String mWorkUnit = workUnit.getText().toString();
        String mAddress = address.getText().toString();
        String mZipCode = zipCode.getText().toString();
        String mRemarks = remarks.getText().toString();
        LinkedList<String> groups = new LinkedList<>();
        for(int i: selectedItems){
            groups.add(groupManager.getAllGroupName()[i]);
        }
        if(mode == 0){  // 新建联系人
            return new Contact(mName, mPhone, mEmail, mWorkUnit, mAddress, mZipCode, mRemarks, groups);
        }
        else{  // 编辑现有联系人
            contact.setName(mName);
            contact.setPhone(mPhone);
            contact.setEmail(mEmail);
            contact.setWorkUnit(mWorkUnit);
            contact.setAddress(mAddress);
            contact.setZipCode(mZipCode);
            contact.setRemarks(mRemarks);
            contact.setGroups(groups);
            return contact;
        }
    }

    private void getHandles(View view){
        name = view.findViewById(R.id.edit_name);
        phone = view.findViewById(R.id.edit_phone);
        email = view.findViewById(R.id.edit_email);
        workUnit = view.findViewById(R.id.edit_workunit);
        address = view.findViewById(R.id.edit_address);
        zipCode = view.findViewById(R.id.edit_zipcode);
        remarks = view.findViewById(R.id.edit_remark);
        chooseGroup = view.findViewById(R.id.choose_group);
        groupName = view.findViewById(R.id.group_name);
    }

    private void createAlertDialog(){
        selectedItems = new ArrayList<>();
        checkedItems = new boolean[groupManager.getAteam().size()+1];
        chooseGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择分组")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String tmp = "";
                                for(int i: selectedItems){
                                    tmp += groupManager.getAllGroupName()[i]+", ";
                                }
                                groupName.setText(tmp);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("新建分组", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText editText = new EditText(getContext());
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("创建分组")
                                        .setView(editText)
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                groupManager.addSingleteam(new Group(editText.getText().toString()));
                                                ((MainActivity)getActivity()).initDrawerMenu();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create().show();
                            }
                        })
                        .setMultiChoiceItems(groupManager.getAllGroupName(), checkedItems,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if(isChecked){
                                            selectedItems.add(which);
                                            checkedItems[which] = true;
                                        }
                                        else if(selectedItems.contains(which)){
                                            selectedItems.remove(Integer.valueOf(which));
                                            checkedItems[which] = false;
                                        }
                                    }
                                })
                        .create().show();
            }
        });
    }

    public void toastPrint(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void changeOptionsMenu(Menu menu){
        Drawable drawable = ResourcesCompat
                .getDrawable(getResources(), R.drawable.ic_confirm_button, null);
        MenuItem confirmButton = menu.findItem(R.id.confirm);
        MenuItem deleteButton = menu.findItem(R.id.delete);

        confirmButton.setIcon(drawable);
        confirmButton.setVisible(true);
        if(getContactInfo != null){
            deleteButton.setVisible(true);
        }
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.export_to_csv).setVisible(false);

        confirmButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(contact == null){  // 新增的联系人
                    contact = getContactInfo(0);
                }
                else{  // 编辑已有的联系人
                    contact = getContactInfo(1);
                }

                for(int i: selectedItems){  // 把联系人添加进选中的组别中
                    Group group = groupManager.getAteam().get(i);
                    if(!group.getSteam().contains(contact)){
                        group.addPerson(contact);
                    }
                }

                boolean flag =  writeExternalFile("ContactInfo.txt");
                if(flag){
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null && imm.isActive()){
//                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  // 打开软件盘
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    if(getContactInfo == null) toastPrint("联系人创建成功");
                    else toastPrint("联系人修改成功");
//                    NavHostFragment.findNavController(SecondFragment.this)
//                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    ((MainActivity)getActivity()).init();
                }
                return true;
            }
        });

        deleteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("确认删除吗？")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(Group i: groupManager.getAteam()){
                                            if((getContactInfo.work3() == 0 && getContactInfo.work2() == null) ||
                                                    getContactInfo.work2().equals(i.getTeamname())){
                                                for(Contact j: i.getSteam()){
                                                    if(j.equals(contact)){
                                                        j.setDelete(true);
                                                    }
                                                }
                                            }
                                        }
                                        writeExternalFile("ContactInfo.txt");
                                        ((MainActivity)getActivity()).init();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        return true;
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        groupLabel = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
