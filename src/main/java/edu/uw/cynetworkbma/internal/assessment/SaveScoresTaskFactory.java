package edu.uw.cynetworkbma.internal.assessment;

import java.util.List;

import org.cytoscape.model.*;
import org.cytoscape.work.*;

public class SaveScoresTaskFactory extends AbstractTaskFactory {

	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	private final List<AssessmentScores> scores;
	
	public SaveScoresTaskFactory(
			CyTableFactory tableFactory,
			CyTableManager tableManager,
			List<AssessmentScores> scores) {
		
		this.tableFactory = tableFactory;
		this.tableManager = tableManager;
		this.scores = scores;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(
				new SaveScoresTask(
						tableFactory,
						tableManager,
						scores));
	}
}
