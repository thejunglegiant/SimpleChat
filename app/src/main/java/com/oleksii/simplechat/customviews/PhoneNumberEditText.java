package com.oleksii.simplechat.customviews;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.oleksii.simplechat.R;

public class PhoneNumberEditText extends androidx.appcompat.widget.AppCompatEditText {

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phoneNumber = getText().toString();

            if (!validatePhoneNumber(phoneNumber)) {
                setTextColor(getResources().getColor(R.color.colorRed800));
            } else {
                setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            if (!isFormattedPhone(s.toString())) {
                String formatted = formatPhoneNumber(s.toString());
                setText(formatted);
                setSelection(formatted.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    public PhoneNumberEditText(Context context) {
        super(context);
        init();
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        addTextChangedListener(textWatcher);
        setInputType(EditorInfo.TYPE_CLASS_PHONE);
        setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        setSingleLine();
        setHint("12 345 67 89");
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        String regexPhone = "(\\d{2})\\s(\\d{3})\\s(\\d{2})\\s(\\d{2,4})";
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.matches(regexPhone);
    }

    private boolean isFormattedPhone(String rawPhone) {
        String[] separate = rawPhone.split(" ");

        if (separate.length == 1)
            return separate[0].length() <= 2;

        if (separate.length == 2)
            return separate[0].length() == 2 && separate[1].length() <= 3;

        if (separate.length == 3)
            return separate[0].length() == 2 && separate[1].length() == 3
                    && separate[2].length() <= 2;


        return true;
    }

    public static String formatPhoneNumber(String rawPhone) {
        rawPhone = rawPhone.replaceAll(" ", "");
        String phoneFormat = "";
        if (rawPhone.length() > 1) {
            phoneFormat += rawPhone.substring(0, 2);
            rawPhone = rawPhone.substring(2);
        } else {
            return rawPhone;
        }
        phoneFormat += " ";
        if (rawPhone.length() > 3) {
            phoneFormat += rawPhone.substring(0, 3);
            rawPhone = rawPhone.substring(3);
        } else {
            phoneFormat += rawPhone;
            return phoneFormat;
        }
        phoneFormat += " ";
        if (rawPhone.length() > 1) {
            phoneFormat += rawPhone.substring(0, 2);
            rawPhone = rawPhone.substring(2);
        } else {
            phoneFormat += rawPhone;
            return phoneFormat;
        }
        phoneFormat += " ";
        if (rawPhone.length() > 1) {
            phoneFormat += rawPhone.substring(0, 2);
            rawPhone = rawPhone.substring(2);
        }

        return phoneFormat + rawPhone;
    }

    public String getPhoneNumber() {
        return getText().toString().trim().replaceAll(" ", "");
    }
}