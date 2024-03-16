/* ***************************************************************
* Autor............: Carlos Gil Martins da Silva
* Matricula........: 202110261
* Inicio...........: 08/10/2023
* Ultima alteracao.: 13/10/2023
* Nome.............: Controller Principal
* Funcao...........: Controla toda a a parte de interface, por
meio de imagens, botoes e tudo que se faz necessario
****************************************************************/

package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.ControleDeErros;
import model.Enlace;
import model.Receptor;
import model.Transmissor;

public class controllerPrincipal implements Initializable {

	@FXML private ChoiceBox<String> boxSelect;
	@FXML private ChoiceBox<String> boxSelect2;
  	@FXML private ChoiceBox<String> boxSelectControle;
	@FXML private ChoiceBox<Integer> boxSelectTaxa;
	@FXML private ImageView startScreen;
	@FXML private Button buttonSelect;
	@FXML private ImageView mainScreen;
	@FXML private ImageView buttonSend;
	@FXML private TextArea textBox;
	@FXML private TextArea finalText;

	// Imagens dos Sinais
	@FXML private ImageView signalAlto1;
	@FXML private ImageView signalAlto2;
	@FXML private ImageView signalAlto3;
	@FXML private ImageView signalAlto4;
	@FXML private ImageView signalAlto5;
	@FXML private ImageView signalAlto6;
	@FXML private ImageView signalAlto7;
	@FXML private ImageView signalAlto8;
	@FXML private ImageView signalBaixo1;
	@FXML private ImageView signalBaixo2;
	@FXML private ImageView signalBaixo3;
	@FXML private ImageView signalBaixo4;
	@FXML private ImageView signalBaixo5;
	@FXML private ImageView signalBaixo6;
	@FXML private ImageView signalBaixo7;
	@FXML private ImageView signalBaixo8;
	@FXML private ImageView signalPe1;
	@FXML private ImageView signalPe2;
	@FXML private ImageView signalPe3;
	@FXML private ImageView signalPe4;
	@FXML private ImageView signalPe5;
	@FXML private ImageView signalPe6;
	@FXML private ImageView signalPe7;
	@FXML private ImageView signalPe8;
	@FXML private ImageView manchester1;
	@FXML private ImageView manchester2;
	@FXML private ImageView manchester3;
	@FXML private ImageView manchester4;
	@FXML private ImageView manchester5;
	@FXML private ImageView manchester6;
	@FXML private ImageView manchester7;
	@FXML private ImageView manchester8;
	@FXML private ImageView manchester9;
	@FXML private ImageView manchester10;
	@FXML private ImageView manchester11;
	@FXML private ImageView manchester12;
	@FXML private ImageView manchester13;
	@FXML private ImageView manchester14;
	@FXML private ImageView manchester15;
	@FXML private ImageView manchester16;
	@FXML private Slider waveSlider;
  @FXML private TextArea binaryArea;
	@FXML private TextArea enquadArea;
	@FXML private ImageView buttonCodif;

	int codificacao = -1; //codificacao escolhida
	int enquadramento = 3; //Enquadramento escolhido
  int controleErro = 0; //Controle de Erro Escolhido
  int taxaErro = 0; // taxa de erro escolhida

	// variaveis de controle/comparacao
	String comparationSignal = "1";
	int comparationBinary = 0;
	Boolean contrBoolean = false;
	Boolean contrVisualizacao = false;
	int qtdBitsInsercaoBits = 0;
	int caracteresAnterior = 0;
	int caracAux = 0;
	int bitsTotaisViolacaoCamadaFisica = 0;
	int flagsViolacaoFisica = 0;
	int inicialCaracteres = 0; // Numero de Caracteres incial sem nada Digitado
	int NumCaracteres = 0; // num de caracteres da palavra digitada
	

