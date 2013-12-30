import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.TreeSet;

//import net.paoding.analysis.analyzer.PaodingAnalyzer;



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

public class ImageCraw {
	public static void main(String[] args) throws IOException {		
			
		TreeSet<String> urlSet = new TreeSet<String>();
			
		//爬取首页
			/*try {
				
				org.jsoup.nodes.Document doc = Jsoup.connect("http://www.zhong-yao.net/shu/List_1466.htm").timeout(5000).get();
				
				Elements links = doc.select("a[target=_self]");
				
				for(Element e : links){
						String temp = e.attr("href");

						if(temp.length()==43)
							urlSet.add(temp);
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}*/
			
			
			
			//爬取除首页外其他页
			String url = "http://www.plantphoto.cn/tu/";
			
			//String suffix = ".htm";
			
			for(int i=1;i<=22;i++){
				
				try {
					
					org.jsoup.nodes.Document doc = Jsoup.connect(url+String.valueOf(i)).timeout(10000).get();
					
					Elements links = doc.select("img");
					String plantName = doc.select("span#Label15").text();
					
					for(Element e : links){

						String temp = e.attr("src");
						
						plantName = plantName.contains(" ")? plantName.substring(0,plantName.indexOf(" ")) : plantName;
						
						if(temp.contains("image2/b")){
							System.out.println(plantName + ":" + temp);
							
							String path = "D:/imageCraw/" + plantName;
							File fd = null;
							try {
								fd = new File(path);
								if (!fd.exists()) {
									fd.mkdirs();
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							} 

							URL imageUrl = new URL(temp);
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
			FileWriter fw = new FileWriter("D:\\1.txt");
					
					
					
			/*int count = 0;
			for(String str:urlSet){
				
				try {
					count++;
					org.jsoup.nodes.Document doc = Jsoup.connect(str).timeout(5000).get();
					
					String t = doc.title();
					System.out.println(t);
					fw.write(count+"."+t+"\r\n");
					String c = doc.select("td[vAlign=top]").eq(6).text();
					fw.write(c+"\r\n\n");
					//obj.writeIndex(t,c,str);
					System.out.println("已经完成第"+count+"篇鉴别文章的索引建立");
				} catch (IOException e) {
					
					e.printStackTrace();

				}
				
				
				
			}*/
		
			fw.close();
		}//end main

}