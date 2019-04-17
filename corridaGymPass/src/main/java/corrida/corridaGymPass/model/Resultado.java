package corrida.corridaGymPass.model;

import java.time.LocalTime;

/**
 * 
 * 
 * Entidade que representa o resultado final da corrida.
 * 
 * 
 * @author Willian
 *
 */

public class Resultado {

	public Resultado(int posicaoChegada, Piloto piloto, int qtdeVoltasCompletadas, LocalTime tempoTotalCorrida,
			LocalTime diferencaParaOPrimeiro) {
		super();
		this.posicaoChegada = posicaoChegada;
		this.piloto = piloto;
		this.qtdeVoltasCompletadas = qtdeVoltasCompletadas;
		this.tempoTotalCorrida = tempoTotalCorrida;
		this.diferencaParaOPrimeiro = diferencaParaOPrimeiro;
	}



	private int posicaoChegada;
	private Piloto piloto;
	private int qtdeVoltasCompletadas;
	private LocalTime tempoTotalCorrida;
	private LocalTime diferencaParaOPrimeiro;
	
	
	public LocalTime getDiferencaParaOPrimeiro() {
		return diferencaParaOPrimeiro;
	}
	public void setDiferencaParaOPrimeiro(LocalTime diferencaParaOPrimeiro) {
		this.diferencaParaOPrimeiro = diferencaParaOPrimeiro;
	}
	public int getPosicaoChegada() {
		return posicaoChegada;
	}
	public void setPosicaoChegada(int posicaoChegada) {
		this.posicaoChegada = posicaoChegada;
	}
	public Piloto getPiloto() {
		return piloto;
	}
	public void setPiloto(Piloto piloto) {
		this.piloto = piloto;
	}
	public int getQtdeVoltasCompletadas() {
		return qtdeVoltasCompletadas;
	}
	public void setQtdeVoltasCompletadas(int qtdeVoltasCompletadas) {
		this.qtdeVoltasCompletadas = qtdeVoltasCompletadas;
	}
	public LocalTime getTempoTotalCorrida() {
		return tempoTotalCorrida;
	}
	public void setTempoTotalCorrida(LocalTime tempoTotalCorrida) {
		this.tempoTotalCorrida = tempoTotalCorrida;
	}
	
	@Override
	public String toString() {
		return "posicao=" + posicaoChegada + ", " + piloto + ", VoltasCompletadas="
				+ qtdeVoltasCompletadas + ", tempoTotalCorrida=" + tempoTotalCorrida + ", diferencaParaOPrimeiro="
				+ diferencaParaOPrimeiro;
	}
	
	
	
	
	
	
	

}
