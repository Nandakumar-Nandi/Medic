package com.example.medic.model_class;

import java.io.Serializable;

public class verification_list_class implements Serializable {
    String product_id,user_id,username,date,count;

    public verification_list_class(String product_id, String user_id, String username, String date, String count) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.username = username;
        this.date = date;
        this.count = count;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getCount() {
        return count;
    }
}
