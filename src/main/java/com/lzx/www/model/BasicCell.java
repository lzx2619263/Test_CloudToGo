package com.lzx.www.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BasicCell {
	
	//默认http请求端口为8080
	public static final String HTTPPORT = "8080";
	//默认http请求端口为6666
	public static final String TCPPORT = "6666";
	//获取组件环境变量
	public Map<String, String> envMap = System.getenv();
	
//	public BasicCell() {
//		if(ts == null) {
//			ts = new TcpServer();
//			ts.init(Integer.valueOf(TCPPORT));
//		}
//	}
	
	
	//获取本机服务名称
	public String getServerName() {
		String serverName = envMap.get("HOSTNAME");
		return serverName;
	}
	
	
	//获取HTTP连线组件信息(连线的环境变量必须满足HTTPSERVER+数字样式)
	public List<String> getChildHttpCell() {
		List<String> cellList = new ArrayList<String>();
		String cell ="";
		for(String key : envMap.keySet()) {
			if(key.contains("HTTPSERVER")) {
				cell = envMap.get(key).toUpperCase().replace('-', '_');
				cellList.add(cell);
			}
		}
		return cellList;
	}
	
	
	//获取TCP连线组件信息(连线的环境变量必须满足TCPSERVER+数字样式)
	public List<String> getChildTcpCell() {
		List<String> cellList = new ArrayList<String>();
		String cell ="";
		for(String key : envMap.keySet()) {
			if(key.contains("TCPSERVER")) {
				cell = envMap.get(key).toUpperCase().replace('-', '_');
				cellList.add(cell);
			}
		}
		return cellList;
	}
	
	
	//发送TCP请求，并获取服务端响应
	public String tcpRequest(String host, int port, String serverName) {
		TcpClient tcl = new TcpClient();
		tcl.init(host, port);
		String msg = tcl.receiveMsg;
		while(msg == null) {
			System.out.println("等待接收");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			msg = tcl.receiveMsg;
		}
		return msg;
	}
	
	

	//发送HTTP请求，并获取服务端服务名称
	public List<String> httpGetRequest(String url, String ServerName) {
		List<String> nextHostNames = new ArrayList<String>();
		try {
			Document doc=Jsoup.connect(url).get();
			Elements elements=doc.getElementsByClass("path"); //获取返回的页面jsp中class=path的内容
			for(Element element : elements) {
				nextHostNames.add(element.text());
			}
		} catch (IOException e) {
			e.printStackTrace();
			nextHostNames.add(ServerName + " Exception");
		}
		return nextHostNames;
	}
	
	
	//发送HTTP请求到连线的组件
	public List<String> requestToChildHttpCell() {
		List<String> childCellList = getChildHttpCell();
		List<String> childCellRes = new ArrayList<String>(); 
		List<String> partCellRes = new ArrayList<String>();
		if(!childCellList.isEmpty()) {
			for(String childCell : childCellList) {
				//如果组件同集群则从环境变量中取IP和端口，如果跨集群则使用service名称和默认8080端口
				String nextHttpHost = (null == envMap.get(childCell + "_SERVICE_HOST")?childCell.toLowerCase():envMap.get(childCell + "_SERVICE_HOST"));
//				System.out.println(nextHttpHost);
				String nextHttpPort = (null == envMap.get(childCell + "_SERVICE_PORT")?HTTPPORT:envMap.get(childCell + "_SERVICE_PORT"));
//				System.out.println(nextHttpPort);
				String nextHttpUrl = "http://" + nextHttpHost + ":" + nextHttpPort;
				System.out.println(nextHttpUrl);
				partCellRes = httpGetRequest(nextHttpUrl, childCell.toLowerCase());
				for(String res : partCellRes) {
					childCellRes.add(res); // 保存当前组件连线下的组件的jsp中class=path的内容
				}
			}
		}
		return childCellRes; 
	}
	
	
	//发送TCP请求到连线的组件
	public List<String> requestToChildTcpCell() {
		List<String> childCellList = getChildTcpCell();
		List<String> childCellRes = new ArrayList<String>(); 
//		ArrayList<String> partCellRes = new ArrayList<String>();
		String receive = "";
		if(!childCellList.isEmpty()) {
			for(String childCell : childCellList) {
				//如果组件同集群则从环境变量中取IP和端口，如果跨集群则使用service名称和默认8080端口
				String nextTcpHost = (null == envMap.get(childCell + "_SERVICE_HOST")?childCell.toLowerCase():envMap.get(childCell + "_SERVICE_HOST"));
				System.out.println("TCP Host: " + nextTcpHost);
				String nextTcPort = (null == envMap.get(childCell + "_SERVICE_PORT_TCP_" + TCPPORT)?TCPPORT:envMap.get(childCell + "_SERVICE_PORT_TCP_" + TCPPORT));
				System.out.println("TCP Port:" + nextTcPort);
				receive = tcpRequest(nextTcpHost, Integer.valueOf(nextTcPort), childCell.toLowerCase());
				childCellRes.add(receive);
			}
		}
		return childCellRes; 
	}
	
	
}
