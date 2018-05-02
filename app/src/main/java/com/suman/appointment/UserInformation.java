package com.suman.appointment;

import java.io.Serializable;

/**
 * Created by Sagar on 4/11/2018.
 */

public class UserInformation implements Serializable {
    public String name;
    public String phone;



    public UserInformation() {

    }



    public UserInformation(String name, String phone) {
        this.name = name;
        this.phone = phone;

    }
}
