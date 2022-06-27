package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	private List<DefaultWeightedEdge>best;
	private Double max;
	
	
	public Model() {
		dao=new GenesDao();
		
	}
	
	public void creaGrafo() {
		this.grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo,dao.getVertici());
		//aggiunt archi
		for(Adiacenza a:dao.getArchi()) {
			Graphs.addEdge(this.grafo,a.getC1(),a.getC2(),a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Double>pesoMinimoEMassimo(){
		List<Double>result=new ArrayList<>();
		Double min=10000000.0;
		Double max=0.0;
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)<min) {
				min=grafo.getEdgeWeight(e);
			}
		}
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)==min) {
				result.add(min);
			}
		}
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>max) {
				max=grafo.getEdgeWeight(e);
			}
		}
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)==max) {
				result.add(max);
			}
		}
		return result;
	}

	public List<Integer> contaArchi(Double soglia) {
		int minori=0;
		int maggiori=0;
		List<Integer> result=new ArrayList<>();
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)<soglia)
				minori++;
			else if(grafo.getEdgeWeight(e)>soglia)
				maggiori++;
		}
		result.add(minori);
		result.add(maggiori);
		return result;
		
	}
	
	public List<Integer>cerca(Double soglia){
		List<DefaultWeightedEdge>parziale=new ArrayList<>();
		List<Integer>result=new ArrayList<>();
		List<Integer>conta=contaArchi(soglia);
		int numMaggiori=conta.get(1);
		max=0.0;
		best=new ArrayList<>();
		cerca_ricorsiva(parziale,numMaggiori,soglia);
		for(DefaultWeightedEdge e:best) {
			if(result.size()==0) {
				result.add(grafo.getEdgeSource(e));
				result.add(grafo.getEdgeTarget(e));
			}
			else {
				result.add(grafo.getEdgeTarget(e));
			}
				
			
		}
		return result;
	}

	private void cerca_ricorsiva(List<DefaultWeightedEdge> parziale,int numMaggiori,Double soglia) {
		if(parziale.size()>numMaggiori)
			return;
		if(parziale.size()<=numMaggiori) {
			if(contaPeso(parziale)>max) {
				max=contaPeso(parziale);
				best=new ArrayList<>(parziale);
			}
		}
		if(parziale.size()==0) {
			for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
				if(grafo.getEdgeWeight(e)>soglia) {
					parziale.add(e);
					cerca_ricorsiva(parziale, numMaggiori, soglia);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		else {
			for(Integer i:Graphs.successorListOf(this.grafo,grafo.getEdgeTarget(parziale.get(parziale.size()-1)))) {
				if(this.grafo.getEdgeWeight(grafo.getEdge(grafo.getEdgeTarget(parziale.get(parziale.size()-1)), i))>soglia &&!parziale.contains(grafo.getEdge(grafo.getEdgeTarget(parziale.get(parziale.size()-1)), i))) {
					parziale.add(grafo.getEdge(grafo.getEdgeTarget(parziale.get(parziale.size()-1)), i));
					cerca_ricorsiva(parziale, numMaggiori, soglia);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
		return;	
	}

	private Double contaPeso(List<DefaultWeightedEdge> parziale) {
		Double peso=0.0;
		for(DefaultWeightedEdge e:parziale) {
			peso+=grafo.getEdgeWeight(e);
		}
		return peso;
	}



}