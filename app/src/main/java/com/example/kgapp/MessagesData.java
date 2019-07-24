package com.example.kgapp;

import java.util.ArrayList;

class MessagesData {
    public static ArrayList<String[]> data= new ArrayList<>();

    public static void addMessage(String[] msg){
        data.add(msg);
    }

    public static ArrayList<UserMessage> getListMessages(){
        ArrayList<UserMessage> list = new ArrayList<>();

        for(String[] aData: data){
            User user = new User(aData[0],aData[1]);
            UserMessage message = new UserMessage(aData[2],user, aData[3],aData[4]);
            list.add(message);
        }

        return list;
    }
}