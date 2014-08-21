package edu.uw.cynetworkbma.internal;

import java.io.*;

public class ScriptReader {

	public String readScript(InputStream stream) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
