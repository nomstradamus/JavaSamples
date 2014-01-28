import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpConnectionPooling {

	public static void main(String[] args) throws IOException {

		ArrayList<String> yrls = new ArrayList<String>();
		FileReader reader = new FileReader(new File("C:\\url.txt"));
		BufferedReader buffReader = new BufferedReader(reader);
		String temp = "";
		while ((temp = buffReader.readLine()) != null) {
			System.out.println(temp);
			yrls.add(temp);
		}
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(10);
		cm.setDefaultMaxPerRoute(10);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(cm).build();

		long time1 = System.nanoTime();
		for (String url : yrls) {
			// HttpClient httpClient = new DefaultHttpClient();
			HttpHead get = new HttpHead(url);
			HttpResponse resp = httpClient.execute(get);
			System.out.println(url + " :::"
					+ resp.getStatusLine().getStatusCode());

		}
		long time2 = System.nanoTime();

		System.out.println("Difference = " + (time2 - time1));
		httpClient.close();

	}

}
