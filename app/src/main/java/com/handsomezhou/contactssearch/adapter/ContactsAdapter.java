package com.handsomezhou.contactssearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.handsomezhou.contactssearch.R;
import com.handsomezhou.contactssearch.model.Contacts;
import com.handsomezhou.contactssearch.model.Contacts.SearchByType;
import com.handsomezhou.contactssearch.util.ViewUtil;
import com.handsomezhou.contactssearch.view.QuickAlphabeticBar;

import java.util.List;


public class ContactsAdapter extends ArrayAdapter<Contacts> implements SectionIndexer {
    //public static final String PINYIN_FIRST_LETTER_DEFAULT_VALUE="#";
    private Context mContext;
    private int mTextViewResourceId;
    private List<Contacts> mContacts;
    private OnContactsAdapter mOnContactsAdapter;

    public interface OnContactsAdapter {
        //void onContactsSelectedChanged(List<Contacts> contacts);
        void onAddContactsSelected(Contacts contacts);

        void onRemoveContactsSelected(Contacts contacts);

//        void onContactsCall(Contacts contacts);

//        void onContactsSms(Contacts contacts);

        void onContactsRefreshView();

        void onContactSelected(Contacts contacts);
    }

    public ContactsAdapter(Context context, int textViewResourceId, List<Contacts> contacts) {
        super(context, textViewResourceId, contacts);
        mContext = context;
        mTextViewResourceId = textViewResourceId;//单个联系人界面
        mContacts = contacts;//搜索后的所有联系人

    }

    public OnContactsAdapter getOnContactsAdapter() {
        return mOnContactsAdapter;
    }

    public void setOnContactsAdapter(OnContactsAdapter onContactsAdapter) {
        mOnContactsAdapter = onContactsAdapter;
    }

    public void clearSelectedContacts() {
        //clear data
        for (Contacts contacts : mContacts) {
            contacts.setSelected(false);

            //other phoneNumber

            if (null != contacts.getNextContacts()) {
                Contacts currentContact = contacts.getNextContacts();
                Contacts nextContact = null;
                while (null != currentContact) {
                    currentContact.setSelected(false);
                    nextContact = currentContact;
                    currentContact = nextContact.getNextContacts();
                }
            }
        }

        //refresh view
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;//最后返回的是这个view
        ViewHolder viewHolder;
        Contacts contacts = getItem(position);
        if (null == convertView) {
            view = LayoutInflater.from(mContext).inflate(mTextViewResourceId, null);//即contacts_list_item.xml
            viewHolder = new ViewHolder();
            viewHolder.contact_info_RLayout = (RelativeLayout) view.findViewById(R.id.contacts_info);//包含姓名和电话的布局，用于点击弹出一个界面
            viewHolder.mAlphabetTv = (TextView) view.findViewById(R.id.alphabet_text_view);
            viewHolder.mContactsMultiplePhoneOperationPromptIv = (ImageView) view.findViewById(R.id.contacts_multiple_phone_operation_prompt_image_view);
            viewHolder.mSelectContactsCB = (CheckBox) view.findViewById(R.id.select_contacts_check_box);
            viewHolder.mNameTv = (TextView) view.findViewById(R.id.name_text_view);
            viewHolder.mPhoneNumber = (TextView) view.findViewById(R.id.phone_number_text_view);
            viewHolder.mOperationViewIv = (ImageView) view.findViewById(R.id.operation_view_image_view);//打开扩展项：用以显示打电话和发短信图标
            viewHolder.mDivisionLineView = view.findViewById(R.id.division_line_view);
            viewHolder.mOperationViewLayout = (RelativeLayout) view.findViewById(R.id.contacts_info);
//            viewHolder.mCallIv = (ImageView) view.findViewById(R.id.call_image_view);//打电话图标
//            viewHolder.mSmsIv = (ImageView) view.findViewById(R.id.sms_image_view);//发短信图标
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        //show the first alphabet of name
        showAlphabetIndex(viewHolder.mAlphabetTv, position, contacts);
        //show name and phone number
        switch (contacts.getSearchByType()) {
            case SearchByNull:
                ViewUtil.showTextNormal(viewHolder.mNameTv, contacts.getName());

                if (!contacts.isBelongMultipleContactsPhone()) {
                    ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                    ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber());
                } else {
                    if ( contacts.isFirstMultipleContacts()) {
                        if (contacts.getNextContacts().isHideMultipleContacts()) {
                            ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                            ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber() + mContext.getString(R.string.phone_number_count, Contacts.getMultipleNumbersContactsCount(contacts) + 1));
                        } else {
                            ViewUtil.showView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                            ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber() + "(" + mContext.getString(R.string.click_to_hide) + ")");
                        }
                    } else {
                        if (contacts.isHideMultipleContacts()) {
                            ViewUtil.invisibleView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                        } else {
                            ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                        }
                        ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber());
                    }
                }
                break;
            case SearchByPhoneNumber:
                ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                ViewUtil.showTextNormal(viewHolder.mNameTv, contacts.getName());
                ViewUtil.showTextHighlight(viewHolder.mPhoneNumber, contacts.getPhoneNumber(), contacts.getMatchKeywords().toString());
                break;
            case SearchByName:
                ViewUtil.hideView(viewHolder.mContactsMultiplePhoneOperationPromptIv);
                ViewUtil.showTextHighlight(viewHolder.mNameTv, contacts.getName(), contacts.getMatchKeywords().toString());
                ViewUtil.showTextNormal(viewHolder.mPhoneNumber, contacts.getPhoneNumber());
                break;
            default:
                break;
        }


        viewHolder.mSelectContactsCB.setTag(position);
        viewHolder.mSelectContactsCB.setChecked(contacts.isSelected());
        viewHolder.mSelectContactsCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer) buttonView.getTag();
                Contacts contacts = getItem(position);
                if ((isChecked) && (! contacts.isSelected())) {
                    contacts.setSelected(isChecked);
                    addSelectedContacts(contacts);

                } else if ((!isChecked) && (contacts.isSelected())) {
                    contacts.setSelected(isChecked);
                    removeSelectedContacts(contacts);
                } else {
                    return;
                }
            }
        });

        viewHolder.mOperationViewIv.setTag(position);
        int resid = (contacts.isHideOperationView()) ? (R.drawable.arrow_down_selector) : (R.drawable.arrow_up_selector);
        viewHolder.mOperationViewIv.setBackgroundResource(resid);//设置上下指示按钮用于是否打开 打电话 发短信 界面项

        viewHolder.contact_info_RLayout.setTag(position);//为每一个contact_info_RLayout设置其相应的位置

        if (contacts.isHideOperationView()) {
            ViewUtil.hideView(viewHolder.mDivisionLineView);
            ViewUtil.hideView(viewHolder.mOperationViewLayout);

        } else {
            ViewUtil.showView(viewHolder.mDivisionLineView);
            ViewUtil.showView(viewHolder.mOperationViewLayout);
        }

        viewHolder.contact_info_RLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =(Integer) v.getTag();
                Contacts contacts = getItem(position);//把选中的联系人信息传过去，以使那边能够将此信息用Activity展现出来（由于此类不能使用startActivity()方法）
                if (null != mOnContactsAdapter) {
                    mOnContactsAdapter.onContactSelected(contacts);
                }



            }
        });
        viewHolder.mOperationViewIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Contacts contacts = getItem(position);
                contacts.setHideOperationView(!contacts.isHideOperationView());//是否隐藏 打电话 发短信 界面项
                if (null != mOnContactsAdapter) {
                    mOnContactsAdapter.onContactsRefreshView();
                }
            }
        });

