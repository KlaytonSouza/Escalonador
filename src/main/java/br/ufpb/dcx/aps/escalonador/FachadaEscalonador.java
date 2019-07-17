package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;

public class FachadaEscalonador {

	private int quantm;
	private int tick;
	private TipoEscalonador tipoEscalonador;
	private ArrayList<String> listaProcesso;
	private String rodando;
	private ArrayList<String> processoBloqueado;
	private String klayton = "";

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
		if (this.listaProcesso.size() == 0) {
			return ("Escalonador " + this.tipoEscalonador + ";" + "Processos: {" + this.rodando + klayton +"};"
					+ "Quantum: " + this.quantm + ";" + "Tick: " + this.tick);
		} else {
			return ("Escalonador " + this.tipoEscalonador + ";" + "Processos: {Fila: " + this.listaProcesso.toString()
					+ "};" + "Quantum: " + this.quantm + ";" + "Tick: " + this.tick);
		}
	}

	public void tick() {
		this.tick++;
		if(this.listaProcesso.size() != 0) {
			this.rodando = "Rodando: "+this.listaProcesso.get(0);
			this.listaProcesso.remove(0);
		}
	}

	public void adicionarProcesso(String nomeProcesso) {
		listaProcesso.add(nomeProcesso);
	}

	public void finalizarProcesso(String nomeProcesso) {
		boolean testeProcesso = true;
		
		for(String x: this.listaProcesso) {
			if(x == nomeProcesso) {
				testeProcesso = false;
				this.klayton = x;
				this.listaProcesso.remove(x);
			}
		}if(testeProcesso)
			this.rodando = "Rodando: ";
		
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {

	}

}
