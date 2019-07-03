package com.example.lostandfoundnew;

public class ItemModel {
   private int Status;
    private String address;
    private String categories;
    private String date;
    private String description;
    private String id;
    private String imageurl1 = "no";
    private String imageurl2 = "no";
    private String imageurl3 = "no";
    private String imageurl4 = "no";
    private String imageurl5 = "no";
    public double latitude;
    public double longitude;
    private String phone;
    private  String title;
    private String user_id;
     ItemModel() {}

    public ItemModel(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, double paramDouble1, double paramDouble2, String paramString9, int paramInt, String paramString10, String paramString11, String paramString12, String paramString13) {
         title = paramString1;
        address = paramString2;
        categories = paramString3;
        imageurl1 = paramString4;
        imageurl2 = paramString5;
        imageurl3 = paramString6;
        imageurl4 = paramString7;
        imageurl5 = paramString8;
        longitude = paramDouble1;
        latitude = paramDouble2;
        description = paramString9;
        Status = paramInt;
        id = paramString10;
        date = paramString11;
        phone = paramString12;
        user_id = paramString13;
    }

    public String getAddress() { return this.address; }

    public String getCategories() { return this.categories; }

    public String getDate() { return this.date; }

    public String getDescription() { return this.description; }

    public String getId() { return this.id; }

    public String getImageurl1() { return this.imageurl1; }

    public String getImageurl2() { return this.imageurl2; }

    public String getImageurl3() { return this.imageurl3; }

    public String getImageurl4() { return this.imageurl4; }

    public String getImageurl5() { return this.imageurl5; }

    public double getLatitude() { return this.latitude; }

    public double getLongitude() { return this.longitude; }

    public String getPhone() { return this.phone; }

    public int getStatus() { return this.Status; }

    public String getTitle() { return this.title; }

    public String getUser_id() { return this.user_id; }

    public void setAddress(String paramString) { this.address = paramString; }

    public void setCategories(String paramString) { this.categories = paramString; }

    public void setDate(String paramString) { this.date = paramString; }

    public void setDescription(String paramString) { this.description = paramString; }

    public void setId(String paramString) { this.id = paramString; }

    public void setImageurl1(String paramString) { this.imageurl1 = paramString; }

    public void setImageurl2(String paramString) { this.imageurl2 = paramString; }

    public void setImageurl3(String paramString) { this.imageurl3 = paramString; }

    public void setImageurl4(String paramString) { this.imageurl4 = paramString; }

    public void setImageurl5(String paramString) { this.imageurl5 = paramString; }

    public void setLatitude(double paramDouble) { this.latitude = paramDouble; }

    public void setLongitude(double paramDouble) { this.longitude = paramDouble; }

    public void setPhone(String paramString) { this.phone = paramString; }

    public void setStatus(int paramInt) { this.Status = paramInt; }

    public void setTitle(String paramString) { this.title = paramString; }

    public void setUser_id(String paramString) { this.user_id = paramString; }
}
