package com.pin91.jojodelivery.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PacketTrackingBean {

	private Integer packetTrackingId;
	private Integer packetId;
	private String packetName;
	private String createdDate;
	private String message;
	private String status;

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
