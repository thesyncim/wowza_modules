package thesyncim;

import java.io.*;

import com.wowza.wms.http.*;
import com.wowza.wms.logging.*;
import com.wowza.wms.vhost.*;

public class ReloadPlaylist extends HTTProvider2Base {

	public void onHTTPRequest(IVHost vhost, IHTTPRequest req, IHTTPResponse resp) {
		if (!doHTTPAuthentication(vhost, req, resp))
			return;
 
		
		ServerListenerStreamPublisher.loadScheduleNow();

		try { 
			OutputStream out = resp.getOutputStream();
			
			out.write(ServerListenerStreamPublisher.loadScheduleNow().getBytes());
		} catch (Exception e) {
			WMSLoggerFactory.getLogger(null).error("ReloadPlaylist: " + e.toString());
		}
 
	}

}
