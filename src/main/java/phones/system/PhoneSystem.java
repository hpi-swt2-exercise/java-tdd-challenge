package phones.system;

import phones.Phone;

/**
 * The public interface of a phone system.
 */
public interface PhoneSystem {

    /**
     * Returns the phone for the given number.
     * @param number phone number
     * @return phone
     */
    Phone getPhone(String number);
}
