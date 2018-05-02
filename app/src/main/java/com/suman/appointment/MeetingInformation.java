package com.suman.appointment;

import java.io.Serializable;

/**
 * Created by Sagar on 4/15/2018.
 */

public class MeetingInformation implements Serializable{
    public String heading;
    public String agenda;
    public String state;





    public MeetingInformation(){}
    public MeetingInformation(String heading, String agenda, String state) {
        this.heading = heading;
        this.agenda = agenda;
        this.state = state;


    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }



    public String getHeading() {

        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
