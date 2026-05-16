package com.example.feedthehunger.User;

public class ReportModel {

    private String _id;
    private String food_type;
    private String description;
    private String address;
    private String status;
    private String image;
    private String upload_date_time;

    public ReportModel() {
    }

    public ReportModel(String _id, String food_type, String description,
                       String address, String status, String image, String upload_date_time) {
        this._id = _id;
        this.food_type = food_type;
        this.description = description;
        this.address = address;
        this.status = status;
        this.image = image;
        this.upload_date_time = upload_date_time;
    }

    public String get_id() {
        return _id;
    }

    public String getFood_type() {
        return food_type;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getUpload_date_time() {
        return upload_date_time;
    }
}