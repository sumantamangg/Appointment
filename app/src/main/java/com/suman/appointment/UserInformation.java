package com.suman.appointment;

import java.io.Serializable;

/**
 * Created by Sagar on 4/11/2018.
 */

public class UserInformation implements Serializable {
    public String name;
    public String phone;
    public String address;
    public String nationality;
    public String company;
    public String position;
    public String email;


    public UserInformation(String address, String nationality, String company, String position, String ph, String name,String email) {
        this.address = address;
        this.nationality = nationality;
        this.company = company;
        this.position = position;
        this.phone = ph;
        this.name = name;
        this.email = email;
    }

    public UserInformation() {

    }


}
