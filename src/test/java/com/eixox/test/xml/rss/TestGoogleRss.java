package com.eixox.test.xml.rss;

import org.junit.Assert;
import org.junit.Test;

import com.eixox.xml.rss.RssChannel;
import com.eixox.xml.rss.RssItem;

public class TestGoogleRss {

	@Test
	public void readNewsRss() {

		RssChannel channel = new RssChannel();

		String channelUrl = "https://news.google.com.br/news?cf=all&hl=pt-BR&pz=1&ned=pt-BR_br&output=rss";

		Assert.assertTrue(
				channelUrl,
				channel.parse(channelUrl));

		System.out.println(channel.title);

		for (RssItem item : channel.items) {
			System.out.println(item.description);
		}

	}
}
