package uk.ac.dotrural.quality.getthere.data;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class Updater {
	
	private final String ENDPOINT = "http://dtp-126.sncs.abdn.ac.uk:8080/openrdf-sesame/repositories/GetThereQualityScores/statements"; 
	
	public void sendUpdate(String query)
	{	
		UpdateRequest request = UpdateFactory.create();
		request.add(query);
		
		UpdateProcessor update = UpdateExecutionFactory.createRemoteForm(request, ENDPOINT);
		update.execute();
	}

}
