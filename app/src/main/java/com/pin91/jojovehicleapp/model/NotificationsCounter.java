package com.pin91.jojovehicleapp.model;

/**
 * Created by udit on 12/27/2015.
 */
public class NotificationsCounter {
    int todoOrderCount;
    int pickupOrderCount;
    int intransitOrderCount;

    public int getTodoOrderCount() {
        return todoOrderCount;
    }

    public void setTodoOrderCount(int todoOrderCount) {
        this.todoOrderCount = todoOrderCount;
    }

    public int getPickupOrderCount() {
        return pickupOrderCount;
    }

    public void setPickupOrderCount(int pickupOrderCount) {
        this.pickupOrderCount = pickupOrderCount;
    }

    public int getIntransitOrderCount() {
        return intransitOrderCount;
    }

    public void setIntransitOrderCount(int intransitOrderCount) {
        this.intransitOrderCount = intransitOrderCount;
    }
}
