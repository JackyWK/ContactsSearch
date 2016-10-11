package com.handsomezhou.contactssearch.model;

public class BaseContacts implements Cloneable{
	private String mId;
	private String mName;
	private String mPhoneNumber;
	private String mBdouNumber;

	public void setBdouNumber(String mBdouNumber) {
		this.mBdouNumber = mBdouNumber;
	}

	public String getBdouNumber() {
		return mBdouNumber;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
}
