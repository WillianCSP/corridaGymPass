package corrida.corridaGymPass.model;

import java.time.LocalTime;

/**
 * Objeto que representa o formato do arquivo lido com as informacoes da corrida.
 * 
 * @author Willian
 */
public class Volta{
	
	
	public Volta(LocalTime data, Piloto piloto, int numeroVolta, LocalTime tempoVolta, double velocidadeMedia) {
		super();
		this.data = data;
		this.piloto = piloto;
		this.numeroVolta = numeroVolta;
		this.tempoVolta = tempoVolta;
		this.velocidadeMedia = velocidadeMedia;
	}

	private LocalTime data;
	
	private Piloto piloto;
	
	private int numeroVolta;
	
	private LocalTime tempoVolta;
	
	private double velocidadeMedia;
	
	public LocalTime getData() {
		return data;
	}
	public void setData(LocalTime data) {
		this.data = data;
	}
	public Piloto getPiloto() {
		return piloto;
	}
	public void setPiloto(Piloto piloto) {
		this.piloto = piloto;
	}
	public int getNumeroVolta() {
		return numeroVolta;
	}
	public void setNumeroVolta(int numeroVolta) {
		this.numeroVolta = numeroVolta;
	}
	public LocalTime getTempoVolta() {
		return tempoVolta;
	}
	public void setTempoVolta(LocalTime tempoVolta) {
		this.tempoVolta = tempoVolta;
		
	}
	public double getVelocidadeMedia() {
		return velocidadeMedia;
	}
	public void setVelocidadeMedia(double velocidadeMedia) {
		this.velocidadeMedia = velocidadeMedia;
	}

	@Override
	public String toString() {
		return "data=" + data + ", " + piloto + ", volta=" + numeroVolta + ", tempo="
				+ tempoVolta + ", velocidadeMedia=" + velocidadeMedia + "";
	}
	
}