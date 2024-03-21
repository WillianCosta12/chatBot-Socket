package com.chatbot.chatBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

	public static void main(String[] args) {
		try {
			
			final Socket cliente = new Socket("127.0.0.1", 9999);
			
			new Thread() {
				@Override
				public void run() {
					try {
						BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
						
						while(true) {
							String msg = leitor.readLine();
							System.out.println(msg);
						}
						
					}catch (Exception e) {
						System.out.println("Erro Encontrado: " + e);
						e.printStackTrace();
					}
				}
			}.start();
			
			PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
			BufferedReader leitorT = new BufferedReader(new InputStreamReader(System.in));
			
			String str;
			
			do {
				str = leitorT.readLine();
				saida.println(str);
			} while ( !str.trim().equals("bye") );
			cliente.close();
			
		}catch(Exception e) {
			System.out.println("Erro Encontrado: " + e);
	}
  }
}
