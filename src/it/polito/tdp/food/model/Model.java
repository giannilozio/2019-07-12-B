package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph <Food,DefaultWeightedEdge> grafo;
	private FoodDao dao;
	private Map <Integer,Food> idMap;
	private List<Food> vertici;
	private List<PD> archi;
	private List<ConPeso> best;
	
	
	
	public Model() {
		
		idMap = new HashMap<>();
		dao = new FoodDao();
		vertici = new ArrayList<>();
		best = new ArrayList<>();
	}

	public void creaGrafo(int porzioni) {

		grafo = new SimpleDirectedWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		vertici = dao.getVertici(porzioni,idMap);
		
		Graphs.addAllVertices(grafo, vertici);
		
		for (Food f1 : vertici) {
			for(Food f2 : vertici) {
				
				if(f1.getFood_code()!=f2.getFood_code()) {
					if(f1.getD()>f2.getD()) {
						double diff = f1.getD()-f2.getD();
						Graphs.addEdge(grafo, f1, f2,diff);
					}
				}
				
			}
		}
	}

	public List<Food> getVertici() {
		return vertici;
	}

	public void setVertici(List<Food> vertici) {
		this.vertici = vertici;
	}

	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Food, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public List<ConPeso> getBest(Food f) {
		
	List<Food> succ = Graphs.successorListOf(grafo, f);
	best = new ArrayList<>();
	
	for ( Food f1 : succ) {
	
		DefaultWeightedEdge edge = grafo.getEdge(f, f1);
		
		double peso = grafo.getEdgeWeight(edge);
		
		ConPeso p = new ConPeso (f1,peso);
		
		best.add(p);
		
	}
	best.sort( (e1,e2) -> e1.compareTo(e2));
	
	return best;
	}

}
