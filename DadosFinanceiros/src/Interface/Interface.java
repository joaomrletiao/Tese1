package Interface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import GA.*;
import dados.Testar;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.SystemColor;
import java.text.ParseException;
import java.util.ArrayList;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
	private JTextField textField_12;
	private JTextField textField_13;
	private int tsaida;
	private ArrayList<String> pattern;
	private int time;
	private double tk;
	private double sl;
	private int patternexit;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	
	public int getTime() {
		return time;
	}


	public double getTk() {
		return tk;
	}


	public double getSl() {
		return sl;
	}


	public int getPatternexit() {
		return patternexit;
	}


	public ArrayList<String> getPattern() {
		return pattern;
	}


	public int getTsaida() {
		return tsaida;
	}


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
		frame.setBounds(100, 100, 844, 541);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblDataDenicio = new JLabel("Start date:");
		lblDataDenicio.setBounds(6, 3, 118, 53);
		
		txtTxt = new JTextField();
		txtTxt.setBounds(110, 15, 103, 29);
		txtTxt.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblDataDenicio);
		frame.getContentPane().add(txtTxt);
		
		JLabel lblRentabilidade = new JLabel("ROI (%):");
		lblRentabilidade.setBounds(257, 3, 118, 53);
		frame.getContentPane().add(lblRentabilidade);
		
		textField_2 = new JTextField();
		textField_2.setBounds(353, 15, 444, 29);
		textField_2.setColumns(10);
		frame.getContentPane().add(textField_2);
		
		JLabel lblDataDeFim = new JLabel("Final date:");
		lblDataDeFim.setBounds(6, 56, 118, 50);
		frame.getContentPane().add(lblDataDeFim);
		
		textField = new JTextField();
		textField.setBounds(110, 65, 103, 29);
		textField.setColumns(10);
		frame.getContentPane().add(textField);
		
		JLabel lblNewLabel = new JLabel("Sell method");
		lblNewLabel.setBounds(257, 43, 92, 50);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblAcestxt = new JLabel("List of stocks:\n");
		lblAcestxt.setBounds(6, 106, 118, 48);
		frame.getContentPane().add(lblAcestxt);
		
		textField_1 = new JTextField();
		textField_1.setBounds(110, 118, 103, 29);
		textField_1.setColumns(10);
		frame.getContentPane().add(textField_1);
		
		JLabel lblLimiteSuperior = new JLabel("Higher Percentage:");
		lblLimiteSuperior.setBounds(257, 148, 118, 48);
		frame.getContentPane().add(lblLimiteSuperior);
		
		JButton btnRun = new JButton("Train");
		btnRun.setBounds(16, 251, 195, 29);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] args = new String[5];
				args[0] = txtTxt.getText();
				args[1] = textField.getText();
				args[2] = textField_1.getText();
				args[3] = textField_12.getText();
				args[4] = textField_13.getText();
				try {
					GA.MaximizeProfit.main(args);
					ArrayList<String> padrao = new ArrayList<String>();
					String resposta = String.format("%.3f",MaximizeProfit.getBestSolution().getFitnessValue());
					String saida = null;
					int tiposaida = MaximizeProfitFitnessFunction.getIntParameter(MaximizeProfit.getBestSolution(), 0);
					tsaida = tiposaida;
					switch (tiposaida){
				    case 0: {
				    	saida = MaximizeProfitFitnessFunction.getDias() +" Days.";
				    	time = MaximizeProfitFitnessFunction.getDias();
				    }
				    break;
				    case 1: {
				    	saida = String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit and" + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss";
				    	tk = MaximizeProfitFitnessFunction.getPtk();
						sl = MaximizeProfitFitnessFunction.getPsl();
				    }
				    break;
				    case 2: {
				    	saida = MaximizeProfitFitnessFunction.getJpadrao()  +" Uptrend Pattern Days.";
				    	patternexit = MaximizeProfitFitnessFunction.getJpadrao();
				    }
				    break;
				    case 3: {
				    	saida = MaximizeProfitFitnessFunction.getDias()  +" Days with " + String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit and " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss";
				    	time = MaximizeProfitFitnessFunction.getDias();
				    	tk = MaximizeProfitFitnessFunction.getPtk();
						sl = MaximizeProfitFitnessFunction.getPsl();
				    }
				    break;
				    case 4: {
				    	saida = MaximizeProfitFitnessFunction.getDias()  +" Days with " + MaximizeProfitFitnessFunction.getJpadrao() +" Uptrend Pattern Days.";
				    	patternexit = MaximizeProfitFitnessFunction.getJpadrao();
				    	time = MaximizeProfitFitnessFunction.getDias();
				    }
				    break;
				    case 5: {
				    	saida = String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit and " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss with "  + MaximizeProfitFitnessFunction.getJpadrao() +" Uptrend Pattern Days.";
				    	patternexit = MaximizeProfitFitnessFunction.getJpadrao();
				    	tk = MaximizeProfitFitnessFunction.getPtk();
						sl = MaximizeProfitFitnessFunction.getPsl();
				    }
				    break;
				    case 6: {
				    	saida = MaximizeProfitFitnessFunction.getDias()  +" Days with " + String.format("%.3f",MaximizeProfitFitnessFunction.getPtk())  +" TakeProfit and " + String.format("%.3f",MaximizeProfitFitnessFunction.getPsl()) +" StopLoss with "  + MaximizeProfitFitnessFunction.getJpadrao() +" Uptrend Pattern Days.";
				    	time = MaximizeProfitFitnessFunction.getDias();
				    	patternexit = MaximizeProfitFitnessFunction.getJpadrao();
				    	tk = MaximizeProfitFitnessFunction.getPtk();
						sl = MaximizeProfitFitnessFunction.getPsl();
				    }
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
				    	pattern = padrao;
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
		
		JButton btnTest = new JButton("Test\n");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] args = new String[3];
				args[0] = textField_1.getText();
				args[1] = textField_14.getText();
				args[2] = textField_15.getText();
				try {
					dados.Testar.setSaida(getTsaida());
					dados.Testar.setAcc(Integer.parseInt(textField_4.getText()));
					dados.Testar.setLiminf(Integer.parseInt(textField_5.getText()));
					dados.Testar.setLimsup(Integer.parseInt(textField_6.getText()));
					dados.Testar.setJanela(Integer.parseInt(textField_7.getText()));
					String dist = textField_8.getText().replace(",", ".");
					dados.Testar.setDist(Double.parseDouble(dist));
					dados.Testar.setPpis(Integer.parseInt(textField_9.getText()));
					dados.Testar.setRegras(Integer.parseInt(textField_10.getText()));
					//mudar isto
					dados.Testar.main(args,pattern,time,tk,sl,patternexit);
					double resposta = dados.Testar.getSaldofinal();
					textField_16.setText(String.format("%.3f",resposta));
				} catch (RowsExceededException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (WriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnTest.setBounds(396, 484, 195, 29);
		frame.getContentPane().add(btnTest);
		
		JLabel lblLimiteInferior = new JLabel("Lower Percentage:\n");
		lblLimiteInferior.setBounds(257, 203, 118, 33);
		frame.getContentPane().add(lblLimiteInferior);
		
		JLabel lblDistncia = new JLabel("Distance SAX:\n");
		lblDistncia.setBounds(257, 293, 118, 29);
		frame.getContentPane().add(lblDistncia);
		
		JLabel lblPpis = new JLabel("PIPs:");
		lblPpis.setBounds(257, 334, 118, 29);
		frame.getContentPane().add(lblPpis);
		
		JLabel lblPadro = new JLabel("Pattern:");
		lblPadro.setBounds(257, 407, 103, 16);
		frame.getContentPane().add(lblPadro);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(353, 52, 444, 33);
		frame.getContentPane().add(textField_3);
		
		JLabel lblNAces = new JLabel("Nº of Stocks:");
		lblNAces.setBounds(257, 97, 118, 48);
		frame.getContentPane().add(lblNAces);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(353, 107, 444, 29);
		frame.getContentPane().add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(396, 158, 401, 29);
		frame.getContentPane().add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(396, 205, 401, 29);
		frame.getContentPane().add(textField_6);
		
		JLabel lblJanela = new JLabel("Window size:");
		lblJanela.setBounds(257, 248, 118, 33);
		frame.getContentPane().add(lblJanela);
		
		JLabel lblRegras = new JLabel("Limit of Relations:");
		lblRegras.setBounds(257, 366, 118, 29);
		frame.getContentPane().add(lblRegras);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(387, 248, 410, 29);
		frame.getContentPane().add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(369, 293, 428, 29);
		frame.getContentPane().add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(301, 334, 496, 29);
		frame.getContentPane().add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(387, 366, 410, 29);
		frame.getContentPane().add(textField_10);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(315, 401, 482, 29);
		frame.getContentPane().add(textField_11);
		
		textField_12 = new JTextField();
		textField_12.setBounds(110, 158, 103, 28);
		frame.getContentPane().add(textField_12);
		textField_12.setColumns(10);
		
		JLabel lblPopulationsize = new JLabel("Population_size:");
		lblPopulationsize.setBounds(6, 148, 118, 48);
		frame.getContentPane().add(lblPopulationsize);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setBounds(110, 205, 103, 28);
		frame.getContentPane().add(textField_13);
		
		JLabel lblGenerations = new JLabel("Generations:");
		lblGenerations.setBounds(6, 193, 118, 48);
		frame.getContentPane().add(lblGenerations);
		
		JLabel lblTestStartDate = new JLabel("Test start date:");
		lblTestStartDate.setBounds(110, 442, 118, 53);
		frame.getContentPane().add(lblTestStartDate);
		
		textField_14 = new JTextField();
		textField_14.setColumns(10);
		textField_14.setBounds(216, 454, 103, 29);
		frame.getContentPane().add(textField_14);
		
		JLabel lblTestEndDate = new JLabel("Test end date:");
		lblTestEndDate.setBounds(325, 442, 118, 53);
		frame.getContentPane().add(lblTestEndDate);
		
		textField_15 = new JTextField();
		textField_15.setColumns(10);
		textField_15.setBounds(426, 454, 103, 29);
		frame.getContentPane().add(textField_15);
		
		JLabel lblResult = new JLabel("RESULT(%):");
		lblResult.setBounds(603, 442, 118, 53);
		frame.getContentPane().add(lblResult);
		
		textField_16 = new JTextField();
		textField_16.setColumns(10);
		textField_16.setBounds(685, 454, 69, 29);
		frame.getContentPane().add(textField_16);
		
	}
}
