package main.java.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ProArenaEntity {

    @Id
    private long arenaId;

    private String name;

    private String location;

    private int capacity;

    private String address;

    private int openedYear;

    private String surface;

    private String amenities;

    private double cost;

    public long getArenaId() {
        return arenaId;
    }

    public void setArenaId(long arenaId) {
        this.arenaId = arenaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOpenedYear() {
        return openedYear;
    }

    public void setOpenedYear(int openedYear) {
        this.openedYear = openedYear;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "ProArenaEntity{" +
                "arenaId=" + arenaId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", address='" + address + '\'' +
                ", openedYear=" + openedYear +
                ", surface='" + surface + '\'' +
                ", amenities='" + amenities + '\'' +
                ", cost=" + cost +
                '}';
    }
}