package uk.ac.dotrural.quality.getthere.data;

import java.util.ArrayList;

import net.sf.json.JSONObject;

public class Observation {

	public String uri;
	public ArrayList<QualityScore> annotations;
	
	public Observation(String uri)
	{
		this.uri = uri;
		annotations = new ArrayList<QualityScore>();
	}
	
	public void addAnnotation(JSONObject annotation)
	{
		String name = annotation.getString("name");
		String desc = annotation.getString("description");
		String score = annotation.getString("plainScore");
		String affectedInstance = annotation.getString("affectedInstance");
		String label = annotation.getString("label");
		annotations.add(new QualityScore(name, desc, score, affectedInstance, label));
	}
	
}
