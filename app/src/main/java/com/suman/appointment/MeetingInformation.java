package com.suman.appointment;

/**
 * Created by Sagar on 4/15/2018.
 */

public class MeetingInformation {
    public String heading;
    public String agenda;
    public String state;
    public String party;

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public MeetingInformation(){}
    public MeetingInformation(String heading, String agenda, String state, String party) {
        this.heading = heading;
        this.agenda = agenda;
        this.state = state;
        this.party = party;

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
