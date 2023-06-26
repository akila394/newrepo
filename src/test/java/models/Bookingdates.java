package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class Bookingdates {

    @JsonProperty("checkin")
    private String checkin;
    @JsonProperty("checkout")
    private String checkout;

    /**
     * No args constructor for use in serialization
     */
    public Bookingdates() {
    }

    /**
     * @param checkin
     * @param checkout
     */
    public Bookingdates(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    @JsonProperty("checkin")
    public String getCheckin() {
        return checkin;
    }

    @JsonProperty("checkin")
    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    @JsonProperty("checkout")
    public String getCheckout() {
        return checkout;
    }

    @JsonProperty("checkout")
    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

}
