package com.pin91.jojovehicleapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PacketTrackingBean implements Serializable{

	private Integer packetTrackingId;
	private Integer packetId;
	private String packetName;
	private String createdDate;
	private String message;

	public String getPacketCode() {
		return packetCode;
	}

	public void setPacketCode(String packetCode) {
		this.packetCode = packetCode;
	}

	private String status;
	private String packetCode;


	public Integer getPacketTrackingId() {
		return packetTrackingId;
	}

	public void setPacketTrackingId(Integer packetTrackingId) {
		this.packetTrackingId = packetTrackingId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPacketName() {
		return packetName;
	}

	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getPacketId() {
		return packetId;
	}

	public void setPacketId(Integer packetId) {
		this.packetId = packetId;
	}

}
