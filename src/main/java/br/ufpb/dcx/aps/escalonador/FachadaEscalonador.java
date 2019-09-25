package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FachadaEscalonador {

	private TipoEscalonador tipoEscalonador;

	private int quantum;
	private int tick;
	private int controlador;
	private int duracaoRodando;
	private int duracaoFixa;

	private Queue<String> listaProcesso;
	private ArrayList<String> processoBloqueado;
	private List<String> fila = new ArrayList<String>();
	private List<Integer> filaDuracao = new ArrayList<Integer>();

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

	}

	public String getStatus() {

		if (this.tipoEscalonador.equals(escalonadorMaisCurtoPrimeiro())) {
			if (fila.size() == 0) {
				if (rodando != null) {
					return "Escalonador MaisCurtoPrimeiro;Processos: {Rodando: " + rodando + "};Quantum: 0;Tick: "
							+ tick;
				}
				return "Escalonador MaisCurtoPrimeiro;Processos: {};Quantum: 0;Tick: " + tick;
			} else if (rodando != null && fila.size() > 0) {

				return "Escalonador MaisCurtoPrimeiro;Processos: {Rodando: " + rodando + ", Fila: " + fila
						+ "};Quantum: 0;Tick: " + tick;

			}
			return "Escalonador MaisCurtoPrimeiro;Processos: {Fila: " + fila + "};Quantum: 0;Tick: " + tick;
		}

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
		if (processoBloqueado.size() > 0) {
			resultado += ", Bloqueados: " + this.processoBloqueado.toString();
		}
		resultado += "};Quantum: " + this.quantum + ";";

		resultado += "Tick: " + this.tick;

		return resultado;
	}

	public void tick() {
		this.tick++;
		if (this.tipoEscalonador.equals(escalonadorMaisCurtoPrimeiro())) {
			if (fila.size() > 0) {
				if (rodando == null) {
					rodando = fila.remove(0);
					duracaoRodando = filaDuracao.remove(0);
					duracaoFixa = this.tick + duracaoRodando;
				}

			}
			if (duracaoFixa == this.tick && rodando != null) {
				if (fila.size() > 0) {
					rodando = fila.remove(0);
					duracaoRodando = filaDuracao.remove(0);
				} else {
					rodando = null;
					duracaoRodando = 0;
				}
				if (duracaoRodando > 0) {
					duracaoFixa = tick + duracaoRodando;

				}
			}
		}		
		
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
		this.listaProcesso.add(nomeProcesso);

	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
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
	
	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao) {

		if (fila.contains(nomeProcesso) || nomeProcesso == null) {
			throw new EscalonadorException();

		}
		if (duracao < 1) {
			throw new EscalonadorException();
		}
		int maisCurto = Integer.MAX_VALUE;

		if (fila.size() == 0) {
			fila.add(nomeProcesso);
			filaDuracao.add(duracao);
		} else {

			int menorPosicao = 0;

			fila.add(nomeProcesso);
			filaDuracao.add(duracao);
			for (int i = 0; i < filaDuracao.size(); i++) {
				if (filaDuracao.get(i) < maisCurto) {
					maisCurto = filaDuracao.get(i);
					menorPosicao = i;
				}

			}
			if (menorPosicao > 0) {
				String processoMenor = fila.remove(menorPosicao);
				int processoMenorTempo = filaDuracao.remove(menorPosicao);
				fila.add(0, processoMenor);
				filaDuracao.add(0, processoMenorTempo);

				for (int k = 0; duracao < k; k++) {
					fila.add(0, nomeProcesso);
					filaDuracao.add(0, duracao);

				}
			}

		}
	}

	public TipoEscalonador escalonadorRoundRobin() {
		return TipoEscalonador.RoundRobin;
	}

	public TipoEscalonador escalonadorPrioridade() {
		return TipoEscalonador.Prioridade;
	}

	public TipoEscalonador escalonadorMaisCurtoPrimeiro() {
		return TipoEscalonador.MaisCurtoPrimeiro;
	}

	public TipoEscalonador getTipoEscalonador() {
		return tipoEscalonador;

	}

	public void setTipoEscalonador(TipoEscalonador tipoEscalonador) {
		this.tipoEscalonador = tipoEscalonador;
	}
	

}
