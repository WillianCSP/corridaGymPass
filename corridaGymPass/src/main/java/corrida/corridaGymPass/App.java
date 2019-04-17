package corrida.corridaGymPass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import corrida.corridaGymPass.model.Piloto;
import corrida.corridaGymPass.model.Resultado;
import corrida.corridaGymPass.model.Volta;

public class App {
	/**
	 * 
	 * 
	 * Hora Piloto Nº Volta Tempo Volta Velocidade média da volta
	 * 
	 * Posição Chegada, Código Piloto, Nome Piloto, Qtde Voltas Completadas e Tempo Total de Prova. - OK
	 * Descobrir a melhor volta de cada piloto - OK
	 * Descobrir a melhor volta da corrida - OK
	 * Calcular a velocidade média de cada piloto durante toda corrida -OK
	 * Descobrir quanto tempo cada piloto chegou após o vencedor-OK
	 * 
	 */

	private static String arquivo;
	private static String arquivoResultado;
	private static String charset;
	private static List<Piloto> listaPiloto = new ArrayList<Piloto>();
	private static List<Volta> listaVoltas = new ArrayList<Volta>();
	
	private static List<Resultado> resultadoFinalCorrida = new ArrayList<Resultado>();
	private static List<Volta> melhorVoltaPorPiloto = new ArrayList<Volta>();
	private static Volta melhorVoltaCorrida;
	private static List<String> mediaVelocidadePorPiloto = new ArrayList<String>();
	
	
	public static void main(String[] args) throws Exception {

		lerArquivoCorrida();
		setResultadoFinalCorrida(calculaTempoTotalCorridaPorPiloto());
		setMelhorVoltaPorPiloto(calculaMelhorVoltaPorPiloto());
		setMelhorVoltaCorrida(calculaMelhorVoltaCorrida());
		setMediaVelocidadePorPiloto(calculaVelocidadeMediaPorPiloto());
		gravaResultadoFinalArquivo();
		
	}
		

	/**
	 * Metodo responsável por ler o arquivo de log com os dados da corrida.
	 * 
	 * @return List 
	 * @throws IOException
	 */
	private static void lerArquivoCorrida() throws IOException {

		
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
		carregaArquivoProperties();

		Scanner scanner = new Scanner(new File(arquivo), charset);
		scanner.nextLine();//pula cabeçalho

		

		while (scanner.hasNextLine()) {
			
			//Em cada linha substitui os espaços pelo ;
			String linha = scanner.nextLine().replaceAll("\\s{2,}", ";");

			Scanner linhaScanner = new Scanner(linha);
			linhaScanner.useDelimiter(";");

			LocalTime data = LocalTime.parse(linhaScanner.next());
			String codigoNomePiloto = linhaScanner.next();
			int[] codigoPiloto = Arrays.stream(codigoNomePiloto.split("[^\\d]+")).mapToInt(Integer::parseInt).toArray();//mapeia apenas numeros para pegar cod. piloto
			Piloto piloto = new Piloto(codigoPiloto[0], codigoNomePiloto.replaceAll("[^A-Za-z.]", ""));//mapeia letras para pegar nome piloto
			registraPiloto(piloto);
			int numeroVolta = linhaScanner.nextInt();
			LocalTime tempoVolta = LocalTime.parse(String.format("00:0%s", linhaScanner.next()), formatador);//ajusta formato para HH:mm:ss.sss
			double velocidadeMedia = linhaScanner.nextDouble();

			linhaScanner.close();
						
			Volta volta = new Volta(data, piloto, numeroVolta, tempoVolta, velocidadeMedia);
			listaVoltas.add(volta);

		}
		 
		scanner.close();

	}
	
