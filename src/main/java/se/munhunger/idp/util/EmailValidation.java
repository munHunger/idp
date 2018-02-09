package se.munhunger.idp.util;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidation {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance(true).isValid(email);
    }
}
