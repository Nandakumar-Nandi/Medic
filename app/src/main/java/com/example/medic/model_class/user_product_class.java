package com.example.medic.model_class;

public class user_product_class {
    String Name,stock,prescription,rate,img,id;




    public user_product_class(String name, String stock, String prescription, String rate, String img, String id) {
        Name = name;
        this.stock = stock;
        this.prescription = prescription;
        this.rate = rate;
        this.img = img;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public String getStock() {
        return stock;
    }



    public String getRate() {
        return rate;
    }

    public String getImg() {
        return img;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getId() {
        return id;
    }
}
