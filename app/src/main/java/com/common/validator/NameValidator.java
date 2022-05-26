package com.common.validator;

/**
 * @Authoer Dharmesh
 * @Date 12-03-2022
 * <p>
 * Information
 **/
public class NameValidator {

    private String name;

    public NameValidator(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameEmpty() {
        return (name == null || name.isEmpty());
    }

    public boolean isNameLengthValid() {
        return (name.length() <= 1);
    }

    public boolean isNameValid() {
        return !(name.matches("^[A-Za-z`]$"));
    }

    public boolean isSubjectEmpty() {
        return isNameEmpty();
    }

    public boolean isMessageEmpty() {
        return isNameEmpty();
    }
}
