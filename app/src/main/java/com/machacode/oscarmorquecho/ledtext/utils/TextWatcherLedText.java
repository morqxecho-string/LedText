package com.machacode.oscarmorquecho.ledtext.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;

import com.google.android.material.textfield.TextInputEditText;
import com.machacode.oscarmorquecho.ledtext.R;
import com.machacode.oscarmorquecho.ledtext.models.LedText;

import static com.machacode.oscarmorquecho.ledtext.utils.Utilities.isNotEmpty;

public class TextWatcherLedText implements TextWatcher {
    //region Variables
    private LedText ledText;
    private TextInputEditText textInputEditText;
    //endregion

    //region Constructor
    @SuppressLint("ClickableViewAccessibility")
    public TextWatcherLedText(Activity activity, TextInputEditText textInputEditText, LedText ledText) {
        super();
        this.textInputEditText = textInputEditText;
        this.ledText = ledText;

        this.textInputEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP)
                try {
                    if (event.getRawX() >= (v.getRight() - ((TextInputEditText) v).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        ((TextInputEditText)v).setText(activity.getString(R.string.empty));
                        this.textInputEditText.requestFocus();
                        return true;
                    }
                } catch (Exception ex) {
                    return false;
                }
            return false;
        });
    }
    //endregion

    //region Listeners
    @Override
    public void afterTextChanged(Editable s) {
        textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        int clearIcon = (isNotEmpty(s.toString())) ? R.drawable.ic_cancel : 0;
        textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0);
        textInputEditText.setSelection(s.toString().length());
        ledText.setText(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
    //endregion
}