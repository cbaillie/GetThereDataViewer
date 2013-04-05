package uk.ac.dotrural.quality.getthere.data;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

public class ObservationViewer {
	
	private final String ENDPOINT = "http://irp.sncs.abdn.ac.uk:8080/openrdf-sesame/repositories/journeys";
	private final String observationUri = "http://dtp-24.sncs.abdn.ac.uk:8093/observation/96e701ce-7579-4201-a174-9fb1b170a600";
	
	public static void main(String[] args)
	{
		new ObservationViewer();
	}
	
	public ObservationViewer()
	{
		ArrayList<String> results = new ArrayList<String>();
		String query = "DESCRIBE <" + observationUri + ">";
		
		try
		{
			System.out.println(query);
			QueryExecution qe = QueryExecutionFactory.sparqlService(ENDPOINT, query);
			ResultSet rs = qe.execSelect();
			while(rs.hasNext())
			{
				QuerySolution qs = rs.next();
				Resource observation = qs.getResource("obs");
				System.out.println(qs.toString());
				results.add(observation.getURI());
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception: " + ex.toString());
		}
	}

}
