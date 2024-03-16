/* ***************************************************************
* Autor............: Carlos Gil Martins da Silva
* Matricula........: 202110261
* Inicio...........: 31/08/2023
* Ultima alteracao.: 05/09/2023
* Nome.............: Comunicacao
* Funcao...........: Recebe os Bits manipulados e passa para
o fluxo B que será passado para o Receptor logo em seguida, tambem
faz o controle e animação da onda de sinais por meio da utilizacao de
threads
****************************************************************/

package model;
import control.controllerPrincipal;

public class Comunicacao {

  public void MeioDeComunicacao(int[] fluxoBrutoDeBits, controllerPrincipal cT) {
    int[] fluxoBrutoDeBitsPontoA;
    int[] fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBits.length]; // Seta o tamanho do Array
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    System.out.println("Quadro Enviado : " + cT.ExibirBinarioControleErro(fluxoBrutoDeBitsPontoA));
    cT.setBinaryArea(cT.ExibirBinarioControleErro(fluxoBrutoDeBitsPontoA));
    new Thread(() -> {
      switch(cT.getCodificacao()){
        case 0: // SETA A FORMA DE ONDA BINARIA
          for (int i = cT.getQtdBitsInsercaoBits()-1; i >= 0; i--) {
            int numeroRandom = (int) (Math.random() * 9) + 1; // Alterna entre 1 e 9
            int bitQuadro = i % 32;
            int mascara = 1 << bitQuadro;

            int Bit = (fluxoBrutoDeBitsPontoA[i / 32] & mascara) >> bitQuadro; // Pega o Bit
            
            if (numeroRandom <= cT.getTaxaErro()) { // If que verifica se vai ter erro ou nao
              // Estrutura de IF que manipula bit por Bit
              if (Bit == 1 || Bit == -1) {
                Bit = 0;
                fluxoBrutoDeBitsPontoB[i / 32] = fluxoBrutoDeBitsPontoB[i / 32] | (0 << bitQuadro);
              } else {
                Bit = 1;
                fluxoBrutoDeBitsPontoB[i / 32] = fluxoBrutoDeBitsPontoB[i / 32] | (1 << bitQuadro);
              }
            }
            else if (Bit == 1 || Bit == -1) {
              Bit = 1;
                fluxoBrutoDeBitsPontoB[i / 32] = fluxoBrutoDeBitsPontoB[i / 32] | (1 << bitQuadro);
            }
            cT.adiantaSignal(cT.getCodificacao()); // Adianta os Sinais Mostrados na Tela
            cT.updateSignalBinario(Bit, bitQuadro); // Seta o bit pego do Array no Sinal da Tela
            try {
              Thread.sleep(cT.getSliderWave()); // Sleep com o valor do Slider
            } catch (InterruptedException e) {
            }
          } // Fim For Bits
        break;


        default: // SETA A FORMA DE ONDA MANCHESTER DIFERENCIAL OU NORMAL       
        // SETANDO A FORMA DE ONDA
        int controladorParBits = 0;
        boolean booleanParBits = false;
        StringBuilder parBit = new StringBuilder();
            for (int i = cT.getQtdBitsInsercaoBits()-1; i >= 0; i--) {
              controladorParBits++;
              if(controladorParBits % 2 == 0 && controladorParBits != 0){
                booleanParBits = true;
                controladorParBits = 0;
              }
              int numeroRandom = (int) (Math.random() * 9) + 1; // Alterna entre 1 e 9
              int bitQuadro = i % 32;
              int mascara = 1 << bitQuadro;
              int Bit = (fluxoBrutoDeBitsPontoA[i / 32] & mascara) >> bitQuadro; // Pega o Bit
              if (numeroRandom <= cT.getTaxaErro()) { // If que verifica se vai ter erro ou nao
                // Estrutura de IF que manipula bit por Bit
                if (Bit == 1 || Bit == -1) {
                  Bit = 0;
                  fluxoBrutoDeBitsPontoB[i / 32] = fluxoBrutoDeBitsPontoB[i / 32] | (0 << bitQuadro);
                } else {
                  Bit = 1;
                  fluxoBrutoDeBitsPontoB[i / 32] = fluxoBrutoDeBitsPontoB[i / 32] | (1 << bitQuadro);
                }
              }
              else if (Bit == 1 || Bit == -1) {
                Bit = 1;
                  fluxoBrutoDeBitsPontoB[i / 32] = fluxoBrutoDeBitsPontoB[i / 32] | (1 << bitQuadro);
              }
            parBit.append(Bit); // Insere o Bit no parBit que será um par de Bits
            if(booleanParBits){
              cT.adiantaSignal(cT.getCodificacao()); // Adianta os Sinais Mostrados na Tela
              cT.updateSignalManchester(parBit.toString()); // Seta o bit pego do Array no Sinal da Tela
              booleanParBits = false;
              parBit = new StringBuilder();
            }
            try {
              Thread.sleep(cT.getSliderWave()); // Sleep com o valor do Slider
            } catch (InterruptedException e) {
            }
          } // Fim For Bits
          break;
        } // Fim Switch
        System.out.println("Quadro Recebido: " + cT.ExibirBinarioControleErro(fluxoBrutoDeBitsPontoB));
        cT.setEnquadArea(cT.ExibirBinarioControleErro(fluxoBrutoDeBitsPontoB));
      cT.getRt().CamadaFisicaReceptora(fluxoBrutoDeBitsPontoB); // Chama o Receptor
    }).start(); // Fim thread
  } // Fim meio de Comunic
  }// fim do metodo MeioDeTransmissao