package main.tal.root;
import main.tal.metrics.GenericMetrics;
import main.tal.metrics.TimeCalculator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import main.tal.db.TwitterLink;

import java.io.IOException;

public class JsoupParser {

	private GenericMetrics metrics;

	public JsoupParser() {
		metrics = new GenericMetrics
				("TWEETS/MANUFACTOR", "TimeToParseTweet",
						"TimeInterval", "seconds");
	}

	public TwitterLink parseTweetWithMetrics(SqsReceiver.TwitterSqsObject sqsObj) {
		TimeCalculator tc = new TimeCalculator();
		tc.start();
		TwitterLink link = parseTweet(sqsObj);
		long interval = tc.stop();
		metrics.sendMetricData((double) interval);
		return link;
	}

	public TwitterLink parseTweet(SqsReceiver.TwitterSqsObject sqsObj) {
		try {
			Document doc = Jsoup.connect(sqsObj.url).get();
			String content = doc.text();
			String url = doc.baseUri();
			TwitterLink twitter_link = new TwitterLink();
			twitter_link.setContent(content);
			twitter_link.setTrack(sqsObj.getTrack());
			twitter_link.setUrl(url);
			twitter_link.setTitle(doc.title());
			Elements metaTags = doc.getElementsByTag("meta");
			metaTags.forEach( element -> {
				String meta_name = element.attr("name");
				String meta_content = element.attr("content");
				if (meta_name.endsWith(":description")) {
					twitter_link.setDescription(meta_content);
				}
			});
			return twitter_link;
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}
}
