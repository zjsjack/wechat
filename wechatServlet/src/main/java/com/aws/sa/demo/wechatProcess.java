package com.aws.sa.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class wechatProcess {
	/**
	 * 解析处理xml、获取智能回复结果（通过图灵机器人api接口）
	 * 
	 * @param xml
	 *            接收到的微信数据
	 * @return 最终的解析结果（xml格式数据）
	 */
	public String processWechatMag(String xml, int count, Socket socket) {

		/** 解析xml数据 */
		ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);

		String result = "";
		if ("text".endsWith(xmlEntity.getMsgType())) {
			// result = new
			// TulingApiProcess().getTulingResult(xmlEntity.getContent());

			result = "hello : " + count + xmlEntity.getContent();
			sendMsg(xmlEntity.getContent(), socket);
		}

		/**
		 * 此时，如果用户输入的是“你好”，在经过上面的过程之后，result为“你也好”类似的内容
		 * 因为最终回复给微信的也是xml格式的数据，所有需要将其封装为文本类型返回消息
		 */

		result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);

		return result;
	}

	public void sendMsg(String line, Socket socket) {
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output);
			writer.println(line);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
