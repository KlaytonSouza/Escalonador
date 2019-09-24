package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FachadaEscalonador {

	private TipoEscalonador tipoEscalonador;
	private int quantum;
	private int tick;
	private int controlador;
	private Queue<String> listaProcesso;
	private ArrayList<String> processoBloqueado;
	private String rodando;
	private String processoParaSerFinalizado;
	private String processoParaSerBloqueado;
	private String processoParaSerRetomado;

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.tick = 0;
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new ArrayList<String>();

	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
		this.quantum = quantum;
		this.tick = 0;
		this.tipoEscalonador = roundrobin;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new ArrayList<String>();
//this.temp = new ArrayList<String>();

	}

	public String getStatus() {

		String resultado = "";

		resultado += "Escalonador " + this.tipoEscalonador + ";";

		resultado += "Processos: {";

		if (rodando != null) {
			resultado += "Rodando: " + this.rodando;

		}
		if (listaProcesso.size() > 0) {
			if (rodando != null) {
				resultado += ", ";
			}
			resultado += "Fila: " + this.listaProcesso.toString();

		}
		if (processoBloqueado.size() > 0)/* && listaProcesso.size()>0 ) */ {
			if (listaProcesso.size() > 0) {
				resultado += ", Bloqueados: " + this.processoBloqueado.toString();
			} else {
				resultado += "Bloqueados: " + this.processoBloqueado.toString();
			}
		}

		resultado += "};Quantum: " + this.quantum + ";";

		resultado += "Tick: " + this.tick;

		return resultado;
	}

	public TipoEscalonador getTipoEscalonador() {
		return tipoEscalonador;
	}

	public void setTipoEscalonador(TipoEscalonador tipoEscalonador) {
		this.tipoEscalonador = tipoEscalonador;
	}

	
	public void tick() {
		this.tick++;
		if (this.controlador > 0 && (this.controlador + this.quantum) == this.tick) {
			this.listaProcesso.add(rodando);
			this.rodando = this.listaProcesso.poll();
			this.controlador = this.tick;
		}

		if (processoParaSerFinalizado != null) {
			if (this.rodando == this.processoParaSerFinalizado) {
				this.rodando = null;

			} else {
				this.listaProcesso.remove(processoParaSerFinalizado);
			}

		}

		if (this.rodando == null) {
			if (this.listaProcesso.size() != 0) {
				this.rodando = this.listaProcesso.poll();
				if (listaProcesso.size() > 0) {
					this.controlador = this.tick;
				}

			}
		}
		if (this.controlador == 0 && this.rodando != null && listaProcesso.size() > 0) {
			this.controlador = this.tick;
		}

		if (processoParaSerBloqueado != null) {
			if (this.rodando == this.processoParaSerBloqueado) {
				this.rodando = null;
				this.processoBloqueado.add(processoParaSerBloqueado);
				if (listaProcesso.size() > 0) {
					rodando = listaProcesso.poll();
				} else {
					rodando = null;
				}
			} else {
				this.listaProcesso.remove(processoParaSerBloqueado);
				this.processoBloqueado.add(processoParaSerBloqueado);
			}
			processoParaSerBloqueado = null;
		}

		if (processoParaSerRetomado != null) {
			for (int k = 0; k < processoBloqueado.size(); k++) {
				if (processoBloqueado.get(k) == this.processoParaSerRetomado) {
					this.processoBloqueado.remove(k);
					this.listaProcesso.add(processoParaSerRetomado);
				}

			}
		}

	}

	public void adicionarProcesso(String nomeProcesso) {
		if (listaProcesso.contains(nomeProcesso)) {
			throw new EscalonadorException();
		}
		this.listaProcesso.add(nomeProcesso);

	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		if (listaProcesso.contains(nomeProcesso)) {
			throw new EscalonadorException();
		}
		this.listaProcesso.add(nomeProcesso);
		
	}

	public void finalizarProcesso(String nomeProcesso) {

		this.processoParaSerFinalizado = nomeProcesso;

	}

	public void bloquearProcesso(String nomeProcesso) {
		this.processoParaSerBloqueado = nomeProcesso;
	}

	public void retomarProcesso(String nomeProcesso) {
		this.processoParaSerRetomado = nomeProcesso;

	}
}
