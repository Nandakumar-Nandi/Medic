package com.example.medic.model_class;

public class rating_class {
    String Rating;
    String comment;
    String user;
    String like;
    String dislike;
    String Id;
    public String getLike() {
        if(like==null || like.equals("")){
            like="0";
        }
        return like;
    }

    public String getDislike() {
        if(dislike==null || like.equals("")){
            dislike="0";
        }
        return dislike;
    }

    public String getRating() {
        return Rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public rating_class(String id,String rating, String comment, String user,String like,String dislike) {
        this.Id=id;
        Rating = rating;
        this.comment = comment;
        this.user = user;
        this.like=like;
        this.dislike=dislike;
    }

    public String getId() {
        return Id;
    }
}
