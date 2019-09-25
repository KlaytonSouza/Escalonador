package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;

public class FachadaEscalonador {

	private int tick;
	private List<String> fila = new ArrayList<String>();
	private String rodando;
	private List<Integer> filaDuracao = new ArrayList<Integer>();
	private int duracaoRodando;
	private int duracaoFixa;

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
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
		} else {
			return "Escalonador MaisCurtoPrimeiro;Processos: {Fila: " + fila + "};Quantum: 0;Tick: " + tick;
		}
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
			}
			else {
				rodando = null;
				duracaoRodando = 0;
			}
			if(duracaoRodando > 0) {
				duracaoFixa = tick + duracaoRodando;
				
			}
		}
	}

	public void adicionarProcesso(String nomeProcesso) {

	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
	}

	public void finalizarProcesso(String nomeProcesso) {
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {

	}

	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao) {
		
		if(fila.size()==0) {
			fila.add(nomeProcesso);
			filaDuracao.add(duracao);
		}
		else {
			
		}
	}
}
