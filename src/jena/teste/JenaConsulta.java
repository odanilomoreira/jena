package jena.teste;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Literal;


public class JenaConsulta {

	public static void main(String[] args) {
		
		String banda = "iron maiden";
		
		String prefixos = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
						  "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "+
						  "PREFIX dbpprop: <http://dbpedia.org/property/> ";
		
		String queryBanda = prefixos +
				"SELECT ?website ?membersLink ?desc " +
				"WHERE { " +
				"?x a dbpedia-owl:Band ; " +
				"   dbpprop:name ?name ; " +
				"   dbpedia-owl:abstract ?desc ; " +
				"   foaf:homepage ?website ; " +
				"   dbpprop:currentMembers ?membersLink . " +
				//"FILTER (?name = \"iron maiden\")"+
				//"FILTER regex(srt(?name) =   \""+banda+"\""+ ")" +
				"FILTER (langMatches(lang(?desc), \"PT\")) " +
				"}";
		
		QueryExecution qe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryBanda);
		
		ResultSet results = qe.execSelect();
		
		//ResultSetFormatter.out(System.out, results );
		
		String descricao = "";
		String site = "";
		String membros = "";
		
		while (results.hasNext()) {
			QuerySolution linha = (QuerySolution) results.nextSolution();
			
			Resource siteRes = linha.getResource("website");
			site = siteRes.getURI();
			
			Literal descLiteral = linha.getLiteral("desc");
			descricao = ("" + descLiteral.getValue());
			
			RDFNode membersNode = linha.get("membersLink");
			membros = (membros + "\n" + membersNode.toString());
		}
		
		System.out.println(membros);
		
		qe.close();
		
		
	}

}
