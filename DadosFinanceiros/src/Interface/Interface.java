package Interface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import GA.*;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class Interface {

	private JFrame frame;
	private JTextField txtTxt;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 803, 477);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblDataDenicio = new JLabel("Data de ínicio:");
		lblDataDenicio.setBounds(6, 3, 118, 53);
		
		txtTxt = new JTextField();
		txtTxt.setBounds(110, 15, 103, 29);
		txtTxt.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblDataDenicio);
		frame.getContentPane().add(txtTxt);
		
		JLabel lblRentabilidade = new JLabel("Rentabilidade:");
		lblRentabilidade.setBounds(257, 3, 118, 53);
		frame.getContentPane().add(lblRentabilidade);
		
		textField_2 = new JTextField();
		textField_2.setBounds(353, 15, 444, 29);
		textField_2.setColumns(10);
		frame.getContentPane().add(textField_2);
		
		JLabel lblDataDeFim = new JLabel("Data de fim:");
		lblDataDeFim.setBounds(6, 56, 118, 50);
		frame.getContentPane().add(lblDataDeFim);
		
		textField = new JTextField();
		textField.setBounds(110, 65, 103, 33);
		textField.setColumns(10);
		frame.getContentPane().add(textField);
		
		JLabel lblNewLabel = new JLabel("Metodo Saída:\n");
		lblNewLabel.setBounds(257, 43, 92, 50);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblAcestxt = new JLabel("Acções (txt) :\n");
		lblAcestxt.setBounds(6, 106, 118, 48);
		frame.getContentPane().add(lblAcestxt);
		
		textField_1 = new JTextField();
		textField_1.setBounds(110, 118, 103, 29);
		textField_1.setColumns(10);
		frame.getContentPane().add(textField_1);
		
		JLabel lblLimiteSuperior = new JLabel("Limite Superior:\n");
		lblLimiteSuperior.setBounds(257, 148, 118, 48);
		frame.getContentPane().add(lblLimiteSuperior);
		
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(16, 166, 195, 29);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] args = new String[3];
				args[0] = txtTxt.getText();
				args[1] = textField.getText();
				args[2] = textField_1.getText();
				try {
					GA.MaximizeProfit.main(args);
					ArrayList<String> padrao = new ArrayList<String>();
					String resposta = String.format("%.3f",MaximizeProfit.getBestSolution().getFitnessValue());
					String saida = null;
					switch (MaximizeProfitFitnessFunction.getIntParameter(MaximizeProfit.getBestSolution(), 0)){
				    case 0: saida = MaximizeProfitFitnessFunction.getDias() +" Dias.";
				    break;
				    case 1: saida = String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit e " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss";
				    break;
				    case 2: saida = MaximizeProfitFitnessFunction.getJpadrao()  +" DiasdoPadraoAsc.";
				    break;
				    case 3: saida = MaximizeProfitFitnessFunction.getDias()  +" Dias em cojunto com " + String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit e " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss";
				    break;
				    case 4: saida = MaximizeProfitFitnessFunction.getDias()  +" Dias em cojunto com " + MaximizeProfitFitnessFunction.getJpadrao() +" DiasdoPadraoAsc.";
				    break;
				    case 5: saida = String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit e " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss em conjunto com "  + MaximizeProfitFitnessFunction.getJpadrao() +" DiasdoPadraoAsc.";
				    break;
				    case 6: saida = MaximizeProfitFitnessFunction.getDias()  +" Dias com " + String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit e " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss em conjunto com "  + MaximizeProfitFitnessFunction.getJpadrao() +" DiasdoPadraoAsc.";
				    break;
				    }
					String accoes = MaximizeProfitFitnessFunction.getIntParameter(MaximizeProfit.getBestSolution(), 1) + "";
					String Limiteinf = 		
				            MaximizeProfitFitnessFunction.
				            getIntParameter(
				        MaximizeProfit.getBestSolution(), 2)+"";
					String Limitesup = 
				            MaximizeProfitFitnessFunction.
				            getIntParameter(
				        MaximizeProfit.getBestSolution(), 3)+"";
					String janela = 
				            MaximizeProfitFitnessFunction.
				            getIntParameter(
				        MaximizeProfit.getBestSolution(), 4)+"";
				    String sax =        
				    		String.format("%.3f",MaximizeProfitFitnessFunction.
				            getDoubleParameter(
				        MaximizeProfit.getBestSolution(), 5))+"";
				    String ppis = 
				            MaximizeProfitFitnessFunction.
				            getIntParameter(
				        MaximizeProfit.getBestSolution(), 6) +"";
				    String regras = 
				            MaximizeProfitFitnessFunction.
				            getIntParameter(
				         MaximizeProfit.getBestSolution(), 7) +"";
				    
				    for(int i=8; i < MaximizeProfit.getBestSolution().size(); i++){
				    	padrao.add(MaximizeProfitFitnessFunction.getStringParameter(MaximizeProfit.getBestSolution(), i));
				    	}
				    	String padrao1 = padrao + "";
				textField_2.setText(resposta);
				textField_3.setText(saida);
				textField_4.setText(accoes);
				textField_5.setText(Limiteinf);
				textField_6.setText(Limitesup);
				textField_7.setText(janela);
				textField_8.setText(sax);
				textField_9.setText(ppis);
				textField_10.setText(regras);
				textField_11.setText(padrao1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			}
			}
		});
		frame.getContentPane().add(btnRun);
		
		JLabel lblLimiteInferior = new JLabel("Limite Inferior:\n");
		lblLimiteInferior.setBounds(257, 203, 118, 33);
		frame.getContentPane().add(lblLimiteInferior);
		
		JLabel lblDistncia = new JLabel("Distância:\n");
		lblDistncia.setBounds(257, 293, 118, 29);
		frame.getContentPane().add(lblDistncia);
		
		JLabel lblPpis = new JLabel("PPIs:");
		lblPpis.setBounds(257, 334, 118, 29);
		frame.getContentPane().add(lblPpis);
		
		JLabel lblPadro = new JLabel("Padrão:");
		lblPadro.setBounds(257, 407, 103, 16);
		frame.getContentPane().add(lblPadro);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(353, 52, 444, 33);
		frame.getContentPane().add(textField_3);
		
		JLabel lblNAces = new JLabel("Nº Acções:");
		lblNAces.setBounds(257, 97, 118, 48);
		frame.getContentPane().add(lblNAces);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(336, 107, 461, 29);
		frame.getContentPane().add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(368, 158, 429, 29);
		frame.getContentPane().add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(368, 205, 429, 29);
		frame.getContentPane().add(textField_6);
		
		JLabel lblJanela = new JLabel("Janela:");
		lblJanela.setBounds(257, 248, 118, 33);
		frame.getContentPane().add(lblJanela);
		
		JLabel lblRegras = new JLabel("Regras:\n");
		lblRegras.setBounds(257, 366, 118, 29);
		frame.getContentPane().add(lblRegras);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(315, 248, 482, 29);
		frame.getContentPane().add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(325, 293, 472, 29);
		frame.getContentPane().add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(301, 334, 496, 29);
		frame.getContentPane().add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(311, 366, 486, 29);
		frame.getContentPane().add(textField_10);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(315, 401, 482, 29);
		frame.getContentPane().add(textField_11);
	}
}
