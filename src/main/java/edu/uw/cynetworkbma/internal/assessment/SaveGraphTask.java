package edu.uw.cynetworkbma.internal.assessment;

import java.io.*;

import org.cytoscape.work.*;

public class SaveGraphTask extends AbstractTask {

	private final byte[] image;
	
	public SaveGraphTask(byte[] image) {
		this.image = image;
	}
	
	@Tunable(description="Save PNG Image As", params="fileCategory=image;input=false")
	public File file;
	
	@Override
	public void run(final TaskMonitor taskMonitor) throws Exception {
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(image);
		stream.close();
	}
}
