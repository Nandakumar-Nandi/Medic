package com.example.medic.model_class;

public class seller_home_product {
    String Product_id,Count,Image_URL,Price,Title;

    public seller_home_product(String product_id, String count, String image_URL, String price, String title) {
        Product_id = product_id;
        Count = count;
        Image_URL = image_URL;
        Price = price;
        Title = title;
    }

    public String getProduct_id() {
        return Product_id;
    }

    public String getCount() {
        return Count;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public String getPrice() {
        return Price;
    }

    public String getTitle() {
        return Title;
    }
}
