package com.chikeandroid.debtmanager20.util.validator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chikeandroid.debtmanager20.R;
import com.chikeandroid.debtmanager20.util.ValidationUtil;

/**
 * Created by Chike on 4/11/2017.
 */

public class EditTextIntegerValidator extends EditTextValidator {

    private Context mContext;
    private String mAmount;

    public EditTextIntegerValidator(EditText editText, Context context) {
        super(editText);
        mContext = context;
        mAmount = editText.getText().toString();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ValidationUtil.isInValid(EditTextIntegerValidator.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean isValid() {
        if (ValidationUtil.isEmpty(this)) {
            setErrorMessage(String.format(mContext.getString(R.string.required_field), "Amount"));
            return false;
        }
        return true;
    }
}