	// Array de imagens, para setar as ondas
	ImageView[] arraySignalBaixo;
	ImageView[] arraySignalAlto;
	ImageView[] arraySignalPe;
	ImageView[] arraySignalManchester0;
	ImageView[] arraySignalManchester1;
	int sleep = 300;
	Transmissor Ts;
	Enlace Ec;
	Receptor Rt;
	ControleDeErros Ctr;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Setando as imagens em cada array correspondente e as opcoes de codificacao
		boxSelect.getItems().addAll("Binaria", "Manchester", "Manchester Diferencial");
		boxSelect2.getItems().addAll("Contagem de Caracteres", "Insercao de Bytes", "Insercao de Bits", "Violacao da Camada Fisica");
    boxSelectControle.getItems().addAll("Paridade Par", "Paridade Impar", "CRC", "Hamming");
    boxSelectTaxa.getItems().addAll(10,20,30,40,50,60,70,80,90);
		ImageView[] SuportB = { signalBaixo1, signalBaixo2, signalBaixo3, signalBaixo4, signalBaixo5, signalBaixo6,
				signalBaixo7, signalBaixo8 };
		arraySignalBaixo = SuportB;

		ImageView[] SuportA = { signalAlto1, signalAlto2, signalAlto3, signalAlto4, signalAlto5, signalAlto6, signalAlto7,
				signalAlto8 };
		arraySignalAlto = SuportA;

		ImageView[] SuportP = { signalPe1, signalPe2, signalPe3, signalPe4, signalPe5, signalPe6, signalPe7, signalPe8 };
		arraySignalPe = SuportP;

		ImageView[] SuportM0 = { manchester1, manchester2, manchester3, manchester4, manchester5, manchester6, manchester7,
				manchester8 };
		arraySignalManchester0 = SuportM0;

		ImageView[] SuportM1 = { manchester9, manchester10, manchester11, manchester12, manchester13, manchester14,
				manchester15, manchester16 };
		arraySignalManchester1 = SuportM1;

		Ts = new Transmissor();
		Ts.setControlador(this);

		Ec = new Enlace();
		Ec.setControlador(this);

		Rt = new Receptor();
		Rt.setControlador(this);

