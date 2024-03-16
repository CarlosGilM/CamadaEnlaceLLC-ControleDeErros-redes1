/* ***************************************************************
* Autor............: Carlos Gil Martins da Silva
* Matricula........: 202110261
* Inicio...........: 31/08/2023
* Ultima alteracao.: 05/09/2023
* Nome.............: Receptor
* Funcao...........: Recebe os Bits manipulados e
descriptografa, fazendo o processo inverso do transmissor
até chegar na mensagem enviada pelo usuario
****************************************************************/
package model;
import control.controllerPrincipal;

public class Receptor {
	controllerPrincipal cG = new controllerPrincipal(); // Instanciando e Criando o Controller
	int caracteresAnterior;
	// Metodo Utilizado para Setar um Controlador em Comum em Todas Thread
	public void setControlador(controllerPrincipal controle) {
	  this.cG = controle;
	}
	void CamadaFisicaReceptora(int quadro[]) {
		int tipoDeDecodificacao = cG.getCodificacao(); // alterar de acordo o teste
		int[] fluxoBrutoDeBits = new int[cG.setTamanhoArray(cG.getCodificacao())]; // ATENcaO: trabalhar com BITS!!!
		switch (tipoDeDecodificacao) {
			case 0: // codificao binaria
				fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoBinaria(quadro);
				break;
			case 1: // codificacao manchester
				fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchester(quadro);
				break;
			case 2: // codificacao manchester diferencial
				fluxoBrutoDeBits = CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
				break;
		}// fim do switch/case
		cG.getEc().CamadaEnlaceDadosReceptora(fluxoBrutoDeBits);
	}// fim do metodo CamadaFisicaTransmissora

	public int[] CamadaFisicaReceptoraDecodificacaoBinaria(int quadro[]) {
		// Ja está em binario, não havendo necessidade de decodificar o Arrray
		return quadro;
	}// fim do metodo CamadaFisicaReceptoraDecodificacaoBinaria

