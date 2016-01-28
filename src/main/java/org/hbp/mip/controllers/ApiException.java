/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;

public class ApiException extends Exception {
    private int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
