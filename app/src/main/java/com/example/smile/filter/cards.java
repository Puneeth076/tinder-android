package com.example.smile.filter;

public class cards {
    String name;
    String userId;
    String userProfile;
    public cards(String userId, String name, String userProfile){
        this.name = name;
        this.userId= userId;
        this.userProfile= userProfile;
    }

    public String getUserId(){
        return  userId;
    }

    public void setUserId( String userId){
        this.userId=userId;
    }

    public String getName(){
        return  name;
    }

    public void setName( String name){
        this.name=name;
    }
    public String getUserProfile(){
        return  userProfile;
    }

    public void setUserProfile( String userProfile){
        this.userProfile=userProfile;
    }

}