	public int[] CamadaFisicaReceptoraDecodificacaoManchester(int quadro[]) {
		//Criando Novo Tamanho do Array
		int quadroDescrip[];
		int tamanhoFOR;
		if (cG.getEnquadramento() == 3) {
			quadro = DecodificacaoViolacaoCamadaFisica(quadro);
			int tamanhoArray;
			int ContagemBits = cG.getQtdBitsInsercaoBits();
			if (ContagemBits % 32 == 0)
			tamanhoArray = (ContagemBits / 32);
			else
			tamanhoArray = ((ContagemBits / 32) + 1);

			if (ContagemBits % 8 == 0)
			tamanhoFOR = (ContagemBits / 8);
			else
			tamanhoFOR = ((ContagemBits / 8) + 1);

			quadroDescrip = new int[tamanhoArray];
		}
		else{
			tamanhoFOR = cG.getNumCaracteres();
			quadroDescrip = new int[cG.setTamanhoArray(cG.getCodificacao())];
		}

		int deslocBit = 0; // Variavel define qual bit vai deslocar para manipulação
		int deslocBitDescript = 0; // Variavel define qual bit vai deslocar para manipulação 

		for (int i = 0; i < tamanhoFOR; i++) { // For ate o tamanho da Mensagem
			if (deslocBit == 32) { // verifica o ultimo bit de cada posicao do array
				deslocBit = 0;
			}
			for (int t = 0; t < 8; t++) { // for para cada Caractere
				StringBuilder parBit = new StringBuilder();
				for (int j = 0; j < 2; j++) { // For para cada Par de Bit
					int mascara = 1 << deslocBit; // Mascara com bit 1 na Posicao deslocBit
					int Bit = (quadro[i / 2] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro na posicao	desloc bit
					if (Bit == -1) {
						Bit = Bit * -1;
					}
					parBit.insert(0, Bit); // Insere o Bit no parBit que será um par de Bits
					deslocBit++;
				}
				if (parBit.toString().equals("10")) { // Verifica se o par de Bits eh 10
					quadroDescrip[i / 4] = quadroDescrip[i / 4] | (1 << (deslocBitDescript)); // Insere o Bit 1 na posicao deslocBit 																																							
				}
				deslocBitDescript++;
			}
		} // Fim for msg
		if(cG.getEnquadramento()!= 3)
			cG.setQtdBitsInsercaoBits(cG.getQtdBitsInsercaoBits()/2);
		return quadroDescrip;
	}// fim do metodo CamadaFisicaReceptoraDecodificacaoManchester

	public int[] CamadaFisicaReceptoraDecodificacaoManchesterDiferencial(int quadro[]) {
		int quadroDescrip[];
		int tamanhoFOR;
		if (cG.getEnquadramento() == 3) {
			quadro = DecodificacaoViolacaoCamadaFisica(quadro);
			int tamanhoArray;
			int ContagemBits = cG.getQtdBitsInsercaoBits();
			if (ContagemBits % 32 == 0)
			tamanhoArray = (ContagemBits / 32);
			else
			tamanhoArray = ((ContagemBits / 32) + 1);

			if (ContagemBits % 8 == 0)
			tamanhoFOR = (ContagemBits / 8);
			else
			tamanhoFOR = ((ContagemBits / 8) + 1);

			quadroDescrip = new int[tamanhoArray];
		}
		else{
			tamanhoFOR = cG.getNumCaracteres();
			quadroDescrip = new int[cG.setTamanhoArray(cG.getCodificacao())];
		}
		//System.out.println("Quadro Desenquadradado: " + cG.ExibirManchester(quadro));
		int deslocBit =0; // Variavel define qual bit vai deslocar para manipulação
		StringBuilder fluxoFinal = new StringBuilder();

		for (int i = 0; i < tamanhoFOR; i++) { // For ate o tamanho da Mensagem
			String bitComparation = "10"; // Bit de comparacao inicial
			boolean verifyComparation = true; // varival de controle para comparacao

			if(i % 2== 0){ // alterna o valor do deslocamento entre 15 ( ultimo bit da primeiro caractere da posicao)
				deslocBit = 15;
			}
			else{ // alterna o valor do deslocamento entre 31 ( ultimo bit da segunda caractere da posicao)
				deslocBit = 31;
			}
			StringBuilder fluxoAux = new StringBuilder();
			for (int t = 0; t < 8; t++) { // for para cada caractere
				StringBuilder parBit = new StringBuilder();
				for (int j = 0; j < 2; j++) { // For para cada par de Bits
					int mascara = 1 << deslocBit; // Mascara com bit 1 na Posicao deslocBit
					int Bit = (quadro[i / 2] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro na posicao eslocBit																
					if (Bit == -1) {
						Bit = Bit * -1;
					}
					parBit.append(Bit); // Insere o Bit no parBit que sera um par de Bits
					deslocBit--;
				}

				if(parBit.toString().equals(bitComparation)){ // Verifica se deve ser inserido ou nao o Bit 1
					if(verifyComparation == true){
						fluxoAux.append(1);
						verifyComparation = false;
						bitComparation = parBit.toString();	
					}
					else{
						fluxoAux.append(0);
					}
				}
				else{
					if(verifyComparation == false){
						fluxoAux.append(1);
						bitComparation = parBit.toString();	
					}
					else{
						fluxoAux.append(0);
					}
				}
			}
			fluxoAux.reverse();
			fluxoFinal.append(fluxoAux.toString());	
		} // Fim for msg

		for (int i = 0; i < fluxoFinal.length(); i++) {
			// Estrutura de IF que manipula bit por Bit
			int deslocamento = i%32;
			if (fluxoFinal.charAt(i) == '1') {
				// define o bit na posicaoo deslocBit do quadro[indexQuadro] como 1
				quadroDescrip[i/32] = quadroDescrip[i/32] | (1 << deslocamento);
			}
		}
		if(cG.getEnquadramento()!= 3)
			cG.setQtdBitsInsercaoBits(cG.getQtdBitsInsercaoBits()/2); 
		return quadroDescrip;
	}// fim do CamadaFisicaReceptoraDecodificacaoManchesterDiferencial

	public int[] DecodificacaoViolacaoCamadaFisica(int[] quadro) {
		int tamanhoArray;
		int ContagemBits = cG.getQtdBitsInsercaoBits() - cG.getFlagsViolacaoFisica();
		if (ContagemBits % 32 == 0)
		tamanhoArray = (ContagemBits / 32);
		else
		tamanhoArray = ((ContagemBits / 32) + 1);
		
		// Criando Novo quadro com o novo tamanho
		int quadroEnquadrado[] = new int[tamanhoArray]; // Novo Array Quadro
		int deslocBit = (ContagemBits) -1;
		int auxiliar = 0;

		int qtdBitsTotais = cG.getBitsTotaisViolacaoCamadaFisica() -1; // Se Forem 32 bits, percorre do 31 a 0 (Ou seja, um a menos)
		//String Flag = "11"; // flag inicial e final pois eh um par de sinais 11 nao utilizado
		// nas codificacoes Manchester e Manchester diferencial, sendo essa a flag imposta pelo
		// metodo de violacao da camada fisica
  
		// For até o tamanho da Mensagem
		for (int i = qtdBitsTotais; i >= 0; i--) {
		  qtdBitsTotais = i % 32;
		  // Quadros de Tamanho 3
				StringBuilder parBit = new StringBuilder();
				for (int j = 0; j < 2; j++) { // For para cada par de Bits
					int mascara = 1 << qtdBitsTotais; // Mascara com bit 1 na Posicao deslocBit
					int Bit = (quadro[i / 32] & mascara) >> qtdBitsTotais; // Pega o Bit na posicao da Mascara&Quadro na posicao eslocBit																
					if (Bit == -1) {
						Bit = Bit * -1;
					}
					parBit.append(Bit); // Insere o Bit no parBit que sera um par de Bits
					qtdBitsTotais--;
					auxiliar++;
				}
				i--;
				// Alterei isso, coloquei as ultimas 3 condicoes (Verificar isso)
				if(auxiliar % 50 == 0 || i == cG.getBitsTotaisViolacaoCamadaFisica()-2 || i == 0){
					auxiliar = 0;
				}
				else{
			deslocBit = deslocBit % 32;
			for (int j = 0; j < 2; j++) { // For para cada Bit
			  if (parBit.charAt(j) == '1') {
				// define o bit na posicaoo deslocBit do quadro[indexQuadro] como 1
				quadroEnquadrado[tamanhoArray - 1] = quadroEnquadrado[tamanhoArray - 1] | (1 << deslocBit);
			  }
			  deslocBit--;
			  if (deslocBit == -1) {
				deslocBit = 31;
				tamanhoArray--;
			  }
			}
				}
		//System.out.println("Teste: "+ cG.ExibirBinarioControleErro(quadroEnquadrado));		
		} // Fim For Bits
		cG.setQtdBitsInsercaoBits((cG.getQtdBitsInsercaoBits() - cG.getFlagsViolacaoFisica())/2);
		return quadroEnquadrado;
	  }

	void CamadaDeAplicacaoReceptora(int quadro[]) {
		int deslocBit = 0;
		StringBuilder Mensagem = new StringBuilder();

		for (int i = 0; i < cG.getInicialCaracteres(); i++) { // For ate o tamanho da Mensagem
			StringBuilder Char = new StringBuilder();
			for (int j = 0; j < 8; j++) { // For para cada Bit
				int mascara = 1 << deslocBit; // Mascara com bit 1 na Posicao deslocBit
				int Bit = (quadro[i / 4] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro
				if (Bit == -1) {
					Bit = Bit * -1;
				}
				Char.insert(0, Bit); // insere o bit no caractere
				deslocBit++;
			}

			int aux = Integer.parseInt(Char.toString(), 2); // converte o binario em inteiro
			Mensagem.append((char) aux); // converte o inteiro em char
		}
		AplicacaoReceptora(Mensagem.toString());
		System.out.println("Mensagem Descriptografada: " + Mensagem.toString());
	}// fim do metodo CamadaDeAplicacaoReceptora

	void AplicacaoReceptora(String mensagem) {
		cG.disableButtons();
		cG.setFinalText(mensagem);
	}// fim do metodo AplicacaoReceptora


}
