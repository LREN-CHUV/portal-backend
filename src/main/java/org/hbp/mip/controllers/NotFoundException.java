/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;

public class NotFoundException extends ApiException {
    private int code;

    public NotFoundException(int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