	/**
	 * Método responsável por gravar o resultado das tarefas em arquivo .txt
	 * 
	 * @throws IOException
	 */
	private static void gravaResultadoFinalArquivo() throws IOException {
		// TODO Auto-generated method stub
		
		PrintWriter ps = new PrintWriter(arquivoResultado,charset);
		ps.println("***********  RESULTADO FINAL  ***************");
		getResultadoFinalCorrida().stream().forEach(ps::println);
		ps.print("\n\n");
		ps.println("***********  MELHOR VOLTA POR PILOTO  ***************");
		getMelhorVoltaPorPiloto().stream().forEach(ps::println);
		ps.print("\n\n");
		ps.println("***********  MELHOR VOLTA da CORRIDA  ***************");
		ps.println(getMelhorVoltaCorrida());
		ps.print("\n\n");
		ps.println("***********  VELOCIDADE MEDIA POR PILOTO  ***************");
		getMediaVelocidadePorPiloto().stream().forEach(ps::println);
		ps.close();
		
	}

	/**
	 * 
	 * Calcula melhor volta da corrida
	 * 
	 * @return Volta
	 */
	private static Volta calculaMelhorVoltaCorrida() {
		Volta melhorVolta = listaVoltas.stream().sorted(Comparator.comparing(Volta::getTempoVolta)).findFirst().get();
		
		System.out.println("***********************Melhor Volta da Corrida****************************************");
		System.out.println(melhorVolta);
		
		return melhorVolta;
	}
	
	/**
	 * Registra cada piloto participante
	 * 
	 * @param piloto
	 */
	private static void registraPiloto(Piloto piloto) {
		boolean pilotoJaRegistrado = listaPiloto.stream().filter(c -> c.getCodigo() == piloto.getCodigo()).findAny().isPresent();
		if (!pilotoJaRegistrado)
			listaPiloto.add(piloto);
	}

	/**
	 * Metodo responsável por calcular o tempo total da corrida por piloto e retornar em ordem crescente de posição final
	 * 
	 * @return List
	 */
	private static List<Resultado> calculaTempoTotalCorridaPorPiloto() {

		Resultado resultadoFinalPiloto = null;
		
		List<Resultado> resultadoParcialPilotosFinal = new ArrayList<>();
	
		for (Piloto piloto : listaPiloto) {

			LocalTime somaVoltas =LocalTime.of(0, 0, 0, 0);
			//para cada piloto, recupera a lista de voltas completadas
			List<Volta> listaVoltaDoPiloto = listaVoltas.stream().filter(v -> v.getPiloto().getCodigo() == piloto.getCodigo())
														   .collect(Collectors.toList());

			System.out.println("Piloto:" + piloto.getNome());
			
			
			for (Volta voltaPiloto : listaVoltaDoPiloto) {
				
				System.out.println("Volta:"+voltaPiloto.getNumeroVolta()+" -> "+voltaPiloto.getTempoVolta());	
				
				somaVoltas =somaVoltas.plusHours(voltaPiloto.getTempoVolta().getHour())
										.plusMinutes(voltaPiloto.getTempoVolta().getMinute())
										.plusSeconds(voltaPiloto.getTempoVolta().getSecond())
										.plus(voltaPiloto.getTempoVolta().getNano() / 1000000, ChronoUnit.MILLIS);				
														
			}
			
			//computa o total de tempo de cada piloto para completar a prova
			resultadoFinalPiloto = new Resultado(0, piloto, listaVoltaDoPiloto.size(), somaVoltas, LocalTime.MIN);
			resultadoParcialPilotosFinal.add(resultadoFinalPiloto);//nao ordenado

		}
		
		//lista final ordenada do líer ao último
		List<Resultado> resultadoFinalPilotosOrdenado = resultadoParcialPilotosFinal.stream()
				.sorted(Comparator.comparing(Resultado::getTempoTotalCorrida))
													.collect(Collectors.toList());
		
		
		calculaDiferencaTempoParaLider(resultadoFinalPilotosOrdenado);
		
		System.out.println("================RESULTADO===================");
		resultadoFinalPilotosOrdenado.stream().forEach(System.out::println);
		System.out.println("========================================");
		
		return resultadoFinalPilotosOrdenado;
		
	}
	
	
	/**
	 * 
	 * Método responsável por calcular a diferença de tempo final de cada piloto para o lider
	 * 
	 * 
	 * @param resultadoFinalPilotosOrdenado
	 */
	private static void calculaDiferencaTempoParaLider(List<Resultado> resultadoFinalPilotosOrdenado) {
		// TODO Auto-generated method stub
		LocalTime tempoTotalLider = resultadoFinalPilotosOrdenado.get(0).getTempoTotalCorrida();
		
		for(int i = 0; i< resultadoFinalPilotosOrdenado.size();i++){
			
			Resultado resultadoPiloto = resultadoFinalPilotosOrdenado.get(i);
			resultadoPiloto.setPosicaoChegada(i+1);
			
			resultadoPiloto.setDiferencaParaOPrimeiro(resultadoPiloto.getTempoTotalCorrida().minusHours(tempoTotalLider.getHour())
																	 .minusMinutes(tempoTotalLider.getMinute())
																	 .minusSeconds(tempoTotalLider.getSecond())
																	 .minus(tempoTotalLider.getNano() / 1000000, ChronoUnit.MILLIS));
		}	
		
	}
	
	
	/**
	 * Metodo responsavel por calcular a melhor volta de cada piloto
	 * @return List
	 * 
	 * 
	 */
	private static List<Volta> calculaMelhorVoltaPorPiloto(){
		
		List<Volta> listaMelhorVoltaPorPiloto = new ArrayList<>();
		
		for (Piloto piloto : listaPiloto) {
		
			Volta melhorVoltaPorPiloto = listaVoltas.stream().filter(v -> v.getPiloto().getCodigo() == piloto.getCodigo())
					.sorted(Comparator.comparing(Volta::getTempoVolta)).findFirst().get();
			listaMelhorVoltaPorPiloto.add(melhorVoltaPorPiloto);
		}
		
		System.out.println("***********************Melhor volta por piloto****************************************");
		listaMelhorVoltaPorPiloto.stream().forEach(System.out::println);
		
		return listaMelhorVoltaPorPiloto;
		
	}
	
