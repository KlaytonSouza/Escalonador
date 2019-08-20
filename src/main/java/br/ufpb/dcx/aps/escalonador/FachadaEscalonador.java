package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;

public class FachadaEscalonador {
	
	private int quantm;
	private int tick;
	private TipoEscalonador tipoEscalonador;
	private ArrayList<String> listaProcesso;
	private String rodando;
	private ArrayList<String> processoBloqueado;
	private String tempo;

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantm = 3;
		this.tick = 0;
		this.rodando = "";
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new ArrayList<String>();
		this.processoBloqueado = new ArrayList<String>();
	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
	}

	public String getStatus() {
		if (this.listaProcesso.size() != 0 && tempo != listaProcesso.get(0)) {
			return ("Escalonador " + this.tipoEscalonador + ";" 
		+ "Processos: {Fila: " + this.listaProcesso.toString() + "};" 
		+ "Quantum: " + this.quantm + ";" 
		+ "Tick: " + this.tick);			
		} else {
			return ("Escalonador " + this.tipoEscalonador + ";" 
			+ "Processos: {" + this.rodando + "};"
			+ "Quantum: " + this.quantm + ";" 
				+ "Tick: " + this.tick);
		}
	}

	public void tick() {
		this.tick++;
		if(this.listaProcesso.size() != 0){
			this.rodando = "Rodando: "+this.listaProcesso.get(0);
			tempo = listaProcesso.get(0);
		
		}else {
			this.rodando = "";
			this.tempo = "";
		}
	}


	public void adicionarProcesso(String nomeProcesso) {
		listaProcesso.add(nomeProcesso);
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
	}

	public void finalizarProcesso(String nomeProcesso) {
		int nomeProcessoEncontrado = -1;

		for(int k = 0; k<listaProcesso.size(); k++) {
			if(listaProcesso.get(k).equals(nomeProcesso)) {
				nomeProcessoEncontrado = k;				
			}
		}if(nomeProcessoEncontrado >= 0) {
			listaProcesso.remove(nomeProcessoEncontrado);
		}
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {

	}

}
