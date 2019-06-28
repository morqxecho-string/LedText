package com.machacode.oscarmorquecho.ledtext.interfaces;

import com.machacode.oscarmorquecho.ledtext.models.LedText;
import com.machacode.oscarmorquecho.ledtext.models.LedTextRealm;

public interface onClickActions{
    void onClick(String fontPath, int TYPE_FONT);

    void onClickConfiguration(LedTextRealm ledTextRealm);
}