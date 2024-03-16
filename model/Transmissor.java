/* ***************************************************************
* Autor............: Carlos Gil Martins da Silva
* Matricula........: 202110261
* Inicio...........: 31/08/2023
* Ultima alteracao.: 05/09/2023
* Nome.............: Transmissor
* Funcao...........: Recebe a mensagem em String e manipula ela
convertendo para char, para int, para binario, e manipula os bits
do array de inteiros
****************************************************************/
package model;

import control.controllerPrincipal;

public class Transmissor {

	controllerPrincipal cG = new controllerPrincipal(); // Instanciando e Criando o Controller
	Comunicacao cM = new Comunicacao(); // Instanciando a Comunicacao

	// Metodo Utilizado para Setar um Controlador em Comum em Todas Thread
	public void setControlador(controllerPrincipal controle) {
	  this.cG = controle;
	}

	public void AplicacaoTransmissora(String mensagem) {
		CamadaDeAplicacaoTransmissora(mensagem);
	}// fim do metodo AplicacaoTransmissora

	void CamadaDeAplicacaoTransmissora(String mensagem) {
		char[] msg = mensagem.toCharArray(); // transforma a mensagem em um Array de Char
		cG.setNumCaracteres(msg.length); //seta o numero de caracteres no controller
		cG.setInicialCaracteres(msg.length);
		int indexQuadro = 0; // Index do Array
		int deslocBit = 0; // Variavel define qual bit vai deslocar para manipulacao (Inicia do 7 pois eh o ultimo Bit de um Caractere)
		// Estrutura Para Definir o Tamanho do Array
		int[] quadro = new int[cG.setTamanhoArray(cG.getCodificacao())]; // Criacao do Array de Quadros
		// For ate o tamanho da Mensagem
		for (int i = 0; i < cG.getNumCaracteres(); i++) {
			deslocBit = 7 *(i+1) + i; // Posicao do bit comparativo (Inicia do 7 pois eh o ultimo Bit de um Caractere) para os bits nao inverterem
			if(i % 4 == 0 && i != 0){ // Aumenta o indice do Array quando tiver mais de 4 letras
				indexQuadro++;
			}
			String aux = cG.charParaBinario(msg[i]); // String auxiliar para transformar em binario
			System.out.println("Caractere: " + msg[i] + " || Em Binario: " + aux);
			for (int j = 0; j < 8; j++) { // For para cada Bit
				// Estrutura de IF que manipula bit por Bit
				if(aux.charAt(j) =='1'){
					// define o bit na posicaoo deslocBit do quadro[indexQuadro] como 1, mantendo os outros bits inalterados.
					quadro[indexQuadro] = quadro[indexQuadro] | (1 << deslocBit);
				}
			deslocBit--;
			}
		}
		cG.setBinaryArea(cG.ExibirBinario(quadro)); 
		cG.getEc().CamadaEnlaceDadosTransmissora(quadro); // Chama a Primeira Camada;
	}// fim do metodo CamadaDeAplicacaoTransmissora

	void CamadaFisicaTransmissora(int quadro[]) {
		int tipoDeCodificacao = cG.getCodificacao(); // alterar de acordo o teste
		System.out.println("Tipo de Codificacao: "+ tipoDeCodificacao);
		int[] fluxoBrutoDeBits = new int[cG.setTamanhoArray(cG.getCodificacao())]; // ATENÇÃO: trabalhar com BITS!!!
		switch (tipoDeCodificacao) {
			case 0: // codificao binaria
				fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoBinaria(quadro);
				break;
			case 1: // codificacao manchester
				fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchester(quadro);
				break;
			case 2: // codificacao manchester diferencial
				fluxoBrutoDeBits = CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
				break;
		}// fim do switch/case
		cM.MeioDeComunicacao(fluxoBrutoDeBits, cG);
	}// fim do metodo CamadaFisicaTransmissora


	public int[] CamadaFisicaTransmissoraCodificacaoBinaria(int quadro[]) {
		// Como o Binário não precisa Alterar os valores, retorna o mesmo Quadro
		return quadro;
	}// fim do metodo CamadaFisicaTransmissoraCodificacaoBinaria

