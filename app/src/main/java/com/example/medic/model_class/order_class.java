package com.example.medic.model_class;

public class order_class {
    String product1;
    String product2;
    String total;
    String status;
    String order_id;
    String date;
    String image_url;

    public order_class(String product1, String product2, String total, String status, String order_id, String date, String image_url) {
        this.product1 = product1;
        this.product2 = product2;
        this.total = total;
        this.status = status;
        this.order_id = order_id;
        this.date = date;
        this.image_url = image_url;
    }


    public String getProduct1() {
        return product1;
    }

    public String getProduct2() {
        return product2;
    }

    public String getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getDate() {
        return date;
    }

    public String getImage_url() {
        return image_url;
    }
}
