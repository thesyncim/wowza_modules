package thesyncim;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowza.util.HTTPUtils;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.client.IClient;
import com.wowza.wms.httpstreamer.model.IHTTPStreamerSession;
import com.wowza.wms.mediacaster.IMediaCaster;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.rtp.model.RTPSession;
import com.wowza.wms.stream.IMediaStreamNameAliasProvider2;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;

public class SecureStream extends ModuleBase implements IMediaStreamNameAliasProvider2 {

	private String script;

	public void onAppStart(IApplicationInstance appInstance) {

		script = "http://opinion.azorestv.com/api/tokens/get/";

		appInstance.setStreamNameAliasProvider(this);

	}

	public String resolvePlayAlias(IApplicationInstance appInstance, String shibId, IClient client) {
		getLogger().info("My Log connect");

		// get the key from the flash player

		try {
			if (shibId == null) {
				return null;
			}
			// get key validity from the web server
			byte[] resp = HTTPUtils.HTTPRequestToByteArray(script + shibId, "POST", null, null);
			String response = new String(resp, "UTF-8");

			getLogger().info(response);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Token t = gson.fromJson(response, Token.class);

			Date expire = parseDateTime(t.Expire);

			Date now = new Date();

			if (now.after(expire)) {
				return null;
			}

			return t.StreamName;
			// check if key was valid

		} catch (Exception e) {
			getLogger().info(e);
			getLogger().info("Failed to log client " + client.getIp());
		}

		return null;
	}

	public void onDisconnect(IClient client) {

	}

	public String resolveStreamAlias(IApplicationInstance appInstance, String name) {
		getLogger().info("Resolve Stream: " + name);
		return name;
	}

	public String resolveStreamAlias(IApplicationInstance appInstance, String name, IMediaCaster mediaCaster) {
		return resolveStreamAlias(appInstance, name);
	}

	public String resolvePlayAlias(IApplicationInstance appInstance, String name, IHTTPStreamerSession httpSession) {
		// get the key from the flash player

		try {
			if (name == null) {
				return null;
			}
			// get key validity from the web server
			byte[] resp = HTTPUtils.HTTPRequestToByteArray(script + name, "POST", null, null);
			String response = new String(resp, "UTF-8");

			getLogger().info(response);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Token t = gson.fromJson(response, Token.class);

			Date expire = parseDateTime(t.Expire);

			Date now = new Date();

			if (now.after(expire)) {
				return null;
			}

			return t.StreamName;
			// check if key was valid

		} catch (Exception e) {
			getLogger().info(e);
			getLogger().info("Failed to log client ");
		}

		return null;
	}

	public String resolvePlayAlias(IApplicationInstance appInstance, String name, RTPSession rtpSession) {
		getLogger().warn("Blocked Play RTPSession");
		return null;
	}

	public String resolvePlayAlias(IApplicationInstance appInstance, String name,
			ILiveStreamPacketizer liveStreamPacketizer) {
		getLogger().warn("Blocked Play LiveStreamPacketizer");
		return null;
	}

	public String resolvePlayAlias(IApplicationInstance appInstance, String name) {
		getLogger().warn("Blocked Play");
		return null;
	}

	public static Date parseDateTime(String dateString) {
		if (dateString == null)
			return null;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
		if (dateString.contains("T"))
			dateString = dateString.replace('T', ' ');
		if (dateString.contains("Z"))
			dateString = dateString.replace("Z", "+0000");
		else
			dateString = dateString.substring(0, dateString.lastIndexOf(':'))
					+ dateString.substring(dateString.lastIndexOf(':') + 1);
		try {
			return fmt.parse(dateString);
		} catch (ParseException e) {
			getLogger().error("Could not parse datetime: " + dateString);
			return null;
		}
	}
}
