/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author thanb_000
 */
public class Elevator {
    
    //<editor-fold desc="Filter">
    private final byte NO_FILTER = 0;
    private final byte FILTER_BY_BASEPRICE = 1;
    private final byte FILTER_BY_FLOORPRICE = 2;
    private final byte FILTER_BY_MAXWEIGHT = 3;
    private final byte FILTER_BY_MAXHUMAN = 4;
    private final String WEIGHT_TYPE = "kg";
    private final String MONEY_TYPE = "$";
    private byte filter = NO_FILTER;
    //</editor-fold>
    
    private int id;
    private String name;
    private double basePrice;
    private double floorPrice;
    private String description;
    private int maxWeight;
    private int maxHuman;
    private String country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        String additionalData = "";
        if (this.filter == FILTER_BY_BASEPRICE) {
            additionalData = this.getBasePrice() + " " + MONEY_TYPE;
        } else if (this.filter == FILTER_BY_FLOORPRICE) {
            additionalData = this.getFloorPrice() + " " + MONEY_TYPE;
        } else if (this.filter == FILTER_BY_MAXWEIGHT) {
            additionalData = this.getMaxWeight() + " " + WEIGHT_TYPE;
        } else if (this.filter == FILTER_BY_MAXHUMAN) {
            additionalData = this.getMaxHuman() + " person";
        } else{
            additionalData = "";
        }
        return name + (additionalData.length() > 0 ? (" (" + additionalData + ")") : "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(double floorPrice) {
        this.floorPrice = floorPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMaxHuman() {
        return maxHuman;
    }

    public void setMaxHuman(int maxHuman) {
        this.maxHuman = maxHuman;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    

    public void setFilter(byte filter) {
        this.filter = filter;
    }
    
    public boolean isGreater(Elevator eleB){
        if (this.filter == FILTER_BY_BASEPRICE) {
            return this.basePrice > eleB.getBasePrice();
        } else if (this.filter == FILTER_BY_FLOORPRICE) {
            return this.floorPrice > eleB.getFloorPrice();
        } else if (this.filter == FILTER_BY_MAXWEIGHT) {
            return this.maxWeight > eleB.getMaxWeight();
        } else if (this.filter == FILTER_BY_MAXHUMAN) {
            return this.maxHuman > eleB.getMaxHuman();
        } else{
            return true;
        }
    }
}
