package Craw;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import SQL.MedPlantMap;

public class ImageCraw {
	public static void main(String[] args) throws IOException {		
			
		TreeSet<String> urlSet = new TreeSet<String>();

			String url = "http://www.plantphoto.cn/tu/";

			for(int i = 1 ; i <= 1542434 ; i++){
				System.out.println(i);
				try {
					
					org.jsoup.nodes.Document doc = Jsoup.connect(url+String.valueOf(i)).timeout(10000).get();
					
					//获取所有图片url
					Elements links = doc.select("img");
					
					//获取植物名字，可能存在别名
					String plantName = doc.select("span#Label15").text();

					for(Element e : links){

						String tempUrl = e.attr("src");

						//只处理植物图片
						if(tempUrl.contains("image2/b")){

							//去除部分名字存在括号与别名
							plantName = plantName.contains(" ")? plantName.substring(0,plantName.indexOf(" ")) : plantName;
							
							//获取植物对应ID，若不存在ID则为中文名
							String plantID = getKey(MedPlantMap.plantIDNameMap,plantName) == null ? plantName : getKey(MedPlantMap.plantIDNameMap,plantName).toString();
							String path = "G:/FTP/imageCraw/" + plantID;
							
							System.out.println(plantID + ":" + tempUrl);
							
							//创建相应植物目录，若不存在则新建
							File fd = null;
							try {
								fd = new File(path);
								if (!fd.exists()) {
									fd.mkdirs();
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							} 

							//保存图片至相应植物目录
							URL imageUrl = new URL(tempUrl);
							DataInputStream dis = new DataInputStream(imageUrl.openStream());
							
							FileOutputStream fos = new FileOutputStream(new File(path + "/" + i + ".jpg"));
							
							byte[] buffer = new byte[1024];
							int length;
							
							while((length = dis.read(buffer)) > 0)
								fos.write(buffer, 0, length);
							
							dis.close();
							fos.close();
							
						}
					}
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
				
				
			}

		}//end main
	
	//根据value得到key，用于根据plantname获得相关植物id
	public static Object getKey(Map map,String value){
		
		for(Object getKey : map.keySet())
			if(map.get(getKey).equals(value))
				return (Long) getKey;
		
		return null;
	}

}