package com.example.medic.model_class;

public class category_class {
    String Imgurl,Title;

    public String getImgurl() {
        return Imgurl;
    }

    public String getTitle() {
        return Title;
    }

    public category_class(String imgurl, String title) {
        Imgurl = imgurl;
        Title = title;
    }
}
