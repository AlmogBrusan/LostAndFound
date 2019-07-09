package com.example.lostandfoundnew;

public class ItemModel {
   private int Status;
    private String address;
    private String categories;
    private String date;
    private String description;
    private String id;
    private String imageurl = "no";
    public double latitude;
    public double longitude;
    private String phone;
    private  String title;
    private String user_id;
     ItemModel() {}

    public ItemModel(String paramString1, String paramString2, String paramString3, String paramString4, double paramDouble1, double paramDouble2, String paramString9, int paramInt, String paramString10, String paramString11, String paramString12, String paramString13) {
         title = paramString1;
        address = paramString2;
        categories = paramString3;
        imageurl = paramString4;
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

    public String getImageurl() { return this.imageurl; }

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

    public void setImageurl(String paramString) { this.imageurl = paramString; }

    public void setLatitude(double paramDouble) { this.latitude = paramDouble; }

    public void setLongitude(double paramDouble) { this.longitude = paramDouble; }

    public void setPhone(String paramString) { this.phone = paramString; }

    public void setStatus(int paramInt) { this.Status = paramInt; }

    public void setTitle(String paramString) { this.title = paramString; }

    public void setUser_id(String paramString) { this.user_id = paramString; }
}
