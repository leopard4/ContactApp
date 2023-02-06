package com.leopard4.contactapp.model;

import java.io.Serializable;

public class Contact implements Serializable {   // Serializable 인터페이스를 구현해야, 객체를 직렬화할 수 있다.

    public int id;
    public String name;
    public String phone;

    public Contact() {
    }
    public Contact(String name, String phone) {

        this.name = name;
        this.phone = phone;
    }

    public Contact(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
