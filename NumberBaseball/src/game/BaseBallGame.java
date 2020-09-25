package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.JOptionPane;

public class BaseBallGame extends JFrame implements ActionListener{
	private JButton start,exit,re_start,correct;
	private JTextArea output;
	private JTextField input;
	private JLabel label;
	private JPanel text_area,text_field,button,down;
	private JScrollPane scroll_bar;
	
	static final int chance=10;
	private int[] answer;
	private int[] inputNumber;
	private int count,strike,ball;
	private Random rand;
	
	public BaseBallGame() {
		answer=new int[3];
		inputNumber=new int[3];
		count=0;
		strike=0;
		ball=0;
		rand=new Random();
		
		displayGame();
	}
	
	private void displayGame() {
		setTitle("숫자 야구 게임");
		
		start=new JButton("시작");
		start.setPreferredSize(new Dimension(130,100));
		start.setFont(new Font("바탕",Font.BOLD,30));
		
		re_start=new JButton("지우기");
		re_start.setPreferredSize(new Dimension(130,100));
		re_start.setFont(new Font("바탕",Font.BOLD,30));
		
		correct=new JButton("정답");
		correct.setPreferredSize(new Dimension(130,100));
		correct.setFont(new Font("바탕",Font.BOLD,25));
		
		exit=new JButton("나가기");
		exit.setPreferredSize(new Dimension(130,100));
		exit.setFont(new Font("바탕",Font.BOLD,30));
		
		output=new JTextArea();
		output.setEditable(false);
		
		scroll_bar=new JScrollPane(output);
		scroll_bar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		input=new JTextField(40);
		input.setEditable(false);
		input.setPreferredSize(new Dimension(40,40));
		
		label=new JLabel("입력: ");
		label.setForeground(Color.YELLOW);
		label.setFont(new Font("바탕",Font.BOLD,35));
		
		down=new JPanel();
		text_area=new JPanel();
		text_field=new JPanel();
		button=new JPanel();
		
		text_area.setLayout(new BorderLayout(20,20));
		text_area.add("North",new JLabel());
		text_area.add("South",new JLabel());
		text_area.add("East",new JLabel());
		text_area.add("West",new JLabel());
		text_area.add("Center",scroll_bar);
		text_area.setBackground(Color.BLUE);
		
		text_field.setLayout(new FlowLayout());
		text_field.add(label);
		text_field.add(input);
		text_field.setBackground(Color.BLUE);
		text_field.setPreferredSize(new Dimension(600,40));
		
		
		button.setLayout(new FlowLayout());
		button.add(new JLabel());
		button.add(start);
		button.add(re_start);
		button.add(correct);
		button.add(exit);
		button.add(new JLabel());
		button.setPreferredSize(new Dimension(600,150));
		
		down.setLayout(new FlowLayout());
		down.add(text_field);
		down.add(button);
		down.setBackground(Color.BLUE);
		
		setLayout(new BorderLayout());
		add("Center",text_area);
		add("South",down);
		
		setBounds(400,400,600,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		startState();
	}
	
	private void startState() {
		start.addActionListener(this);
		re_start.addActionListener(this);
		correct.addActionListener(this);
		exit.addActionListener(this);
		input.addActionListener(this);
		
		re_start.setEnabled(false);
		correct.setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==start) {
			input.setEnabled(true);
			input.setEditable(true);
			re_start.setEnabled(true);
			correct.setEnabled(true);
			output.setText("");
			count=0;
			RandNum();
		}
		else if(obj==re_start) {
			re_start.setEnabled(false);
			input.setText("");
			output.setText("");
		}
		else if(obj==correct) {
			output.append("포기하셨네요... 정답은 "+answer[0]+answer[1]+answer[2]+"입니다!\n");
			correct.setEnabled(false);
			input.setEditable(false);
			return;
		}
		else if(obj==exit) {
			System.exit(1);
		}
		else if(obj==input) {
			ball=0;
			strike=0;
			
			String ans=input.getText();
			if(!ans.matches("[\\d]{3}")){
				JOptionPane.showMessageDialog(this,"세 자리 수를 입력하세요");
				input.setText("");
				return;
			}
			int number=Integer.parseInt(ans);
			
			inputNumber[0]=number/100;
			inputNumber[1]=(number%100)/10;
			inputNumber[2]=(number%100)%10;
			
			for(int i=0;i<inputNumber.length-1;i++) {
				for(int j=i+1;j<inputNumber.length;j++) {
					if(inputNumber[i]==inputNumber[j]) {
						JOptionPane.showMessageDialog(this,"중복되지 않는 수를 입력해주세요");
						input.setText("");
						return;
					}
				}
			}
			
			count++;
			
			for(int i=0;i<answer.length;i++) {
				for(int j=0;j<inputNumber.length;j++) {
					if(answer[i]==inputNumber[j]) {
						if(i==j) {
							strike++;
						}
						else {
							ball++;
						}
					}
				}
			}
			
			if(count==chance) {
				if(strike==3) {
					win();
				}
				else {
					output.append(count+"회: "+inputNumber[0]+inputNumber[1]+inputNumber[2]+"\n");
					output.append(strike+"스트라이크, "+ball+"볼\n\n");
					output.append(chance+"번의 기회를 소진했습니다. 다시 도전하세요!!");
				}
				return;
			}
			
			output.append(count+"회: "+inputNumber[0]+inputNumber[1]+inputNumber[2]+"\n");
			
			if(strike==3) {
				win();
				return;
			}
			else if(strike==0&&ball==0) {
				output.append("아웃입니다.\n\n");
				input.setText("");
				return;
			}
			
			output.append(strike+"스트라이크, "+ball+"볼\n\n");
			
			input.setText("");
		}
	}
	
	private void RandNum() {
		for(int i=0;i<answer.length;i++) {
			answer[i]=rand.nextInt(10);
			for(int j=0;j<i;j++) {
				if(answer[i]==answer[j]) {
					i--;
					continue;
				}
			}
		}
	}
	
	private void win() {
		output.append("축하합니다!~~ 정답은 "+answer[0]+answer[1]+answer[2]+"입니다.\n");
		output.append(count+"회만에 성공하셨습니당~~!! 다시 하고 싶으시면 '시작'버튼을 눌러주세요!!\n");
		input.setEditable(false);
		input.setEnabled(false);
		input.setText("");
		correct.setEnabled(false);
		return;
	}
}