//        viewHolder.mCallIv.setTag(position);
//        viewHolder.mCallIv.setOnClickListener(new View.OnClickListener() {//实现打电话功能
//            @Override
//            public void onClick(View v) {
//                int position = (Integer) v.getTag();
//                Contacts contacts = getItem(position);
//                if (null != mOnContactsAdapter) {
//                    mOnContactsAdapter.onContactsCall(contacts);
//                }
//
//            }
//        });

//        viewHolder.mSmsIv.setTag(position);
//        viewHolder.mSmsIv.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                int position = (Integer) v.getTag();
//                Contacts contacts = getItem(position);
//                if (null != mOnContactsAdapter) {
//                    mOnContactsAdapter.onContactsSms(contacts);
//                }
//            }
//        });
        return view;
    }


    @Override
    public Object[] getSections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getPositionForSection(int section) { //SectionIndexer接口中的方法
        Contacts contacts = null;
        if (QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER == section) {
            return 0;
        } else {
            int count = getCount();
            for (int i = 0; i < count; i++) {
                contacts = getItem(i);
                char firstChar = contacts.getSortKey().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        RelativeLayout contact_info_RLayout;
        TextView mAlphabetTv;
        ImageView mContactsMultiplePhoneOperationPromptIv;
        CheckBox mSelectContactsCB;
        TextView mNameTv;
        TextView mPhoneNumber;
        ImageView mOperationViewIv;

        View mDivisionLineView;

        RelativeLayout mOperationViewLayout;
//        ImageView mCallIv;
//        ImageView mSmsIv;
    }

    private void showAlphabetIndex(TextView textView, int position, final Contacts contacts) {
        if ((null == textView) || position < 0 || (null == contacts)) {
            return;
        }
        String curAlphabet = getAlphabet(contacts.getSortKey());
        if (position > 0) {
            Contacts preContacts = getItem(position - 1);
            String preAlphabet = getAlphabet(preContacts.getSortKey());
            if (curAlphabet.equals(preAlphabet) || (SearchByType.SearchByNull != contacts.getSearchByType())) {
                textView.setVisibility(View.GONE);
                textView.setText(curAlphabet);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(curAlphabet);
            }
        } else {
            if ((SearchByType.SearchByNull == contacts.getSearchByType())) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(curAlphabet);
            } else {
                textView.setVisibility(View.GONE);
            }
        }

        return;
    }

    private String getAlphabet(String str) {
        if ((null == str) || (str.length() <= 0)) {
            return String.valueOf(QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER);
        }
        String alphabet = null;
        char chr = str.charAt(0);
        if (chr >= 'A' && chr <= 'Z') {
            alphabet = String.valueOf(chr);
        } else if (chr >= 'a' && chr <= 'z') {
            alphabet = String.valueOf((char) ('A' + chr - 'a'));
        } else {
            alphabet = String.valueOf(QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER);
        }
        return alphabet;
    }

    private boolean addSelectedContacts(Contacts contacts) {


        do {
            if (null == contacts) {
                break;
            }

            if (null != mOnContactsAdapter) {
                mOnContactsAdapter.onAddContactsSelected(contacts);
            }

            return true;
        } while (false);

        return false;

    }

    private void removeSelectedContacts(Contacts contacts) {
        if (null == contacts) {
            return;
        }

        if (null != mOnContactsAdapter) {
            mOnContactsAdapter.onRemoveContactsSelected(contacts);
        }
    }
}
