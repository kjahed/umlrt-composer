package ca.jahed.umlrt.composer.model.sm;

import ca.jahed.umlrt.composer.model.UMLRTElement;

public class UMLRTActionCode extends UMLRTElement {
    String code;
    String language;

    public void setCode(String code) {
        this.code = code;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }
}
