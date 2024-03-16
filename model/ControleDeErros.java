/* ***************************************************************
* Autor............: Carlos Gil Martins da Silva
* Matricula........: 202110261
* Inicio...........: 31/10/2023
* Ultima alteracao.: 04/11/2023
* Nome.............: Controle de Erros
* Funcao...........: Camada Controle de erros, faz toda a parte
de controle, desde a transmissao ate a recepcao, controlando tudo
que eh necessario para o mesmo, em todos os metodos pedidos no
trabalho
****************************************************************/
package model;

import java.io.IOException;
import control.controllerPrincipal;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControleDeErros {
    int TamanhoQuadro = 0;
    controllerPrincipal cG = new controllerPrincipal(); // Instanciando e Criando o Controller

  // Metodo Utilizado para Setar um Controlador em Comum em Todas Thread
  public void setControlador(controllerPrincipal controle) {
    this.cG = controle;
  }
public int[] CamadaEnlaceDadosTransmissoraControleDeErro(int quadro[]) {
  int tipoDeControleDeErro = cG.getControleErro(); // alterar de acordo com o teste
  TamanhoQuadro = cG.setNumBITScontroleErro(tipoDeControleDeErro);
  int[] quadroControleErro = new int[TamanhoQuadro];
    switch (tipoDeControleDeErro) {
      case 0: // bit de paridade par
        quadroControleErro = CamadaEnlaceDadosTransmissoraControleDeErroBitParidadePar(quadro);
        break;
      case 1: // bit de paridade impar
        quadroControleErro = CamadaEnlaceDadosTransmissoraControleDeErroBitParidadeImpar(quadro);
        break;
      case 2: // CRC
         quadroControleErro = CamadaEnlaceDadosTransmissoraControleDeErroCRC(quadro);
        break;
      case 3: // codigo de Hamming
        quadroControleErro = CamadaEnlaceDadosTransmissoraControleDeErroCodigoDeHamming(quadro);
        // codigo
        break;
    }// fim do switch/case
    return quadroControleErro;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControleDeErro

  int[] CamadaEnlaceDadosTransmissoraControleDeErroBitParidadePar(int quadro[]) {
      int[] quadroControleErro = new int[TamanhoQuadro];
    int somatorioBITS1 = 0;
    int PercorrerArray;
    if(cG.getQtdBitsInsercaoBits()%32 == 0)
      PercorrerArray = cG.getQtdBitsInsercaoBits()/32;
    else
      PercorrerArray = (cG.getQtdBitsInsercaoBits()/32) +1;

     int posicaoBitControle = ((cG.getQtdBitsInsercaoBits()+1) %32)-1;
    // For até o tamanho da Mensagem
    for (int i = cG.getQtdBitsInsercaoBits()-1; i >= 0; i--) {
        int bitQuadro = i%32;
        int mascara = 1 << bitQuadro;
        int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
        // Estrutura de IF que manipula bit por Bit
        if (Bit == 1 || Bit == -1) {
          somatorioBITS1++;
        }
      } // Fim For Bits
      for(int i =0; i < PercorrerArray; i++){
        quadroControleErro[i] = quadro[i];
      }
      if(somatorioBITS1 % 2 != 0){ // Se nao tiver Quantidade Par de Bits, Insere um Bit 1 no ultimo Bit da mensagem
        quadroControleErro[quadroControleErro.length-1] = quadroControleErro[quadroControleErro.length-1] | (1 << posicaoBitControle);
      }
    cG.setQtdBitsInsercaoBits((cG.getQtdBitsInsercaoBits() + 1));
    return quadroControleErro;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControledeErroBitParidadePar

  int[] CamadaEnlaceDadosTransmissoraControleDeErroBitParidadeImpar(int quadro[]) {
      int[] quadroControleErro = new int[TamanhoQuadro];
    int somatorioBITS1 = 0;
    int PercorrerArray;
    if(cG.getQtdBitsInsercaoBits()%32 == 0)
      PercorrerArray = cG.getQtdBitsInsercaoBits()/32;
    else
      PercorrerArray = (cG.getQtdBitsInsercaoBits()/32) +1;

     int posicaoBitControle = ((cG.getQtdBitsInsercaoBits()+1) %32)-1;
    // For até o tamanho da Mensagem
    for (int i = cG.getQtdBitsInsercaoBits()-1; i >= 0; i--) {
        int bitQuadro = i%32;
        int mascara = 1 << bitQuadro;
        int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
        // Estrutura de IF que manipula bit por Bit
        if (Bit == 1 || Bit == -1) {
          somatorioBITS1++;
        }
      } // Fim For Bits
      for(int i =0; i < PercorrerArray; i++){
        quadroControleErro[i] = quadro[i];
      }
      if(somatorioBITS1 % 2 == 0){ // Se nao tiver Quantidade Par de Bits, Insere um Bit 1 no ultimo Bit da mensagem
        quadroControleErro[quadroControleErro.length-1] = quadroControleErro[quadroControleErro.length-1] | (1 << posicaoBitControle);
      }
    cG.setQtdBitsInsercaoBits((cG.getQtdBitsInsercaoBits() + 1));
    return quadroControleErro;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControledeErroBitParidadeImpar

  int[] CamadaEnlaceDadosTransmissoraControleDeErroCRC(int quadro[]) {
     /* EXEMPLO DE FUNCIONAMENTO DO CRC, COM UM DE 4 BITS
    11010011101100 000 <--- entrada deslocada para a direita com 3 zeros
    1011               <--- divisor (4 bits) = x³ + x + 1
    ------------------
    01100011101100 000 <--- resultado
    */

    // Polinomio CRC-32 = x32, x26, x23, x22, x16, x12, x11, x10, x8, x7, x5, x4, x2, x1 + 1
    // Logo um Polinomio de 33 bits com 32 bits de resto, assim deslocamos a mensagem
    // 32 Bits 0 A direita e dividos o crc e inserimos nesse lugar

    String PolinomioCRC32 = "100000100110000010001110110110111";
    int[] quadroComDetecErro = new int[TamanhoQuadro];
    
    // Passando os Bits do Quadro antigo para o Novo Ja inserindo os Bits 0 a direita para divisao Binaria
    int bitsAtt = cG.getQtdBitsInsercaoBits() + 31;
    for(int i = cG.getQtdBitsInsercaoBits() -1; i >= 0; i--){
      int bitQuadro = i%32;
      int mascara = 1 << bitQuadro;
      int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
      if(Bit == 1 || Bit  == -1){
        int bitQuadroComErro = bitsAtt%32;
        quadroComDetecErro[bitsAtt/32] = quadroComDetecErro[bitsAtt/32] | (1 << bitQuadroComErro);
      }
      bitsAtt--;
    }

    cG.setQtdBitsInsercaoBits((cG.getQtdBitsInsercaoBits() + 32));
    // Obtendo o Resto da Divisao Binaria
    String Resto = cG.divisaoBinariaResto(cG.ExibirBinarioControleErro(quadroComDetecErro), PolinomioCRC32);
    
    // Inserindo o CRC32 no final do Quadro de Bits
    int AuxIndexChar = 0;
    for(int i = 31; i >= 0; i--){
      int bitQuadroErro = i%32;
      if(Resto.toString().charAt(AuxIndexChar) == '1'){
        quadroComDetecErro[i/32] = quadroComDetecErro[i/32] | (1 << bitQuadroErro);
      }
      AuxIndexChar++;
    } // Fim For Bits

    return quadroComDetecErro;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControledeErroCRC

  int[] CamadaEnlaceDadosTransmissoraControleDeErroCodigoDeHamming(int quadro[]) {
    String mensagemBinaria = cG.ExibirBinarioControleErro(quadro); // String Binaria da Mensagem
    // Inserindo a Codificacao de Hamming
    StringBuilder mensagemCodificada = new StringBuilder(cG.codificarHamming(mensagemBinaria));
    mensagemCodificada.reverse(); // Invertendo Para Inserir corretamente no Array
    
    //Setando a quantidade total de bits e o tamanho do array
    // Com o controle de erro e novo Num de Caracteres/Caracteres Anterior
    cG.setQtdBitsInsercaoBits(mensagemCodificada.length());
    int TamanhoQuadroControleERRO;
    if(mensagemCodificada.length() % 32 == 0){
      TamanhoQuadroControleERRO = (mensagemCodificada.length() / 32);
    }
    else{
      TamanhoQuadroControleERRO = (mensagemCodificada.length() / 32) + 1;
    }

    if ((mensagemCodificada.length()) % 8 == 0) {
      cG.setNumCaracteres(((mensagemCodificada.length())/8));
    } else {
      cG.setNumCaracteres(((mensagemCodificada.length())/8)+1);
    }

    //Criando o Array com o novo Tamanho e Inserindo os Bits
    // Por meio de Mascara nele
    int[] arrayControleERRO = new int[TamanhoQuadroControleERRO];
    for(int i = (mensagemCodificada.length()-1); i >= 0; i--){
      int bitQuadro = i%32;
      if(mensagemCodificada.charAt(i) == '1'){
        arrayControleERRO[i/32] = arrayControleERRO[i/32] | (1 << bitQuadro);
      }
    }
    // implementacao do algoritmo
    return arrayControleERRO;
  }// fim do metodo CamadaEnlaceDadosTransmissoraControleDeErroCodigoDehamming


  ////////////////////////////////////////////////////
  //                                                //
  // FINALIZAÇÃO DO CONTROLE DE ERROS TRANSMISSORA  //
  // INICIO DO CONTROLE DE ERROS ENLACE RECEPETORA  //
  //                                               //
  //////////////////////////////////////////////////

  int[] CamadaEnlaceDadosReceptoraControleDeErro (int quadro []) {
    int tipoDeControleDeErro = cG.getControleErro(); //alterar de acordo com o teste
    int[] quadroSemErro;
    switch (tipoDeControleDeErro) {
    case 0 : //bit de paridade par
    quadroSemErro = CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadePar(quadro);
    break;
    case 1 : //bit de paridade impar
    quadroSemErro = CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadeImpar(quadro);
    break;
    case 2 : //CRC
    quadroSemErro = CamadaEnlaceDadosReceptoraControleDeErroCRC(quadro);
    break;
    case 3 : //codigo de hamming
    quadroSemErro = CamadaEnlaceDadosReceptoraControleDeErroCodigoDeHamming(quadro);
    break;
    default:
    quadroSemErro = quadro;
    break;
    }//fim do switch/case
    return quadroSemErro;
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErro

  int[] CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadePar (int quadro []){
    int posicaoRemocao = ((cG.getQtdBitsInsercaoBits() %32)-1);
    int indexArray = 0;
    if(cG.getQtdBitsInsercaoBits() % 32 == 0)
      indexArray = (cG.getQtdBitsInsercaoBits() / 32);
    else
      indexArray = (cG.getQtdBitsInsercaoBits() / 32)+1;

    int somatorioBITS1 = 0;
    // For até o tamanho da Mensagem
    for (int i = cG.getQtdBitsInsercaoBits()-1; i >= 0; i--) {
        int bitQuadro = i%32;
        int mascara = 1 << bitQuadro;
        int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
        // Estrutura de IF que manipula bit por Bit
        if (Bit == 1 || Bit == -1) {
          somatorioBITS1++;
        }
      } // Fim For Bits
      if(somatorioBITS1 % 2 == 0){ // Se Tiver Quantidade Par de Bits, Insere um Bit 1 no ultimo Bit da mensagem
        //Tira a Informação de Controle do Array de Bits
        quadro[indexArray-1] = quadro[indexArray-1] | (0 << posicaoRemocao);
        cG.setQtdBitsInsercaoBits(cG.getQtdBitsInsercaoBits()-1);
        return quadro;
      }
      else{
        TelaErro(); // Chama Tela de Erro
        return null;
      }
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadePar
   int[] CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadeImpar (int quadro []) {
    int posicaoRemocao = ((cG.getQtdBitsInsercaoBits() %32)-1);
    int indexArray = 0;
    if(cG.getQtdBitsInsercaoBits() % 32 == 0)
      indexArray = (cG.getQtdBitsInsercaoBits() / 32);
    else
      indexArray = (cG.getQtdBitsInsercaoBits() / 32)+1;

    int somatorioBITS1 = 0;
    // For até o tamanho da Mensagem
    for (int i = cG.getQtdBitsInsercaoBits()-1; i >= 0; i--) {
        int bitQuadro = i%32;
        int mascara = 1 << bitQuadro;
        int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
        // Estrutura de IF que manipula bit por Bit
        if (Bit == 1 || Bit == -1) {
          somatorioBITS1++;
        }
      } // Fim For Bits
      if(somatorioBITS1 % 2 != 0){ // Se Tiver Quantidade Par de Bits, Insere um Bit 1 no ultimo Bit da mensagem
        //Tira a Informação de Controle do Array de Bits
        quadro[indexArray-1] = quadro[indexArray-1] | (0 << posicaoRemocao);
        cG.setQtdBitsInsercaoBits(cG.getQtdBitsInsercaoBits()-1);
        return quadro;
      }
      else{
        TelaErro(); // Chama Tela de Erro
        return null;
      }
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroBitDeParidadeImpar
   int[] CamadaEnlaceDadosReceptoraControleDeErroCRC (int quadro []) {
    int[] quadroSemControle;
    String PolinomioCRC32 = "100000100110000010001110110110111";
    String verifyRESTO = cG.divisaoBinariaResto(cG.ExibirBinarioControleErro(quadro), PolinomioCRC32);
    //Validacao se A mensagem Chegou Corretamente (Sem ERROS)
    for(int i = 0; i < 32; i++){
      if(verifyRESTO.charAt(i) != '0'){
        TelaErro();
        return null;
      }
    }
    //Definindo Tamanho do Array sem o Controle de Erro
    if((cG.getQtdBitsInsercaoBits()-32) % 32 == 0){
      quadroSemControle = new int[((cG.getQtdBitsInsercaoBits()-32) / 32)];
    }else{
      quadroSemControle = new int[(((cG.getQtdBitsInsercaoBits()-32) / 32)+1)];
    }
    int bitsAnterior = cG.getQtdBitsInsercaoBits()-33;

    for(int i = cG.getQtdBitsInsercaoBits() -1; i >= 32; i--){
      int bitQuadro = i%32;
      int mascara = 1 << bitQuadro;
      int Bit = (quadro[i / 32] & mascara) >> bitQuadro; // Pega o Bit na posicao da Mascara&Quadro na posicao desloc Bit
      if(Bit == 1 || Bit  == -1){
        int bitQuadroSemControle = bitsAnterior%32;
        quadroSemControle[bitsAnterior/32] = quadroSemControle[bitsAnterior/32] | (1 << bitQuadroSemControle);
      }
      bitsAnterior--;
    }
    cG.setQtdBitsInsercaoBits((cG.getQtdBitsInsercaoBits() - 32));
    return quadroSemControle;
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroCRC

   int[] CamadaEnlaceDadosReceptoraControleDeErroCodigoDeHamming (int quadro []) {
    String StringBinariaRecebida = cG.ExibirBinarioControleErro(quadro);

    if(cG.decodificarHamming(StringBinariaRecebida) == null){
      TelaErro();
      return null;
    }

    StringBuilder mensagemBinaria = new StringBuilder(cG.decodificarHamming(StringBinariaRecebida));
    mensagemBinaria.reverse(); // Invertendo os bits para inserir corretamente no array


    //Definindo o tamanho do array sem Controle de Erro
    int tamanhoQuadro;
    if(mensagemBinaria.length() % 32 == 0){
      tamanhoQuadro = (mensagemBinaria.length() / 32);
    }
    else{
      tamanhoQuadro = (mensagemBinaria.length() / 32) + 1;
    }

    //Criando o Array com o novo Tamanho e Inserindo os Bits
    // Por meio de Mascara nele
    int[] arraySemControleERRO = new int[tamanhoQuadro];
    for(int i = (mensagemBinaria.length()-1); i >= 0; i--){
      int bitQuadro = i%32;
      if(mensagemBinaria.charAt(i) == '1'){
        arraySemControleERRO[i/32] = arraySemControleERRO[i/32] | (1 << bitQuadro);
      }
    }
    return arraySemControleERRO;
   }//fim do metodo CamadaEnlaceDadosReceptoraControleDeErroCodigoDeHamming

   //Exibe a tela de Erro encontrado
   public void TelaErro(){
        Platform.runLater(() -> {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/fxmlErroEncontrado.fxml")); // Carregando Tela de Erro Campos Preenchidos
        Parent root;
          root = loader.load();
          Stage stage = new Stage(); // Setando Novo Stage
          stage.setScene(new Scene(root)); // Setando Nova Cena
          stage.setResizable(false); // Impossibilitando Redimensionamento
          stage.show(); // Mostrando Novo Stage
          stage.setTitle("Erro Encontrado");
        } catch (IOException e) {}
      });
   }
}
