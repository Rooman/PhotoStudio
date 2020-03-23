package com.photostudio.exception.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorChangeOrderStatus {

    INCORRECT_STATUS_FORWARD("Order status %s can't be changed forward"),
    INCORRECT_STATUS_BACK("Order status %s can't be changed back"),
    INCORRECT_STATUS_FOR_USER("Order status %s can't be changed by USER"),
    INCORRECT_STATUS_FOR_ADMIN("Order status %s can't be changed by ADMIN"),
    PHOTOS_SHOULD_BE_LOADED("Photos should be loaded"),
    PHOTOS_SHOULD_BE_SELECTED("Photos should be selected"),
    PHOTOS_SHOULD_BE_PAID("Photos should be paid");

    private String message;
}
