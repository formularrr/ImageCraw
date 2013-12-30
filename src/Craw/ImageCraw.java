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
					
					//��ȡ����ͼƬurl
					Elements links = doc.select("img");
					
					//��ȡֲ�����֣����ܴ��ڱ���
					String plantName = doc.select("span#Label15").text();

					for(Element e : links){

						String tempUrl = e.attr("src");

						//ֻ����ֲ��ͼƬ
						if(tempUrl.contains("image2/b")){

							//ȥ���������ִ������������
							plantName = plantName.contains(" ")? plantName.substring(0,plantName.indexOf(" ")) : plantName;
							
							//��ȡֲ���ӦID����������ID��Ϊ������
							String plantID = getKey(MedPlantMap.plantIDNameMap,plantName) == null ? plantName : getKey(MedPlantMap.plantIDNameMap,plantName).toString();
							String path = "G:/FTP/imageCraw/" + plantID;
							
							System.out.println(plantID + ":" + tempUrl);
							
							//������Ӧֲ��Ŀ¼�������������½�
							File fd = null;
							try {
								fd = new File(path);
								if (!fd.exists()) {
									fd.mkdirs();
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							} 

							//����ͼƬ����Ӧֲ��Ŀ¼
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
	
	//����value�õ�key�����ڸ���plantname������ֲ��id
	public static Object getKey(Map map,String value){
		
		for(Object getKey : map.keySet())
			if(map.get(getKey).equals(value))
				return (Long) getKey;
		
		return null;
	}

}