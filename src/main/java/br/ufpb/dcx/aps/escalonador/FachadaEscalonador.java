package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.Arrays;

public class FachadaEscalonador {
	
	private TipoEscalonador tipoEscalonador;
	private int quantm;
	private int tick;
	private ArrayList<String> listaProcesso;
	private ArrayList<String> processoBloqueado;
	private ArrayList<String> fila;
	private String tempo;
	private String rodando;
	private String resultado = "";

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
		//tipoEscalonador
		//Escalonador Prioridade;Processos: {Rodando: P1, Fila: [P2]};Quantum: 3;Tick: 1
		resultado = "Escalonador " + this.tipoEscalonador + ";";
		
		if(rodando != "" ) {
			resultado += "Processos: {Rodando: " + rodando + "};"
					+ "Quantum: " + this.quantm + ";"
					+ "Tick: " + this.tick;
		
		}
		if(rodando == "") {
			resultado += "Processos: {};Quantum: "+this.quantm +";Tick: "+this.tick;
		}
		if(rodando != "" && listaProcesso.size() != 0) {
			resultado += "Processos: {Rodando: " + rodando + ", Fila: " + Arrays.toString(fila) + "};"
					+ "Quantum: " + this.quantm + ";"
					+ "Tick: " + this.tick;
		}
		
		return resultado;
		//Processo
		//Rodando
		//Fila
		//Bloqueado
		//tick
		//Quant
		
		
		
		/*if (this.listaProcesso.size() != 0 && tempo != listaProcesso.get(0)) {
			return ("Escalonador " + this.tipoEscalonador + ";" 
		+ "Processos: {Fila: " + this.listaProcesso.toString() + "};" 
		+ "Quantum: " + this.quantm + ";" 
		+ "Tick: " + this.tick);			
		} else {
			return ("Escalonador " + this.tipoEscalonador + ";" 
			+ "Processos: {" + this.rodando + "};"
			+ "Quantum: " + this.quantm + ";" 
				+ "Tick: " + this.tick);
		}*/
	}

	public void tick() {
		this.tick++;
		if(this.listaProcesso.size() != 0){
			this.rodando = this.listaProcesso.get(0);
			tempo = listaProcesso.get(0);
		
		}else {
			this.rodando = "";
			this.tempo = "";
		}
	}


	public void adicionarProcesso(String nomeProcesso) {
		if(rodando == "" && this.tick > this.quantm) {
			listaProcesso.add(nomeProcesso);
		}	
		rodando = nomeProcesso;
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