	/**
	 * Metodo responsavel por calcular a velocidade media de cada piloto
	 * 
	 * @return List
	 */
	private static List<String> calculaVelocidadeMediaPorPiloto(){
		
		List<String> mediaPorPiloto = new ArrayList<>();

		for (Piloto piloto : listaPiloto) {
			
			DoubleStream dbStream = listaVoltas.stream().filter(v -> v.getPiloto().getCodigo() == piloto.getCodigo())
					.mapToDouble(v -> v.getVelocidadeMedia());
		
			Double media = dbStream.average().getAsDouble();
			
			mediaPorPiloto.add("Media "+piloto.getNome()+":"+media);
		
		}
		
		System.out.println("***********************Velocidade Media por Piloto****************************************");
		mediaPorPiloto.stream().forEach(System.out::println);
		
		return mediaPorPiloto;
	}
	
	/**
	 * Metodo responsavel por recuparar do .properties os arquivos a sere usados no processo
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void carregaArquivoProperties() throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileReader("conf.properties"));
		
		arquivo = props.getProperty("arquivo_log_inicial");
		arquivoResultado = props.getProperty("arquivo_resultado_final");
		charset = props.getProperty("charset");
		
		System.out.println("Arquivo a ser lido: " + arquivo + ", Charset selecionado: " + charset);
	}

	
	
	
	public static List<Resultado> getResultadoFinalCorrida() {
		return resultadoFinalCorrida;
	}

	public static void setResultadoFinalCorrida(List<Resultado> resultadoFinalCorrida) {
		App.resultadoFinalCorrida = resultadoFinalCorrida;
	}

	public static Volta getMelhorVoltaCorrida() {
		return melhorVoltaCorrida;
	}

	public static void setMelhorVoltaCorrida(Volta melhorVoltaCorrida) {
		App.melhorVoltaCorrida = melhorVoltaCorrida;
	}

	public static List<Volta> getMelhorVoltaPorPiloto() {
		return melhorVoltaPorPiloto;
	}

	public static void setMelhorVoltaPorPiloto(List<Volta> melhorVoltaPorPiloto) {
		App.melhorVoltaPorPiloto = melhorVoltaPorPiloto;
	}

	public static List<String> getMediaVelocidadePorPiloto() {
		return mediaVelocidadePorPiloto;
	}

	public static void setMediaVelocidadePorPiloto(List<String> mediaVelocidadePorPiloto) {
		App.mediaVelocidadePorPiloto = mediaVelocidadePorPiloto;
	}

}
