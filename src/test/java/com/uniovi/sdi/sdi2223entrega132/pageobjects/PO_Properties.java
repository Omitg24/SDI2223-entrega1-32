package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class PO_Properties {
    static final int SPANISH = 0;
    static final int ENGLISH = 1;
    static final Locale[] idioms = new Locale[]{new Locale("ES"), new Locale("EN")};
    static private String Path;
    //static Properties p = new Properties();
    public PO_Properties(String Path) //throws FileNotFoundException, IOException
    {
        PO_Properties.Path = Path;
    }

    public static int getSPANISH() {
        return SPANISH;
    }

    public static int getENGLISH() {
        return ENGLISH;
    }

    //
    // locale is de index in idioms array.
    //
    public String getString(String prop, int locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(Path, idioms[locale]);
        String value = bundle.getString(prop);
        String result;
        //result = new String(value.getBytes(StandardCharsets.ISO_8859_1),  StandardCharsets.UTF_8);
        result = new String(value.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        return result;
    }


}
