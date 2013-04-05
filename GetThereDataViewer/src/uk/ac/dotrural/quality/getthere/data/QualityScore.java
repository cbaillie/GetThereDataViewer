package uk.ac.dotrural.quality.getthere.data;

import java.util.UUID;

public class QualityScore {
	
	public String name;
	public String description;
	public String score;
	public String observationUri;
	public String label;
	
	public QualityScore(String n, String d, String s, String o, String l)
	{
		name = n;
		description = d;
		score = s;
		observationUri = o;
		label = l;
	}
	
	public String getQuery(String observationUri)
	{
		
		String uri = "http://dtp-126.sncs.abdn.ac.uk/GetThere/Quality/Result/" + name + "/" + UUID.randomUUID();
		String assessment = "http://dtp-126.sncs.abdn.ac.uk/GetThere/Quality/Assessment/" + UUID.randomUUID();
		String prov = "http://www.w3.org/ns/prov-o/";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT DATA {\n");
		sb.append("\t<" + uri + "> a <http://abdn.ac.uk/~r01ccb9/Qual-O/Result> . \n");
		sb.append("\t<" + uri + "> a <" + prov + "Entity> . \n");
		sb.append("\t<" + uri + "> <http://abdn.ac.uk/~r01ccb9/Qual-O/hasScore> \"" + score + "\" .\n");
		sb.append("\t<" + uri + "> <http://abdn.ac.uk/~r01ccb9/Qual-O/description> \"" + description + "\" . \n");
		sb.append("\t<" + uri + "> <http://abdn.ac.uk/~r01ccb9/Qual-O/annotates> <" + observationUri + "> . \n");
		sb.append("\t<" + assessment + "> <http://abdn.ac.uk/~r01ccb8/Qual-O/produces> <" + uri + "> . \n");
		sb.append("\t<" + assessment + "> <" + prov + "Used> <" + observationUri + "> . \n");
		sb.append("\t<" + assessment + "> <" + prov + "Used> <http://abdn.ac.uk/~r01ccb9/Qual-O/" + name + "Metric_1> . \n");
		sb.append("\t<" + uri + "> <" + prov + "wasGeneratedBy> <" + assessment + "> . \n");
		sb.append("\t<http://abdn.ac.uk/~r01ccb9/Qual-O/" + name + "Metric_1> <http://abdn.ac.uk/~r01ccb9/Qual-O/guides> <" + assessment + "> . \n");
		sb.append("\t<" + assessment + "> a <http://abdn.ac.uk/~r01ccb9/Qual-O/Assessment> . \n");
		sb.append("\t<" + assessment + "> a <" + prov + "Activity> . \n");
		sb.append("\t<http://abdn.ac.uk/~r01ccb9/Qual-O/" + name + "Metric_1> <http://abdn.ac.uk/~r01ccb9/Qual-O/evaluates> <http://abdn.ac.uk/~r01ccb9/Qual-O/Dimension/" + name + "> . \n");
		sb.append("\t<http://abdn.ac.uk/~r01ccb9/Qual-O/" + name + "Metric_1> a <" + prov + "Entity> . \n");
		sb.append("\t<http://abdn.ac.uk/~r01ccb9/Qual-O/Dimension/" + name + "> a <http://abdn.ac.uk/~r01ccb9/Qual-O/Dimension> . \n");
		sb.append("\t<http://abdn.ac.uk/~r01ccb9/Qual-O/Dimension/" + name + "> a <" + prov + "Entity> . \n");
		sb.append("}\n");
		
		return sb.toString();
	}

}
