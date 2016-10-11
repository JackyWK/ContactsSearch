package com.handsomezhou.contactssearch.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.handsomezhou.contactssearch.R;
import com.handsomezhou.contactssearch.model.Contacts;

/**
 * Created by lenovo on 2016/10/10.
 */
public class AddContactActivity extends Activity {
    EditText contactNameEText;
    EditText contactBDnumberEText;
    EditText contactPHnumberEText;
    Button confirmBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        contactNameEText = (EditText) findViewById(R.id.contact_name);
        contactBDnumberEText = (EditText) findViewById(R.id.contact_bdnumber);
        contactPHnumberEText = (EditText) findViewById(R.id.contact_pbnumber);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        insertContactDataToSQL();
    }

    private void insertContactDataToSQL() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = contactNameEText.getText().toString();
                String contactBDnumber = contactBDnumberEText.getText().toString();
                String contactPHnumber = contactPHnumberEText.getText().toString();
                do {
                    if (contactName.trim().length() == 0) {
                        Toast.makeText(AddContactActivity.this, "请输入姓名!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (contactBDnumber.trim().length() == 0
                            || (contactBDnumber.trim().length() != 6 && contactBDnumber.trim().length() != 7)
                            || Integer.parseInt(contactBDnumber) > 2097151) {
                        Toast.makeText(AddContactActivity.this, "北斗号码非法，必须是6或7位数字，且小于2097151!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //执行其它必须在一切成功后进行的操作
                    if (!contactPHnumber.equals("") && contactPHnumber.trim().length() != 11) {
                        Toast.makeText(AddContactActivity.this, "手机号码必须为11位数字！", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    ContentResolver contentResolver = getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(Contacts.Contact.contactName, contactName);
                    values.put(Contacts.Contact.contactBdnumber, contactBDnumber);
                    values.put(Contacts.Contact.contactPbnumber, contactPHnumber);

                    contentResolver.insert(Contacts.Contact.CONTACT_CONTENT_URI, values);
                    Toast.makeText(AddContactActivity.this, "添加联系人成功！", Toast.LENGTH_LONG).show();
                    finish();
                } while (false);


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
