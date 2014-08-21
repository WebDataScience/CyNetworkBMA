package edu.uw.cynetworkbma.internal;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="connectionParameters")
@XmlAccessorType(XmlAccessType.NONE)
public class ConnectionParameters {

	@XmlElement(name="host")
	private String host;
	
	@XmlElement(name="port")
	private int port;
	
	private boolean loginRequired;
	private String username;
	private String password;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isLoginRequired() {
		return loginRequired;
	}
	
	public void setLoginRequired(boolean loginRequired) {
		this.loginRequired = loginRequired;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public static ConnectionParameters getDefault() {
		try {
			JAXBContext context = JAXBContext.newInstance(ConnectionParameters.class);
			Unmarshaller um = context.createUnmarshaller();
			return (ConnectionParameters)um.unmarshal(
					ConnectionParameters.class.getResourceAsStream("/connectionParameters.xml"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return new ConnectionParameters();
	}
}
