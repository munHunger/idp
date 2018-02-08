package se.munhunger.idp.Util;

import org.apache.commons.validator.routines.EmailValidator;


public class EmailValidation {
    public static boolean isValidEmailAddress(String email) {
        boolean valid = false;
        return valid = EmailValidator.getInstance(true).isValid(email);
    }
}
