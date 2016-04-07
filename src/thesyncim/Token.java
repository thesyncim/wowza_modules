package thesyncim;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Token {

	@SerializedName("Id")
	@Expose
	public String Id;
	@SerializedName("ClientId")
	@Expose
	public String ClientId;
	@SerializedName("StreamName")
	@Expose
	public String StreamName;
	@SerializedName("Expire")
	@Expose
	public String Expire;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Token() {
	}
}