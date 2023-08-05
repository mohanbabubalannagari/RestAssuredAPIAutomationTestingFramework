package com.apitesting.pojos;

public class BookingDates {
	
	private String checkin; 
	private String checkout; 
	
	public BookingDates(String chin, String chout) {

        setCheckin(chin);
        setCheckout(chout);
	}
	
	public String getCheckin() {
		return checkin;
	}
	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}
	public String getCheckout() {
		return checkout;
	}
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}


}
