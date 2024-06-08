package com.example.uasiot.Model;

public class WaterflowModel {
    private double rate,volume_L;

    private String nama;

    public WaterflowModel() {
    }

    public WaterflowModel(double rate, double volume_L, String nama) {
        this.rate = rate;
        this.nama = nama;
        this.volume_L = volume_L;

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getVolume_L() {
        return volume_L;
    }

    public void setVolume_L(double volume_L) {
        this.volume_L = volume_L;
    }


}
