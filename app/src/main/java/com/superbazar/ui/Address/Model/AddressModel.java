package com.superbazar.ui.Address.Model;

public class AddressModel {
    String name,phone,address,landmark,pincode;

    public AddressModel(String name, String phone, String address, String landmark, String pincode) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.landmark = landmark;
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getPincode() {
        return pincode;
    }
}