	public int[] CamadaFisicaTransmissoraCodificacaoManchester(int quadro[]) {
		cG.setQtdBitsInsercaoBits(cG.getQtdBitsInsercaoBits()*2);
		int [] CodManchester = new int[cG.setTamanhoArray(cG.getCodificacao())];
		int controllerBits = 0;
		int controllerBitsManchester = 0;	
		int indexManch = 0; // Index do Array Manchester
		
		int deslocBit = 0; // Variavel define qual bit vai deslocar para manipulacaoo (Inicia do 7 pois eh o ultimo Bit de um Caractere)
		int deslocBitManchester = 0; // Variavel define qual bit vai deslocar para manipulacao (Inicia do 7 pois eh o ultimo Bit de um Caractere)

		// For até o tamanho da Mensagem
		for (int i = 0; i < cG.getNumCaracteres(); i++) {
			if(i % 2 == 0 && i != 0){ // Aumenta o indice do Array quando tiver mais de 2 letras (Cada index guarda 2 letras (Metade do Binario))
				indexManch++;
			}
			// Estruturas de If/else que faz o controle coordenado dos bits de cada array
			// Sendo eles o binario (7, 15, 23, 31) e o Manchester (15, 31)
			// assim, garante que serao pegados os bits corretamente salvando 2 caracteres no array manchester
			if (controllerBitsManchester % 2 == 0) {
				deslocBitManchester = 15; // Primeiro Bit da Primeira Letra
				controllerBitsManchester = 0;
			} else {
				deslocBitManchester = 31; // Primeiro Bit da Segunda lETRA
			}
			if (controllerBits % 4 == 0) {
				deslocBit = 7;
				controllerBits = 0;
			} else {
				deslocBit += 16;
			}
	
			for (int j = 0; j < 8; j++) { // For para cada caractere
 				int mascara = 1 << deslocBit;
				int Bit = (quadro[i/4] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro na posicao deslocBit
				// Estrutura de IF que manipula bit por Bit
				if(Bit == 1 || Bit == -1){
					// define o bit na posicaoo deslocBit do quadro[indexQuadro] como 10, mantendo os outros bits inalterados.
					CodManchester[indexManch] = CodManchester[indexManch] | (1 << (deslocBitManchester));
					CodManchester[indexManch] = CodManchester[indexManch] | (0 << (deslocBitManchester-1));}
				else{
					// define o bit na posicaoo deslocBit do quadro[indexQuadro] como 01, mantendo os outros bits inalterados.
					CodManchester[indexManch] = CodManchester[indexManch] | (0 << (deslocBitManchester));
					CodManchester[indexManch] = CodManchester[indexManch] | (1 << (deslocBitManchester-1));}
			deslocBit--;
			deslocBitManchester = deslocBitManchester - 2;
			}
			controllerBits++;
			controllerBitsManchester++;
		}
    //Verifica Se o enquadramento selecionado foi o 
    //Violacao da camada fisica, caso tenha sido realiza o enquadramento
    // antes de enviar para o meio de comunicacao
    int quadroEnquadrado[];
		if(cG.getEnquadramento() == 3){
			 quadroEnquadrado = CodificacaoViolacaoCamadaFisica(CodManchester);
		}
    else{
      quadroEnquadrado = CodManchester;
    }
		//System.out.println("Testando: "+ cG.ExibirBinarioControleErro(quadroEnquadrado));
		return quadroEnquadrado;
	}// fim do metodo CamadaFisicaTransmissoraCodificacaoManchester

	public int[] CamadaFisicaTransmissoraCodificacaoManchesterDiferencial(int quadro[]) {
		cG.setQtdBitsInsercaoBits(cG.getQtdBitsInsercaoBits()*2); 
		int controllerBits = 0;
		int controllerBitsManchester = 0;
		int [] CodManchesterDiff = new int[cG.setTamanhoArray(cG.getCodificacao())];
		int indexManch = 0; // Index do Array Manchester Differencial
		int deslocBit = 0; // Variavel define qual bit vai deslocar para manipulação (Inicia do 7 pois é o ultimo Bit de um Caractere)
		int deslocBitManchester = 0; // Variavel define qual bit vai deslocar para manipulação (Inicia do 7 pois é o ultimo Bit de um Caractere)
		
		// For até o tamanho da Mensagem
		for (int i = 0; i < cG.getNumCaracteres(); i++) {
			boolean InversionSignal = false;
			if(i % 2 == 0 && i != 0){ // Aumenta o indice do Array quando tiver mais de 2 letras (Cada index guarda 2 letras (Metade do Binario))
				indexManch++;
			}

			// Estruturas de If/else que faz o controle coordenado dos bits de cada array
			// Sendo eles o binario (7, 15, 23, 31) e o Manchester (15, 31)
			// assim, garante que serao pegados os bits corretamente salvando 2 caracteres no array manchester
		if (controllerBitsManchester % 2 == 0) {
			deslocBitManchester = 15; // Primeiro Bit da Primeira Letra
			controllerBitsManchester = 0;
		} else {
			deslocBitManchester = 31; // Primeiro Bit da Segunda lETRA
		}
		if (controllerBits % 4 == 0) {
			deslocBit = 7;
			controllerBits = 0;
		} else {
			deslocBit += 16;
		}

			for (int j = 0; j < 8; j++) { // For para cada caractere
				int mascara = 1 << deslocBit;
				int Bit = (quadro[i/4] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro na posicao deslocBit
				// Estrutura de IF que manipula bit por Bit
				if (Bit == 1 || Bit == -1) {
					// define o bit na posicao deslocBit do quadro[indexQuadro] como 1, mantendo os outros bits inalterados.
					if (InversionSignal == true) {
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (0 << (deslocBitManchester));
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (1 << (deslocBitManchester - 1));
						InversionSignal = false;
					} else {
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (1 << (deslocBitManchester));
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (0 << (deslocBitManchester - 1));
						InversionSignal = true;
					}
				}
				else {
					// define o bit na posicao deslocBit do quadro[indexQuadro] como 1, mantendo os outros bits inalterados.
					if (InversionSignal == true) {
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (1 << (deslocBitManchester));
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (0 << (deslocBitManchester - 1));

					} else {
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (0 << (deslocBitManchester));
						CodManchesterDiff[indexManch] = CodManchesterDiff[indexManch] | (1 << (deslocBitManchester - 1));
					}
				}
				deslocBit--;
				deslocBitManchester = deslocBitManchester - 2;
			}
			controllerBits++;
			controllerBitsManchester++;
		}
    //Verifica Se o enquadramento selecionado foi o 
    //Violacao da camada fisica, caso tenha sido realiza o enquadramento
    // antes de enviar para o meio de comunicacao
    int quadroEnquadrado[];
		if(cG.getEnquadramento() == 3){
			 quadroEnquadrado = CodificacaoViolacaoCamadaFisica(CodManchesterDiff);
		}
    else{
      quadroEnquadrado = CodManchesterDiff;
    }
		return quadroEnquadrado;
	}// fim do CamadaFisicaTransmissoraCodificacaoManchesterDiferencial

    public int[] CodificacaoViolacaoCamadaFisica(int[] quadro) {
      //SETANDO O NOVO TAMANHO DO ARRAY JA COM OS BITS DO
      //ENQUADRAMENTO (2 BITS 1) a cada flag em cada sub
      //Divisao de cada enquadramento
	  int NumeroCaracteres;
	  if(((cG.getQtdBitsInsercaoBits()/2) % 8) == 0){
		NumeroCaracteres = ((cG.getQtdBitsInsercaoBits()/2) / 8);
	  }
	  else{
		NumeroCaracteres = ((cG.getQtdBitsInsercaoBits()/2) / 8)+1;
	  }
      int qtdflags;
      if (NumeroCaracteres % 3 == 0)
	  qtdflags = (NumeroCaracteres / 3) +1;
      else
	  qtdflags = (NumeroCaracteres / 3) + 2;
	  
      if(qtdflags*2 %16 == 0){
		  cG.setNumCaracteres(NumeroCaracteres + (qtdflags/16));
		}else{cG.setNumCaracteres(NumeroCaracteres + ((qtdflags/16) + 1));}
		
		int qtdBitsTotais = cG.getQtdBitsInsercaoBits()+ qtdflags * 2;
		cG.setBitsTotaisViolacaoCamadaFisica(qtdBitsTotais);
		int bitsAnterior = cG.getQtdBitsInsercaoBits();
	  	cG.setQtdBitsInsercaoBits(qtdBitsTotais); // Verificar a questao da Violacao Camada Fisica
		cG.setFlagsViolacaoFisica(qtdflags * 2);
      //int qtdBitsSemEnquadramento = (NumeroCaracteres*2) * 8;
      int tamanhoArray = 0;

      if (qtdBitsTotais % 32 == 0)
        tamanhoArray = qtdBitsTotais / 32; // Index do Array ENQUADRADO
      else
        tamanhoArray = qtdBitsTotais / 32 + 1;
      // Criando Novo quadro com o novo tamanho
      int quadroEnquadrado[] = new int[tamanhoArray]; // Novo Array Quadro

      int indexQuadro = tamanhoArray;
      int flagCount = 0;
      String Flag = "11"; // flag inicial e final pois eh um par de sinais 11 nao utilizado
      // nas codificacoes Manchester e Manchester diferencial, sendo essa a flag imposta pelo
      // metodo de violacao da camada fisica
      qtdBitsTotais--; // Se Forem 32 bits, percorre do 31 a 0 (Ou seja, um a menos)

      // For até o tamanho da Mensagem
      for (int i = bitsAnterior - 1; i >= 0; i--) {
        qtdBitsTotais = qtdBitsTotais % 32;
        // Quadros de Tamanho 3
        if (flagCount == 0 || flagCount == 48) { // Insere os bytes de Controle inicial e final de cada quadro (Insercao
                                                 // de Bits)
          for (int j = 0; j < 2; j++) { // For para cada Bit
            if (Flag.charAt(j) == '1') {
              // define o bit na posicaoo deslocBit do quadro[indexQuadro] como 1
              quadroEnquadrado[indexQuadro - 1] = quadroEnquadrado[indexQuadro - 1] | (1 << qtdBitsTotais);
            }
            qtdBitsTotais--;
            if (qtdBitsTotais == -1) {
              qtdBitsTotais = 31;
              indexQuadro--;
            }
          }
          flagCount = 0;
        } // Fim If Controle do Enquadramento (insercao de bytes)

        int bitQuadro = i % 32;
        int mascara = 1 << bitQuadro;
        int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc
                                                           // Bit
        // Estrutura de IF que manipula bit por Bit
        if (Bit == 1 || Bit == -1) {
          quadroEnquadrado[indexQuadro - 1] = quadroEnquadrado[indexQuadro - 1] | (1 << qtdBitsTotais);
        } 
        
        qtdBitsTotais--;
        if (qtdBitsTotais == -1) {
          qtdBitsTotais = 31;
          indexQuadro--;
        }

        flagCount++;
        if (i == 0) { // Insere os bytes de Controle inicial e final de cada quadro (Insercao de Bits)
          for (int j = 0; j < 2; j++) { // For para cada Bit
            if (Flag.charAt(j) == '1') {
              // define o bit na posicaoo deslocBit do quadro[indexQuadro] como 1
              quadroEnquadrado[indexQuadro - 1] = quadroEnquadrado[indexQuadro - 1] | (1 << qtdBitsTotais);
            }
            qtdBitsTotais--;
            if (qtdBitsTotais == -1) {
              qtdBitsTotais = 31;
              indexQuadro--;
            }
          }
        } // Fim If Controle do Enquadramento (insercao de bytes)

      } // Fim For Bits
      return quadroEnquadrado;
    }
}
