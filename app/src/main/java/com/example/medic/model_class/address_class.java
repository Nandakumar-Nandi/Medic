package com.example.medic.model_class;

public class address_class {
    String Name,mobile,flat_no,address;

    public address_class(String name, String mobile, String flat_no, String address) {
        Name = name;
        this.mobile = mobile;
        this.flat_no = flat_no;
        this.address = address;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public String getAddress() {
        return address;
    }
}
