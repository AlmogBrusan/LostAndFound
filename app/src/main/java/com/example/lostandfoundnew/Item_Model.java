package com.example.lostandfoundnew;

public class Item_Model {
    int Status;

    public String address;

    public String categories;

    String date;

    String description;

    String id;

    String imageurl1 = "no";

    String imageurl2 = "no";

    String imageurl3 = "no";

    String imageurl4 = "no";

    String imageurl5 = "no";

    double latitude;

    double longitude;

    String phone;

    public String title;

    String user_id;

    public Item_Model() {}

    public Item_Model(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, double paramDouble1, double paramDouble2, String paramString9, int paramInt, String paramString10, String paramString11, String paramString12, String paramString13) {
        this.title = paramString1;
        this.address = paramString2;
        this.categories = paramString3;
        this.imageurl1 = paramString4;
        this.imageurl2 = paramString5;
        this.imageurl3 = paramString6;
        this.imageurl4 = paramString7;
        this.imageurl5 = paramString8;
        this.longitude = paramDouble1;
        this.latitude = paramDouble2;
        this.description = paramString9;
        this.Status = paramInt;
        this.id = paramString10;
        this.date = paramString11;
        this.phone = paramString12;
        this.user_id = paramString13;
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
