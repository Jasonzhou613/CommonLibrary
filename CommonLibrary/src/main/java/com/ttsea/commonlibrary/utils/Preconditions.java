package com.ttsea.commonlibrary.utils;

/**
 * // to do <br>
 * <p>
 * <b>date:</b> 2017/2/18 13:21 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 * <b>last modified date:</b> 2017/2/18 13:21.
 */
final public class Preconditions {

    private Preconditions() {
    }

    /**
     * Ensures that an object reference passed as LeakUtils parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as LeakUtils parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMsg  the exception message to use if the check fails
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, String errorMsg) {
        if (reference == null) {
            throw new NullPointerException(errorMsg);
        }
        return reference;
    }
}
