package com.example.chatfirebase;

public class MenuItem {
    private String Name;
    private String Note;
    private  Long No;

    public MenuItem(String name, String note, Long no) {
        Name = name;
        Note = note;
        No = no;
    }

    public String getName() {
        return Name;
    }

    public String getNote() {
        return Note;
    }

    public Long getNo() {
        return No;
    }
}
