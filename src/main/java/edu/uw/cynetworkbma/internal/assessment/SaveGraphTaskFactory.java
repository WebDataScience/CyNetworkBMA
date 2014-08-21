package edu.uw.cynetworkbma.internal.assessment;

import org.cytoscape.work.*;

public class SaveGraphTaskFactory extends AbstractTaskFactory {

	private final byte[] image;
	
	public SaveGraphTaskFactory(byte[] image) {
		this.image = image;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(
				new SaveGraphTask(image));
	}
}
