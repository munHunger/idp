package se.munhunger.idp.Util;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidation {
    public static boolean isValidEmailAddress(String email) {
        boolean valid = false;
        try {
            valid = EmailValidator.getInstance(true).isValid(email);
        } catch (Exception e){
            e.printStackTrace();
    }
        return valid;
    }
}
