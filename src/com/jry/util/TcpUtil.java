package com.jry.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(TcpUtil.class);

	public static String sendMessage(String ip, int port, String message) {
		System.out.println(ip);
		System.out.println(port);
		System.out.println(message);
		String receiveDataStr = null;
		try {
			Socket s = new Socket(ip, port);
			s.setKeepAlive(true);
			try {
				if (s.isClosed() == false) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(s.getInputStream()));
					BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(s.getOutputStream()));
					bw.write(message);
					bw.flush();
					receiveDataStr = br.readLine();
					br.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				s.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return receiveDataStr;
	}

	public static void main(String[] args) {
		String bv = TcpUtil.sendMessage("192.168.100.97", 55555,
				"@0001,18,01!");
		logger.info(bv);
		System.out.println(bv);
	}

	public static void stopAll() {
		String bv = TcpUtil.sendMessage("192.168.1.220", 55555, "@0001,29,02!");
		logger.info(bv);
		int sindex = bv.indexOf('(');
		int eindex = bv.indexOf(')');
		while (true) {
			if (sindex >= 0 && eindex >= 0) {
				String val = bv.substring(sindex + 1, eindex);
				String[] vals = val.split(",");
				if (vals.length >= 4) {
					String linktype = vals[0];
					String address = vals[1];
					if (linktype.equals("00")) {
						String stopCommand = "@0001,29,01," + linktype + ","
								+ address + "!";
						String cv = TcpUtil.sendMessage("192.168.1.220", 55555,
								stopCommand);
						logger.info(cv);
					} else {
						String tongdao = vals[2];
						String stopCommand = "@0001,29,01," + linktype + ","
								+ address + "," + tongdao + "!";
						String cv = TcpUtil.sendMessage("192.168.1.220", 55555,
								stopCommand);
						logger.info(cv);
					}
				}
				bv = bv.replace("(" + val + ")", "");
				sindex = bv.indexOf('(');
				eindex = bv.indexOf(')');
			} else {
				break;
			}
		}
	}
}
