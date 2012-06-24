package org.fotworld.app001;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class RssReaderActivity extends ListActivity {
	public static final String RSS_FEED_URL = "https://sites.google.com/a/fotworld.org/fotworld/activity.xml";
	public static final String RSS_FEED_URL2 = "https://sites.google.com/site/kivajapanrssreader/news/posts.xml";
	public static final String RSS_FEED_URL3 = "https://sites.google.com/a/fotworld.org/fotworld/hot-news-1/posts.xml";
	private ArrayList<Item> mItems;
	private RssListAdapter mAdapter;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
      
		// Itemオブジェクトを保持するためのリストを生成し、アダプタに追加する
        mItems = new ArrayList<Item>();
		mAdapter = new RssListAdapter(this, mItems);
		
		RssParserTask task = new RssParserTask(this, mAdapter);
		task.execute(RSS_FEED_URL);

    }
        
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Item item = mItems.get(position);
		Uri uri = Uri.parse(item.getLink());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}