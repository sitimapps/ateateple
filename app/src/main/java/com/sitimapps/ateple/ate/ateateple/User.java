package com.sitimapps.ateple.ate.ateateple;

public class User {

    private String id;
    private String name;
    private String email;
    private String token;
    private String photo;
    private int money;
    private int hold;
    private int wait_out;
    private int view_today;
    private int click_today;
    private int view_a;
    private int click_a;
    private int code_ref_id = 0;

    public String getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getToken() {
        return this.token;
    }

    public String getPhoto() {
        return photo;
    }

    public int getMoney() {
        return money;
    }

    public int getHold() {
        return hold;
    }

    public int getWait_out() {
        return wait_out;
    }

    public int getView_today() {
        return view_today;
    }

    public int getClick_today() {
        return click_today;
    }

    public int getView_a() {
        return view_a;
    }

    public int getClick_a() {
        return click_a;
    }

    public int getCode_ref_id() {
        return code_ref_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setHold(int hold) {
        this.hold = hold;
    }

    public void setWait_out(int wait_out) {
        this.wait_out = wait_out;
    }

    public void setView_today(int view_today) {
        this.view_today = view_today;
    }

    public void setClick_today(int click_today) {
        this.click_today = click_today;
    }

    public void setView_a(int view_a) {
        this.view_a = view_a;
    }

    public void setClick_a(int click_a) {
        this.click_a = click_a;
    }

    public void setCode_ref_id(int code_ref_id) {
        this.code_ref_id = code_ref_id;
    }

    public boolean outMoney() {
        if (checkBalansToOut()) {
            return true;
        }
            return false;
    }

    private boolean checkBalansToOut() {
        if (this.money>=500 && this.wait_out==0) {
            return true;
        } else {
            return false;
        }
    }
}