		Ctr = new ControleDeErros();
		Ctr.setControlador(this);
	}

    @FXML
    void clickSelected(MouseEvent event) { // Metodo para selecionar o tipo de codificacao
      if (boxSelect.getValue() != null && boxSelect2.getValue() != null && boxSelectControle.getValue() != null
          && boxSelectTaxa.getValue() != null) {
        switch (boxSelect.getValue()) {
          case "Binaria":
            codificacao = 0;
            break;
          case "Manchester":
            codificacao = 1;
            break;
          case "Manchester Diferencial":
            codificacao = 2;
            break;
        } // fim Switch
        switch (boxSelectControle.getValue()) {
          case "Paridade Par":
            controleErro = 0;
            break;
          case "Paridade Impar":
            controleErro = 1;
            break;
          case "CRC":
            controleErro = 2;
            break;
          case "Hamming":
            controleErro = 3;
            break;
        } // fim Switch
        switch (boxSelect2.getValue()) {
          case "Contagem de Caracteres":
            enquadramento = 0;
            break;
          case "Insercao de Bytes":
            enquadramento = 1;
            break;
          case "Insercao de Bits":
            enquadramento = 2;
            break;
          case "Violacao da Camada Fisica":
            enquadramento = 3;
            break;
        } // fim Switch
        taxaErro = (boxSelectTaxa.getValue() / 10);
        if (codificacao == 0 && enquadramento == 3) {
          Alert alert = new Alert(AlertType.WARNING);
          alert.setTitle("Aviso");
          alert.setHeaderText("Ocorreu um erro!");
          alert.setContentText(
              "Devido a Sua Implementacao, O metodo de Enquadramento Violacao da Camada Fisica nao funciona com o tipo de codificaco binaria, selecione qualquer outra combinacao");

          alert.showAndWait();
        } else {
          disableStartScreen(); // desativa a tela inicial
        }
      } // fim if
      else {
        System.out.println("Selecione uma opcao");
      }
    }

	public int getCodificacao() {
		return codificacao;
	}

  public int getControleErro() {
      return controleErro;
  }
  public int getTaxaErro() {
      return taxaErro;
  }
  public void setFlagsViolacaoFisica(int flagsViolacaoFisica) {
	  this.flagsViolacaoFisica = flagsViolacaoFisica;
  }
  public int getFlagsViolacaoFisica() {
	  return flagsViolacaoFisica;
  }

	public void setBitsTotaisViolacaoCamadaFisica(int bitsTotaisViolacaoCamadaFisica) {
		this.bitsTotaisViolacaoCamadaFisica = bitsTotaisViolacaoCamadaFisica;
	}
	public int getBitsTotaisViolacaoCamadaFisica() {
		return bitsTotaisViolacaoCamadaFisica;
	}

	public int getInicialCaracteres() {
		return inicialCaracteres;
	}
	public void setInicialCaracteres(int inicialCaracteres) {
		this.inicialCaracteres = inicialCaracteres;
	}

	public int getCaracteresAnterior() {
			return caracteresAnterior;
	}

	public int getCaracAux() {
		return caracAux;
	}
	public void setCaracAux(int caracAux) {
		this.caracAux = caracAux;
	}

	public Transmissor getTs() {
		return Ts;
	}

	public Receptor getRt() {
			return Rt;
	}

	public ControleDeErros getCtr() {
		return Ctr;
	}

	public void setFinalText(String RespostaFinal) {
		finalText.setText(RespostaFinal);
	}

	public void setNumCaracteres(int numCaracteres) {
		NumCaracteres = numCaracteres;
	}

	public int getNumCaracteres() {
		return NumCaracteres;
	}

	@FXML
	void clickButtonSend(MouseEvent event) { // Metodo quando clicar no botão de enviar a mensagem
		if (textBox.getText().isEmpty()) {
			System.out.println("Digite alguma coisa!");
		} else {
			Ts.AplicacaoTransmissora(textBox.getText()); // Chama o Transmissor
			disableButtons(); // desativa os botoes
			textBox.clear(); // limpa o texto digitado
			finalText.clear(); // limpa o texto recebido (caso tenha)
			finalText.setPromptText("Aguardando Mensagem!"); // seta um prompt text
		for (int i=0; i < 8; i++){
			arraySignalAlto[i].setVisible(false);
			arraySignalBaixo[i].setVisible(false);
			arraySignalPe[i].setVisible(false);
			arraySignalManchester0[i].setVisible(false);
			arraySignalManchester1[i].setVisible(false);
		}
		}
	}

	public void disableButtons(){ // Desativa/ativa os botoes
		if(!contrVisualizacao){
			buttonSend.setVisible(false);
			buttonSend.setDisable(true);
			buttonCodif.setVisible(false);
			buttonCodif.setDisable(true);
			contrVisualizacao = !contrVisualizacao;
		}
		else{
			buttonSend.setVisible(true);
			buttonSend.setDisable(false);
			buttonCodif.setVisible(true);
			buttonCodif.setDisable(false);
			contrVisualizacao = !contrVisualizacao;
		}
	}
	public void updateSignalBinario(int bit, int deslocBit) { //Atualiza o sinal da onda binario
		Platform.runLater(() -> {
			// seta tudo como false
			arraySignalAlto[0].setVisible(false);
			arraySignalBaixo[0].setVisible(false);
			arraySignalPe[0].setVisible(false);

			// caso haja transicao ele ativa a posicao em pe
			if (comparationBinary != bit && deslocBit != 0) {
				arraySignalPe[0].setVisible(true);
			}
			// ativa o signal com base no bit
			if (bit == 0) {
				arraySignalBaixo[0].setVisible(true);
			} else {
				arraySignalAlto[0].setVisible(true);
			}
			comparationBinary = bit;
		});
	}

	public int getSliderWave() { // retorna o valor do slider
		double aux = waveSlider.getValue();
		int retorno = (int)aux;
		return retorno;
	  }

	public void updateSignalManchester(String bit) { //Atualiza o sinal da onda Manchester
		Platform.runLater(() -> {
			// seta tudo false
			arraySignalManchester0[0].setVisible(false);
			arraySignalManchester1[0].setVisible(false);
			arraySignalPe[0].setVisible(false);
			arraySignalAlto[0].setVisible(false);

			// compara para ver a necessidade de setar visualizacao do sinal de transicao
			if(bit.equals("11")){
				if(comparationSignal.equals("01"))
				arraySignalPe[0].setVisible(true);	
			}
			if(comparationSignal.equals("11")){
				if(bit.equals("10")){
					arraySignalPe[0].setVisible(true);	
				}
			}
			else{
				if (comparationSignal.equals(bit)) {
					arraySignalPe[0].setVisible(true);
				}
			}
			// compara o par de bits recebido
			if (bit.equals("01")) {
				arraySignalManchester0[0].setVisible(true);
			} else if(bit.equals("10")) {
				arraySignalManchester1[0].setVisible(true);
			}else{
				arraySignalAlto[0].setVisible(true);
			}
	
			comparationSignal = bit;
		});
	}

	public void adiantaSignal(int codificacao) { // avanca o sinal 1 posicao a frente
		Platform.runLater(() -> {
			// atualiza a ultima posicao do array com base na sua anterior, ou seja
			// se estamos na posicao 7, seta a visualizao dela com base na visualizacao da posicao 6
			switch (codificacao) {
				case 0:
					for (int i = 7; i > 0; i--) {
						arraySignalAlto[i].setVisible(arraySignalAlto[i - 1].isVisible());
						arraySignalBaixo[i].setVisible(arraySignalBaixo[i - 1].isVisible());
						arraySignalPe[i].setVisible(arraySignalPe[i - 1].isVisible());
					}
					break;
				case 1:
					for (int i = 7; i > 0; i--) {
						arraySignalManchester0[i].setVisible(arraySignalManchester0[i - 1].isVisible());
						arraySignalManchester1[i].setVisible(arraySignalManchester1[i - 1].isVisible());
						arraySignalPe[i].setVisible(arraySignalPe[i - 1].isVisible());
						arraySignalAlto[i].setVisible(arraySignalAlto[i - 1].isVisible());
					}
					break;
				case 2:
					for (int i = 7; i > 0; i--) {
						arraySignalManchester0[i].setVisible(arraySignalManchester0[i - 1].isVisible());
						arraySignalManchester1[i].setVisible(arraySignalManchester1[i - 1].isVisible());
						arraySignalPe[i].setVisible(arraySignalPe[i - 1].isVisible());
						arraySignalAlto[i].setVisible(arraySignalAlto[i - 1].isVisible());
					}
					break;
			}
		});
	}

	public String charParaBinario(char caractere) { // Converte um Char em Binario
		StringBuilder binario = new StringBuilder(8); 
		for (int i = 7; i >= 0; i--) { // For do tamanho dos Bits de um Caractere
			int bit = (caractere >> i) & 1; // desloca o bit do caractere direita do por i posicoes e aplica a mascara
			binario.append(bit);  // concatena o bit na string
		}
		return binario.toString();
	}

	public String ExibirBinario(int[] bits) { // exibe os valores binarios/bits do array
		int deslocBit = 0;
		StringBuilder Binario = new StringBuilder();
        for (int i = 0; i < getNumCaracteres(); i++) { // For ate o tamanho da Mensagem
			if (deslocBit == 32){ // limita essa variavel a 31, ultimo bit da posicao
				deslocBit =0;
			}
			for (int j = 0; j < 8; j++) { // For para cada Caractere
			  int mascara = 1 << deslocBit;
			  int Bit = (bits[i/4] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro na posicao deslocBit
			  if(Bit == -1){
				Bit = Bit * -1;
			  }
			Binario.insert(0, Bit); // insere o bit na string
			  deslocBit++; 
			  }
			Binario.insert(0, " "); // insere um espaco para melhor visualizacao a cada caractere
			}
			return Binario.toString(); //retorna o binario
		  }

		public String ExibirBinarioControleErro(int[] bits) { // exibe os valores binarios/bits do array
		StringBuilder Binario = new StringBuilder();
      for (int i = getQtdBitsInsercaoBits()-1; i >= 0; i--) {
        int bitQuadro = i%32;
        int mascara = 1 << bitQuadro;
        int Bit = (bits[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
        if(Bit == -1){
				Bit = Bit * -1;
			  }
        Binario.append(Bit); // insere o bit na string
      } // Fim For Bits
			return Binario.toString(); //retorna o binario
		  }


		  public void setBinaryArea(String Code) {
			  binaryArea.setText(Code);
		  }
		
			public void setEnquadArea(String Code) {
					enquadArea.setText(Code);
			}
		public String ExibirManchester(int[] bits) { // exibe o manchester
		int deslocBit = 0;
		StringBuilder Manchester = new StringBuilder();
        for (int i = 0; i < getNumCaracteres(); i++) { // For ate o tamanho da Mensagem
			if (deslocBit == 32){ // limita ate 32
				deslocBit =0;
			}
			for (int j = 0; j < 16; j++) { // For para cada caractere
			  int mascara = 1 << deslocBit;
			  int Bit = (bits[i/2] & mascara) >> deslocBit; // Pega o Bit na posicao da Mascara&Quadro na posicao deslocBit
				if(Bit == -1){
				Bit = Bit * -1;
			  }
			Manchester.insert(0, Bit); // insere o bit
			  deslocBit++; 
			  }
			Manchester.insert(0, " "); // insere o espaco a cada 16
			}
			return Manchester.toString();
		  }

	public void disableStartScreen() { // desativa a tela principal e tudo que participa dela
		boxSelect2.setVisible(false);
		boxSelect2.setDisable(true);
		boxSelect.setVisible(false);
		boxSelect.setDisable(true);
    boxSelectControle.setVisible(false);
		boxSelectControle.setDisable(true);
    boxSelectTaxa.setVisible(false);
		boxSelectTaxa.setDisable(true);

		startScreen.setVisible(false);
		startScreen.setDisable(true);

		buttonSelect.setVisible(false);
		buttonSelect.setDisable(true);
	}
//define o tamanho do array, com base que cada array Binario cabe 4 caracteres e Manchester 2 carac
	public int setTamanhoArray(int tipoDeCodificacao) { 
		switch (tipoDeCodificacao) {
			case 0:
				if (NumCaracteres % 4 == 0)
					return (NumCaracteres / 4);
				else
					return ((NumCaracteres / 4) + 1);

			default:
				if (NumCaracteres % 2 == 0)
					return (NumCaracteres / 2);
				else
					return ((NumCaracteres / 2) + 1);
		}
	}

    // define o tamanho do array de Enquadramento, com base que cada array Binario 4 carac
    // Verifica tambem no case 1 o tamanho total do quadro, verificando quantas fags fakes
    // Para Obter o Tamanho ideal do quadro
    public void setNumCaracteresEnquadramento(int tipoDeEnquadramento, int[] quadro) {
      int contQuadros;
      switch (tipoDeEnquadramento) {
        case 0: // Contagem de Caracteres
          caracteresAnterior = NumCaracteres;
          if (NumCaracteres % 3 == 0)
            NumCaracteres = ((NumCaracteres / 3) + NumCaracteres);
          else
            NumCaracteres = ((NumCaracteres / 3) + (NumCaracteres + 1));
          setQtdBitsInsercaoBits(NumCaracteres * 8);
          break; // Fim Contagem de Caracteres
        case 1: // Insercao de Bytes
          caracteresAnterior = NumCaracteres;
          String valueEscape = "01111100"; // -> Escape = |
          String STX = "01010011"; // -> Flag = S
          String ETX = "01000101"; // -> Flag = E
          int contFlagFAKE = 0;
          int contEscFAKE = 0;
          int deslocBit = 0;
          for (int i = 0; i < getNumCaracteres(); i++) { // For ate o tamanho da Mensagem
            String Char = recuperaBitArray(quadro, i, deslocBit);
            if (STX.equals(Char.toString()) || ETX.equals(Char.toString())) { // Verifica se o Carcacter Recuperado eh
                                                                              // uma FlagFake
              contFlagFAKE++;
            }
            if (valueEscape.equals(Char.toString())) { // Verifica se o Carcacter Recuperado eh uma EscapeFake
              contEscFAKE++;
            }
            deslocBit = deslocBit + 8;
          }
          if (NumCaracteres % 3 == 0)
            contQuadros = NumCaracteres / 3;
          else
            contQuadros = (NumCaracteres / 3) + 1;

          NumCaracteres = NumCaracteres + (contQuadros * 2) + contEscFAKE + contFlagFAKE;
          setQtdBitsInsercaoBits(NumCaracteres * 8);
          break; // Fim Insercao de Bites

        case 2:
          caracteresAnterior = NumCaracteres;
          int bitsTotais = 0;
          int contBITS1 = 0;
          int contFLAGFAKE = 0;
          int deslocamentoBit = 0;
          for (int i = 0; i < getNumCaracteres(); i++) { // For ate o tamanho da Mensagem
            for (int j = 0; j < 8; j++) { // For para cada Bit
              int mascara = 1 << deslocamentoBit; // Mascara com bit 1 na Posicao deslocBit
              int Bit = (quadro[i / 4] & mascara) >> deslocamentoBit; // Pega o Bit na posicao da Mascara&Quadro
              if (Bit == -1 || Bit == 1) {
                contBITS1++;
              } else {
                contBITS1 = 0;
              }
              if (contBITS1 == 5) {
                contFLAGFAKE++;
                contBITS1 = 0;
              }
              deslocamentoBit++;
            }
          }
          // Define o tamanho do novo array com o enquadramento
          bitsTotais = NumCaracteres * 8;
          if (contFLAGFAKE > 0) {
            if ((contFLAGFAKE % 8) == 0) {
              NumCaracteres = NumCaracteres + (contFLAGFAKE / 8);
            } else {
              NumCaracteres = NumCaracteres + ((contFLAGFAKE / 8) + 1);
            }
          }
          if (NumCaracteres % 3 == 0)
            contQuadros = (NumCaracteres / 3) - 1;
          else
            contQuadros = NumCaracteres / 3;

          NumCaracteres = NumCaracteres + (contQuadros + 2);
          bitsTotais += (contQuadros + 2) * 8 + contFLAGFAKE;
          setQtdBitsInsercaoBits(bitsTotais);
          break;
        case 3:
          setQtdBitsInsercaoBits(NumCaracteres*8);
          break;
        default:
          break;
      }
    }

    // Define o Tamanho Ideal para o Array com informacao de controle de erros
    public int setNumBITScontroleErro(int tipoDeControle) {
      int tamanhoQuadro = 0;
      switch (tipoDeControle) {
        case 2:
          if((getQtdBitsInsercaoBits() + 32) % 32 == 0){
			tamanhoQuadro = ((getQtdBitsInsercaoBits() + 32) / 32);
		  }
		  else{
			tamanhoQuadro = (((getQtdBitsInsercaoBits() + 32) / 32)+1);
		  }
		  caracAux = NumCaracteres;
          if ((getQtdBitsInsercaoBits() + 32) % 8 == 0) {
            NumCaracteres = (getQtdBitsInsercaoBits() + 32)/8;
          } else {
            NumCaracteres = ((getQtdBitsInsercaoBits() + 32)/8)+1;
          }
		  return tamanhoQuadro;
        case 3:
		  caracAux = NumCaracteres;
          return 1; // Retorno do Tamanho é Definido no metodo do Controle

        default: // Bit Par ou Bit Impar
          if ((getQtdBitsInsercaoBits() + 1) % 32 == 0) {
            tamanhoQuadro = ((getQtdBitsInsercaoBits() + 1) / 32);
          } else {
            tamanhoQuadro = (((getQtdBitsInsercaoBits() + 1) / 32) + 1);
          }
		  caracAux = NumCaracteres;
          if ((getQtdBitsInsercaoBits() + 1) % 8 == 0) {
            NumCaracteres = (getQtdBitsInsercaoBits() + 1)/8;
          } else {
            NumCaracteres = ((getQtdBitsInsercaoBits() + 1)/8)+1;
          }

          return tamanhoQuadro;
        // Fim Bit Par ou Bit Impar
      }
    }
	public int binaryToInteger(String binary) {
        int result = 0;
        int length = binary.length();

        for (int i = 0; i < length; i++) {
            char digit = binary.charAt(i);
            if (digit == '1') {
                result += Math.pow(2, length - 1 - i);
            } else if (digit != '0') {
                throw new IllegalArgumentException("A string de entrada contém caracteres inválidos.");
            }
        }
        return result;
    }


// Método que realiza a Divisão Binária com a operação de Xor
public String divisaoBinariaResto(String dividendo, String divisor) {
    int comprimentoDividendo = dividendo.length();
    int comprimentoDivisor = divisor.length();
    // Inicializa o resto com o valor do dividendo
    StringBuilder resto = new StringBuilder(dividendo);
    // Loop principal para realizar a divisão binária
    while (resto.length() >= comprimentoDivisor) {
        // Verifica se o bit mais à esquerda do resto é '1'
        if (resto.charAt(0) == '1') {
            // Realiza a operação XOR bit a bit entre o resto e o divisor
            for (int i = 0; i < comprimentoDivisor; i++) {
                char bitAtual = resto.charAt(i);
                char bitDivisor = divisor.charAt(i);
                // Se os bits forem iguais, o resultado é '0', caso contrário, é '1'
                if (bitAtual == bitDivisor) {
                    resto.setCharAt(i, '0');
                } else {
                    resto.setCharAt(i, '1');
                }
            }
        } else {
            // Se o bit mais à esquerda do resto for '0', removemos esse bit
            resto.deleteCharAt(0);
        }
    }
    // Se o resto estiver vazio, o resultado é zero
    if (resto.length() == 0) {
        return "0";
    }
    // Retorna o resultado da divisão binária (resto)
    return resto.toString();
}


	// Metodo q Insere os bits de controle do Codigo de hamming
	public String codificarHamming(String dados) {
		int tamanhoEntrada = dados.length();
		StringBuilder dadoCodificado = new StringBuilder();
	
		// Loop para processar a entrada em segmentos de 4 bits
		// Devido estarmos usando a codificacao de hamming 	(7,4) existe
		// a necessidade de separar a entrada
		for (int i = 0; i < tamanhoEntrada; i += 4) {
			// Determina o tamanho do segmento atual (pode ser menor que 4 bits no último segmento)
			int subTamanho = Math.min(4, tamanhoEntrada - i);
			String subEntrada = dados.substring(i, i + subTamanho);
	
			// Preenche com zeros à esquerda, se o segmento for menor que 4 bits
			while (subEntrada.length() < 4) {
				subEntrada = "0" + subEntrada;
			}
	
			// Converte o segmento de entrada em um array de inteiros (bits)
			int[] entradaBits = new int[4];
			for (int j = 0; j < 4; j++) {
				entradaBits[j] = Character.getNumericValue(subEntrada.charAt(j));
			}
	
			/*Os bits de paridade p1, p2 e p3 são calculados com base nos bits da entradaBits.
			Cada bit de paridade é calculado usando operações XOR (^)
			entre os bits correspondentes do segmento.*/
			int p1 = entradaBits[0] ^ entradaBits[1] ^ entradaBits[3];
			int p2 = entradaBits[0] ^ entradaBits[2] ^ entradaBits[3];
			int p3 = entradaBits[1] ^ entradaBits[2] ^ entradaBits[3];
	
			// Montagem da subSequência codificada
			dadoCodificado.append(p1).append(p2).append(entradaBits[0]).append(p3).append(entradaBits[1]).append(entradaBits[2]).append(entradaBits[3]);
		}
	
		// Retorna a sequência de saída completa
		return dadoCodificado.toString();
	}

	//Realiza a Decodificacao de Hamming
	public String decodificarHamming(String dadoRecebido) {
		int tamanhoEntrada = dadoRecebido.length();
		StringBuilder dadoDecodificado = new StringBuilder();
	
		for (int i = 0; i < tamanhoEntrada; i += 7) {
			// Determina o tamanho do segmento atual (pode ser menor que 7 bits no último segmento)
			int subTamanho = Math.min(7, tamanhoEntrada - i);
			String subEntrada = dadoRecebido.substring(i, i + subTamanho);
	
			// Preenche com zeros à esquerda, se o segmento for menor que 7 bits
			while (subEntrada.length() < 7) {
				subEntrada = "0" + subEntrada;
			}
	
			// Converte a subEntrada em um array de inteiros (bits)
			int[] entradaBits = new int[7];
			for (int j = 0; j < 7; j++) {
				entradaBits[j] = Character.getNumericValue(subEntrada.charAt(j));
			}
	
			// Calcula os bits de paridade (p1, p2 e p3)
			int p1 = entradaBits[0] ^ entradaBits[2] ^ entradaBits[4] ^ entradaBits[6];
			int p2 = entradaBits[1] ^ entradaBits[2] ^ entradaBits[5] ^ entradaBits[6];
			int p3 = entradaBits[3] ^ entradaBits[4] ^ entradaBits[5] ^ entradaBits[6];
	
			// Calcula o índice do bit de erro
			int bitErro = p1 + p2 * 2 + p3 * 4;
	
			// Verifica se há erro na sequência
			if (bitErro > 0) {
				// Se houver erro, retorne null
				return null;
			}
	
			// Monta a subSequência decodificada
			dadoDecodificado.append(entradaBits[2]).append(entradaBits[4]).append(entradaBits[5]).append(entradaBits[6]);
		}
	
		// Retorna a sequência de saída completa
		return dadoDecodificado.toString();
	}
	
	
	

	public void setQtdBitsInsercaoBits(int qtdBitsInsercaoBits) {
		this.qtdBitsInsercaoBits = qtdBitsInsercaoBits;
	}

public int getQtdBitsInsercaoBits() {
		return qtdBitsInsercaoBits;
}
	public int atualizaIndiceArrayEnquadrado(int contCarac, int indexQuadro){
		if (contCarac % 4 == 0 && contCarac != 0) { // Aumenta o indice do Array Enquadrado quando tiver mais de 4
			indexQuadro++;
		}
		return indexQuadro;
	}

	public int setDeslocamentoBIT(int controllerDeslocamento, int deslocamento){
		if (controllerDeslocamento % 4 == 0) {
			deslocamento = 7;
		  } else {
			deslocamento += 16;
		  }
		return deslocamento;
	}


	public String recuperaBitArray(int[]quadro, int i, int deslocBit){
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
					return Char.toString();
	}

	@FXML
    void alterarCodificacao(MouseEvent event) { //ativa a tela inicial e reseta todos valores ao inicio
		boxSelect2.setVisible(true);
		boxSelect2.setDisable(false);
		boxSelect.setVisible(true);
		boxSelect.setDisable(false);

    boxSelectControle.setVisible(true);
		boxSelectControle.setDisable(false);
    boxSelectTaxa.setVisible(true);
		boxSelectTaxa.setDisable(false);

		startScreen.setVisible(true);
		startScreen.setDisable(false);

		buttonSelect.setVisible(true);
		buttonSelect.setDisable(false);

		for (int i=0; i < 8; i++){
			arraySignalAlto[i].setVisible(false);
			arraySignalBaixo[i].setVisible(false);
			arraySignalPe[i].setVisible(false);
			arraySignalManchester0[i].setVisible(false);
			arraySignalManchester1[i].setVisible(false);
		}

		textBox.clear(); // limpa o texto digitado
		finalText.clear(); // limpa o texto recebido (caso tenha)
		binaryArea.clear();
		enquadArea.clear();
		waveSlider.setValue(100);

    }

	public Enlace getEc() {
		return Ec;
	}
	public int getEnquadramento() {
		return enquadramento;
	}
}
