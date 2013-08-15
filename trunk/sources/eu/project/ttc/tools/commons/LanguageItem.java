// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.commons;

import java.util.Locale;

/**
 * This class represents a language item. It is an abstraction between
 * language code as used by the system vs. language representation as
 * understood by humans.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 15/08/13
 */
public class LanguageItem {

    private String code;

    public LanguageItem(String code) {
        this.code = code;
    }

    public String getValue() {
        return code;
    }

    @Override
    public String toString() {
        return new Locale(code).getDisplayLanguage(Locale.ENGLISH);
    }

}
