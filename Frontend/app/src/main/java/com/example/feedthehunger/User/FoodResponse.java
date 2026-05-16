package com.example.feedthehunger.User;

import java.util.List;

public class FoodResponse {
    private boolean status;
    private List<ReportModel> foods;

    public FoodResponse(boolean status, List<ReportModel> foods) {
        this.status = status;
        this.foods = foods;
    }

    public FoodResponse() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ReportModel> getFoods() {
        return foods;
    }

    public void setFoods(List<ReportModel> foods) {
        this.foods = foods;
    }
}
