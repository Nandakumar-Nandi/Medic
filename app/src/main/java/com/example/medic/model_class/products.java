package com.example.medic.model_class;

import android.content.Context;

public class    products {
    String url;
    String name;
    String mrp;
    String id;
    String manufacturer;
    String category;
    public products(String url, String name, String mrp, String id, String manufacturer,String category) {
        this.url = url;
        this.name = name;
        this.mrp = mrp;
        this.id = id;
        this.manufacturer = manufacturer;
        this.category=category;
    }


    public String getId() {
        return id;
    }

    public String getMrp() {
        return mrp;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }
}
