package cegeka.goalfollower.ro.goalfollower;

import java.io.Serializable;
import java.util.Date;

public class Goal implements Serializable{
    public String desc;
    public String descrip;
    public Date dueDate;
    public String pass;
    public Goal(){

    }
    public Goal(String desc, String descrip, Date dueDate, String pass){
        this.desc = desc;
        this.descrip = descrip;
        this.dueDate = dueDate;
        this.pass = pass;
    }
}
