package com.example.pbl4_remote_desktopclient;

public enum SubServerController {
    REMOTE_DESKTOP(1),
    CHATTING(2),
    FILE_TRANSFER(3);

    private int control;

    SubServerController(int control) {this.control = control;}

    public int getControl() {return control;}
}
