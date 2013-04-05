package uk.ac.dotrural.quality.getthere.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

public class Viewer {
	
	private final String ENDPOINT = "http://irp.sncs.abdn.ac.uk:8080/openrdf-sesame/repositories/journeys"; 
	private final String OBS_SERVICE = "http://irp.sncs.abdn.ac.uk:8080/";
	private final String QUALITY_SERVICE = "http://dtp-126.sncs.abdn.ac.uk:8080/ecosystem-quality/QualityAssessment?";
	private final String RULE_LOCATION = "http://dtp-126.sncs.abdn.ac.uk/ontologies/GetThereQuality/GetThereQualityRules.ttl&test=true";
	
	public Viewer()
	{
		
	}
	
	public void loadObservations()
	{
		ArrayList<Observation> observations = getObservations();
		observations = assessObservationQuality(observations);
		outputObservations(observations);
	}
	
	private void outputObservations(ArrayList<Observation> observations)
	{
		try
		{
			for(int i=0;i<observations.size();i++)
			{
				Observation obs = (Observation)observations.get(i);
				for(int j=0;j<obs.annotations.size();j++)
				{	
					QualityScore qs = (QualityScore)obs.annotations.get(j);
					new Updater().sendUpdate(qs.getQuery(obs.uri));
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("outputObservations Exception: " + ex.toString());
		}
	}
	
	private ArrayList<Observation> assessObservationQuality(ArrayList<Observation> observations) 
	{	
		for(int i=0;i<observations.size();i++)
		{
			Observation observation = (Observation)observations.get(i);
			
			StringBuilder address = new StringBuilder();
			address.append(QUALITY_SERVICE);
			address.append("observationUri=");
			address.append(observation.uri);
			address.append("&observationEndpoint=");
			address.append(OBS_SERVICE);
			address.append("&ruleLocation=");
			address.append(RULE_LOCATION);
			
			System.out.println("URL: " + address.toString());
			
			try
			{
				URL url = new URL(address.toString());
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);
				
				String in = "";
				StringBuffer sb = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while((in = br.readLine()) != null)
				{
					sb.append(in);
				}
			
				JSONObject json = (JSONObject)JSONSerializer.toJSON(sb.toString());
				JSONObject annotations = json.getJSONObject("annotations");
				
				JSONObject timeliness = annotations.getJSONObject("TimelinessAnnotation");
				JSONObject relevance = annotations.getJSONObject("RelevanceAnnotation");
				JSONObject availability = annotations.getJSONObject("AvailabilityAnnotation");
				JSONObject accuracy = annotations.getJSONObject("AccuracyAnnotation");
				
				observation.addAnnotation(timeliness);
				observation.addAnnotation(relevance);
				observation.addAnnotation(availability);
				observation.addAnnotation(accuracy);
			}
			catch(Exception ex)
			{
				System.out.println("assessObservation Exception: " + ex.toString());
			}
		}
		
		return observations;
	}

	public ArrayList<Observation> getObservations()
	{
		System.out.println("Getting observations");
		ArrayList<Observation> results = new ArrayList<Observation>();
		String query = "SELECT * WHERE {" +
						"	?obs a <http://www.dotrural.ac.uk/irp/uploads/ontologies/sensors/LocationDeviceObservation> " + 
						"}";
		
		try
		{
			QueryExecution qe = QueryExecutionFactory.sparqlService(ENDPOINT, query);
			ResultSet rs = qe.execSelect();
			while(rs.hasNext())
			{
				QuerySolution qs = rs.next();
				Resource observation = qs.getResource("obs");
				results.add(new Observation(observation.getURI()));
			}
		}
		catch(Exception ex)
		{
			System.out.println("getObservations Exception: " + ex.toString());
		}
		return results;	
	}

}
