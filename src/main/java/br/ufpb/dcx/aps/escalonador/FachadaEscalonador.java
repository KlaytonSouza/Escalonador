package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FachadaEscalonador {

	private int tick;

	private int controlador;
	private Queue<String> listaProcesso;
	private ArrayList<String> processoBloqueado;

	private List<String> fila = new ArrayList<String>();
	private String rodando;
	private List<Integer> filaDuracao = new ArrayList<Integer>();
	private int duracaoRodando;
	private int duracaoFixa;

	private int quantum;

	private TipoEscalonador tipoEscalonador;

	private String processoParaSerFinalizado;

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.tick = 0;
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new ArrayList<String>();

	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
	}

	public String getStatus() {

		// MaisCurtoPrimeiro;Processos: {Fila: [P1]};Quantum: 0;Tick: 0

		if (fila.size() == 0) {
			if (rodando != null) {
				return "Escalonador MaisCurtoPrimeiro;Processos: {Rodando: " + rodando + "};Quantum: 0;Tick: " + tick;
			}
			return "Escalonador MaisCurtoPrimeiro;Processos: {};Quantum: 0;Tick: " + tick;
		} else if (rodando != null && fila.size() > 0) {

			return "Escalonador MaisCurtoPrimeiro;Processos: {Rodando: " + rodando + ", Fila: " + fila
					+ "};Quantum: 0;Tick: " + tick;

		}
		return "Escalonador MaisCurtoPrimeiro;Processos: {Fila: " + fila + "};Quantum: 0;Tick: " + tick;

	}

	public TipoEscalonador getTipoEscalonador() {
		return tipoEscalonador;

	}

	public void setTipoEscalonador(TipoEscalonador tipoEscalonador) {
		this.tipoEscalonador = tipoEscalonador;
	}

	public void tick() {
		tick++;
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

	public void adicionarProcesso(String nomeProcesso) {

		
		this.listaProcesso.add(nomeProcesso);

		if (listaProcesso.contains(nomeProcesso)) {
			throw new EscalonadorException();
		}else {
			throw new EscalonadorException();
		}	
		
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
	}

	public void retomarProcesso(String nomeProcesso) {

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
}