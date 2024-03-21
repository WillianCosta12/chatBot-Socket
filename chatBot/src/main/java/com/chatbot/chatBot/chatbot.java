package com.chatbot.chatBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.alicebot.ab.Bot;
	import org.alicebot.ab.Chat;
	import org.alicebot.ab.History;
	import org.alicebot.ab.MagicBooleans;
	import org.alicebot.ab.MagicStrings;

	public class chatbot {
		private static final boolean TRACE_MODE = false;
		static String botName = "super";

		public static void main(String[] args) {
			try {

				String resourcesPath = getResourcesPath();
				System.out.println(resourcesPath);
				MagicBooleans.trace_mode = TRACE_MODE;
				Bot bot = new Bot("super", resourcesPath);
				Chat chatSession = new Chat(bot);
				bot.brain.nodeStats();
				String textLine = "";
				
				ServerSocket servidor = new ServerSocket(9999);

				System.out.println("Servidor em operação!");

				while (true) {
					
					Socket cliente = servidor.accept();			
					
					System.out.println("Conexão Realizada");
					
					PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
					BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

					do {
						saida.println("Human : ");
						textLine = entrada.readLine();
						if ((textLine == null) || (textLine.length() < 1))
							textLine = MagicStrings.null_input;
						if (textLine.equals("q")) {
							System.exit(0);
						} else if (textLine.equals("wq")) {
							bot.writeQuit();
							System.exit(0);
						} else {
							String request = textLine;
							if (MagicBooleans.trace_mode)
								System.out.println(
										"STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0)
												+ ":TOPIC=" + chatSession.predicates.get("topic"));
							String response = chatSession.multisentenceRespond(request);
							while (response.contains("&lt;"))
								response = response.replace("&lt;", "<");
							while (response.contains("&gt;"))
								response = response.replace("&gt;", ">");
							saida.println("Robot : " + response);
						}
					}while(!textLine.trim().equals("bye") );

					
					cliente.close();
					servidor.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static String getResourcesPath() {
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			path = path.substring(0, path.length() - 2);
			System.out.println(path);
			String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
			return resourcesPath;
		}

	}
