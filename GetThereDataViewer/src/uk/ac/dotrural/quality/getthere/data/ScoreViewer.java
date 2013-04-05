package uk.ac.dotrural.quality.getthere.data;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

public class ScoreViewer {
	
	private Double[] thresholds = {0.0, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 1.0};
	private ArrayList<Integer> rejected = new ArrayList<Integer>();
	
	private ArrayList<Integer> timeliness = new ArrayList<Integer>();
	private ArrayList<Integer> relevance = new ArrayList<Integer>();
	private ArrayList<Integer> availability = new ArrayList<Integer>();
	private ArrayList<Integer> accuracy = new ArrayList<Integer>();
	
	public static void main(String[] args)
	{
		new ScoreViewer();
	}
	
	public ScoreViewer()
	{		
		getScores();
		
		System.out.print("Threshold: ");
		for(int i=0; i<thresholds.length; i++)
			System.out.print("\t" + thresholds[i]);
		System.out.println("");
		
		System.out.print("Rejected: ");
		for(int i=0;i<rejected.size();i++)
			System.out.print("\t" + rejected.get(i));
		System.out.println("");
		
		System.out.println("");
		System.out.print("Timeliness: ");
		for(int i=0;i<timeliness.size();i++)
			System.out.print("\t" + timeliness.get(i));
		System.out.println("");
		
		System.out.print("Relevance: ");
		for(int i=0;i<relevance.size();i++)
			System.out.print("\t" + relevance.get(i));
		System.out.println("");
		
		System.out.print("Availability: ");
		for(int i=0;i<availability.size();i++)
			System.out.print("\t" + availability.get(i));
		System.out.println("");
		
		System.out.print("Accuracy: ");
		for(int i=0;i<accuracy.size();i++)
			System.out.print("\t" + accuracy.get(i));
		System.out.println("");
		
		
	}
	
	public void getScores()
	{
		String endpoint = "http://dtp-126.sncs.abdn.ac.uk:8080/openrdf-sesame/repositories/GetThereQualityScores";
		String qual = "http://abdn.ac.uk/~r01ccb9/Qual-O/";
		
		for(int i=0;i<thresholds.length;i++)
		{
			String query = "SELECT * WHERE {" +
							"	?result a <" + qual + "Result> . " +
							"	?result <" + qual + "hasScore> ?score . " +
							"	?assess <http://abdn.ac.uk/~r01ccb8/Qual-O/produces> ?result . " +
							"	?metric <" + qual + "guides> ?assess . " +
							"	?metric <" + qual + "evaluates> ?dimension . " +
							"}";
			
			int t = 0;
			int r = 0;
			int a = 0;
			int ac = 0;
			
			QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
			ResultSet rs = qe.execSelect();
			int count = 0;
			while(rs.hasNext())
			{
				QuerySolution qs = rs.next();
				Literal score = qs.getLiteral("score");
				
				if(Double.parseDouble(score.toString()) > thresholds[i])
				{
					Resource dimension = qs.getResource("dimension");
					String d = dimension.getLocalName();
					
					if(d.equals("Timeliness"))
						t++;
					if(d.equals("Relevance"))
						r++;
					if(d.equals("Availability"))
						a++;
					if(d.equals("Accuracy"))
						ac++;
					count++;
				}
			}
			timeliness.add(t);
			relevance.add(r);
			availability.add(a);
			accuracy.add(ac);
			
			rejected.add(count);
		}
	}

}
