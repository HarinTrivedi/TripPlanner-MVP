package com.hlabexamples.commonmvp.base.mvp.validator;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by H.T. on 01/12/17.
 */

public abstract class FormValidator implements IFormValidator {

    /**
     * Validates the field
     *
     * @return true if not empty text
     */
    public boolean isEmptyField(final String text, String errorMessage) {
        boolean isEmpty = TextUtils.isEmpty(text);
        if (isEmpty && !TextUtils.isEmpty(errorMessage))
            onValidationError(errorMessage);
        return isEmpty;
    }

    /**
     * Validates the Email Id
     *
     * @return true valid email id, false invalid emailid
     */
    public boolean isValidEmailId(final String emailId, String errorMessage) {
        boolean isValid = !isEmptyField(emailId, null) && Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
        if (!isValid && !TextUtils.isEmpty(errorMessage))
            onValidationError(errorMessage);
        return isValid;
    }

    /**
     * Validates the Url
     *
     * @return true valid email id, false invalid emailid
     */
    public boolean isValidUrl(final String url, String errorMessage) {
        boolean isValid = !isEmptyField(url, null) && Patterns.WEB_URL.matcher(url).matches();
        if (!isValid && !TextUtils.isEmpty(errorMessage))
            onValidationError(errorMessage);
        return isValid;
    }

    /**
     * Validates the password
     *
     * @return true valid email id, false invalid emailid
     */
    public boolean isConfirmPasswordSame(final String password, final String confirmPassword, String errorMessage) {
        boolean isSame = password.equals(confirmPassword);
        if (!isSame && !TextUtils.isEmpty(errorMessage))
            onValidationError(errorMessage);
        return isSame;
    }

    /**
     * Validates length
     *
     * @return true if length matches condition
     */
    public boolean isValidLength(final String text, final int maxLength, String errorMessage) {
        boolean hasLength = !TextUtils.isEmpty(text) && text.length() <= maxLength;
        if (!hasLength && !TextUtils.isEmpty(errorMessage))
            onValidationError(errorMessage);
        return hasLength;
    }

}
