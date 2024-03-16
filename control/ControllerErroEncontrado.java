/* ***************************************************************
* Autor............: Carlos Gil Martins da Silva
* Matricula........: 202110261
* Inicio...........: 31/10/2023
* Ultima alteracao.: 04/11/2023
* Nome.............: Controle da Tela ErroEncontrado
* Funcao...........: O Controle ErroENCONTRADO controla a tela
de erro quando o foi encontrada um erro na transmissao da mensagem
pelo meio de comunicacao. Controla toda a tela
*************************************************************** */

package control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerErroEncontrado {

  @FXML
  private Button botaoRetorno;

  @FXML
  private ImageView fundoPreencha;

  @FXML
  void BotaoRetorno(MouseEvent event) {
    Stage stage = (Stage) botaoRetorno.getScene().getWindow(); //Obtendo a janela atual
    stage.close(); //Fechando o Stage
  }
}
