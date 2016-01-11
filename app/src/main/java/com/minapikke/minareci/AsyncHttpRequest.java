package com.minapikke.minareci;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Yuki on 2015/07/05.
 */
public class AsyncHttpRequest extends AsyncTask<Void, Void, Void> {

    static private int pageNum = 1;

    private Activity mainActivity;
    private String searchWord;
    private ArrayList<MinaReciItem> reciItemArray;
    private ArrayList<MinaReciItem> reciItemArrayPrev;

    public AsyncHttpRequest(Activity activity) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;

        this.reciItemArray = new ArrayList<MinaReciItem>();
        this.reciItemArrayPrev = new ArrayList<MinaReciItem>();

    }

    public String getSearchWord(){ return this.searchWord; }
    public void setSearchWord(String word){ this.searchWord = word; }

    public ArrayList<MinaReciItem> getReciItemArray() { return reciItemArray; }
    public void setReciItemArray(ArrayList<MinaReciItem> reciItemArray) { this.reciItemArray = reciItemArray; }

    public ArrayList<MinaReciItem> getReciItemArrayPrev() { return reciItemArrayPrev; }
    public void setReciItemArrayPrev(ArrayList<MinaReciItem> reciItemArrayPrev) { this.reciItemArrayPrev = reciItemArrayPrev; }

    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    protected Void doInBackground(Void... params) {
        String urlString = "http://cookpad.com/search/" + getSearchWord() + "?page=" + String.valueOf(this.pageNum ++);
        String uri = urlString;

        try {
            uri = new URI(urlString).toASCIIString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Log.d(MinaReciMainAct.class.getSimpleName(), uri);

        try {
            Document document = Jsoup.connect(uri).get();
            Elements elementsByTitle = document.getElementsByClass("recipe_title");
            Elements elementsByLink  = document.getElementsByClass("recipe_link");
            Elements elementsByImage = document.getElementsByClass("card_image");
            MinaReciItem reciItem;
            int max_reci = 7;

//            Log.d(MinaReciMainAct.class.getSimpleName(), elementsByImage.toString());
//            Log.d(MinaReciMainAct.class.getSimpleName(), elementsByLink.toString());

            if(max_reci > elementsByTitle.size()){ max_reci = elementsByTitle.size(); }

            for(int i = 0; max_reci > i; i ++){
                reciItem = new MinaReciItem();
                reciItem.setReciTitle(elementsByTitle.get(i).text());
                this.reciItemArray.add(reciItem);
            }

            int i = 0;

            for(Element imgTags:elementsByImage){
                String imgUrlStr;
                URL imageUrl;
                InputStream imageIs;

                if(i >= max_reci) break;
                imgUrlStr = imgTags.getElementsByTag("img").attr("src").toString();
                Log.d(MinaReciMainAct.class.getSimpleName(), imgUrlStr);
                imageUrl = new URL(imgUrlStr);
                imageIs = imageUrl.openStream();
                this.reciItemArray.get(i).setReciImage(BitmapFactory.decodeStream(imageIs));
                i ++;
            }

            i = 0;

            for(Element aTags:elementsByLink){
                String reciUrlStr;

                if(i >= max_reci) break;
                reciUrlStr = aTags.getElementsByTag("a").attr("href").toString();
                Log.d(MinaReciMainAct.class.getSimpleName(), reciUrlStr);
                this.reciItemArray.get(i).setReciUrl(reciUrlStr);
                i ++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

//            Log.d(MinaReciMainAct.class.getSimpleName(), String.valueOf(imageArray.size()));
//        Log.d(MinaReciMainAct.class.getSimpleName(), elementsByTitle.toString());
//        System.out.println(elementsByClass.toString());

        return null;
    }


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(Void result) {
        int viewId;
        String resViewName;
        CheckBox checkBox;
        TextView textView;
        ImageView imageView;
        Resources res = mainActivity.getResources();

        for(int i = 0; i < this.reciItemArray.size(); i++){
            resViewName = "checkBox" + (i + 1);
            viewId = res.getIdentifier(resViewName, "id", mainActivity.getPackageName());
            checkBox = (CheckBox) mainActivity.findViewById(viewId);

            if(!checkBox.isChecked()){
                resViewName = "textView" + (i + 1);
                viewId = res.getIdentifier(resViewName, "id", mainActivity.getPackageName());
                textView = (TextView) mainActivity.findViewById(viewId);
                textView.setText(this.reciItemArray.get(i).getReciTitle());

                resViewName = "imageView" + (i + 1);
                viewId = res.getIdentifier(resViewName, "id", mainActivity.getPackageName());
                imageView = (ImageView) mainActivity.findViewById(viewId);
                imageView.setImageBitmap(this.reciItemArray.get(i).getReciImage());
            }else{
                this.reciItemArray.set(i, this.reciItemArrayPrev.get(i));
            }
        }
    }
}
