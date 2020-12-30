package com.example.MillenApp.NotesList;


import java.util.ArrayList;

public class DataObj {
    public String ID;
    public String Date;
    public String Type;
    public String Details;

    public DataObj(ArrayList data) {
        ID = data.get(0).toString().trim();
        Date = data.get(1).toString().trim();
        Type = data.get(2).toString().trim();
        Details = data.get(3).toString().trim();
    }

}



