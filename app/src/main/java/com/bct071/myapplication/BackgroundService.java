package com.bct071.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Prasidha Karki on 6/20/2016.
 */
public class BackgroundService extends Service {

    private boolean isRunning;

    private Thread backgroundThread;
     MasterDbAdapter masterDbAdapter; //=new MasterDbAdapter(getApplicationContext());
    Context context = this;
    private static int NOTIFY_ME_ID=1;

    private static final int NID_POEM=1,NID_STORY=2,NID_ESSAY=3,NID_BALSANSAR=4,NID_GAJAL=5;




    private static  int poem_number,story_number,balsansar_number,essay_number,gajal_number,other_number;
    private static int fpoem_number,fstory_number,fbalsansar_number,fessay_number,fgajal_number;
    private static  int cpoem_number,cstory_number,cbalsansar_number,cessay_number,cgajal_number,cother_number;

    private static final String SQLITE_TABLE_POEMS="Poem";
    private static final String SQLITE_TABLE_STORIES="Story";
    private static final String SQLITE_TABLE_ESSAYS="Essay";
    private static final String SQLITE_TABLE_BALSANSARS="Balsansar";
    private static final String SQLITE_TABLE_GAJALS="Gajal";
    private static final String SQLITE_TABLE_OTHERS ="Other";


    private static final String mainurl="http://www.writenepal.com/archives/category/";
    private static final String forpoem="poems";
    private static final String forstory="stories";
    private static final String foressay="essays";
    private static final String forbalsansar="balsansar";
    private static final String forgajal="gajals";
    private static final String forother="others";


   @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        this.context = getApplicationContext();
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        this.masterDbAdapter=new MasterDbAdapter(this.context);

        //masterDbAdapter.open();

    //    poem_number=masterDbAdapter.getRowCount(SQLITE_TABLE_POEMS);







    }


    private Runnable myTask = new Runnable() {
        public void run() {

         //   masterDbAdapter.open();
         //   new SyncAll().execute();

            Connection conpoem = null, constory = null, conessay = null, conbalsansaar = null, congajal = null,conother= null;
            //masterDbAdapter=new MasterDbAdapter(getApplicationContext());
            masterDbAdapter.open();

            conpoem = Jsoup.connect(mainurl + forpoem)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);

            constory = Jsoup.connect(mainurl + forstory)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            conbalsansaar = Jsoup.connect(mainurl + forbalsansar)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            conessay = Jsoup.connect(mainurl + foressay)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            congajal = Jsoup.connect(mainurl + forgajal)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            conother = Jsoup.connect(mainurl + forother)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);

            Log.d("Status","Ready to fetch data");
            try {
                // Connect to the Website URL

                Document docpoem = conpoem.get();
                Document docstory = constory.get();
                Document docessay = conessay.get();
                Document docgajal = congajal.get();
                Document docbalsansar = conbalsansaar.get();
                Document docother=conother.get();

                //for poem
                Elements titlespoem = docpoem.select("div.span8 h2");
                Elements descrispoem = docpoem.select(".span8 p");
                Elements linkpoem = docpoem.select(".entry.archive-box h2>a");
                //for essay
                Elements titlesessay= docessay.select("div.span8 h2");
                Elements descrisessay = docessay.select(".span8 p");
                Elements linkessay = docessay.select(".entry.archive-box h2>a");
                //for gajal
                Elements titlesgajal = docgajal.select("div.span8 h2");
                Elements descrisgajal = docgajal.select(".span8 p");
                Elements linkgajal = docgajal.select(".entry.archive-box h2>a");
                //for bal sansarr
                Elements titlesbalsansar = docbalsansar.select("div.span8 h2");
                Elements descrisbalsansar = docbalsansar.select(".span8 p");
                Elements linkbalsansar = docbalsansar.select(".entry.archive-box h2>a");
                //for storys
                Elements titlesstory = docstory.select("div.span8 h2");
                Elements descrisstory = docstory.select(".span8 p");
                Elements linkstory = docstory.select(".entry.archive-box h2>a");

                //for other
                Elements titlesother = docother.select("div.span8 h2");
                Elements descrisother = docother.select(".span8 p");
                Elements linkother = docother.select(".entry.archive-box h2>a");


                fpoem_number = titlespoem.size();
                fstory_number=titlesstory.size();
                fgajal_number=titlesgajal.size();
                fessay_number=titlesessay.size();
                fbalsansar_number=titlesbalsansar.size();


                //Clean all data
                //dbHelper.deleteAllCountries();

                if (titlespoem != null && descrispoem!=null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_POEMS);
                    if(titlespoem.size()==descrispoem.size())
                    {
                        cpoem_number=titlespoem.size();
                    }
                    else{
                        cpoem_number=descrispoem.size();

                    }

                    for (int i = 0; i < cpoem_number; i++) {
                        Log.i("data got", trimme(descrispoem.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlespoem.get(i).text();
                        String b = trimme(descrispoem.get(i).text(), 100);
                        String c = linkpoem.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_POEMS, c, a, b);
                    }
                }



                if (titlesstory != null && descrisstory!=null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_STORIES);
                    if(titlesstory.size()==descrisstory.size())
                    {
                        cstory_number=titlesstory.size();
                    }
                    else{
                        cstory_number=descrisstory.size();

                    }
                    for (int i = 0; i < cstory_number; i++) {
                        Log.i("data got", trimme(descrisstory.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesstory.get(i).text();
                        String b = trimme(descrisstory.get(i).text(), 100);
                        String c = linkstory.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_STORIES, c, a, b);
                    }

                }
                if (titlesessay != null && descrisessay!=null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_ESSAYS);
                    if(titlesessay.size()==descrisessay.size())
                    {
                        cessay_number=titlesessay.size();
                    }
                    else{
                        cessay_number=descrisessay.size();

                    }
                    for (int i = 0; i < cessay_number; i++) {
                        Log.i("data got", trimme(descrisessay.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesessay.get(i).text();
                        String b = trimme(descrisessay.get(i).text(), 100);
                        String c = linkessay.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_ESSAYS, c, a, b);
                    }


                }if (titlesbalsansar != null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_BALSANSARS);
                    if(titlesbalsansar.size()==descrisbalsansar.size())
                    {
                        cbalsansar_number=titlesbalsansar.size();
                    }
                    else{
                        cbalsansar_number=descrisbalsansar.size();

                    }
                    for (int i = 0; i < cbalsansar_number; i++) {
                        Log.i("data got", trimme(descrisbalsansar.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesbalsansar.get(i).text();
                        String b = trimme(descrisbalsansar.get(i).text(), 100);
                        String c = linkbalsansar.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_BALSANSARS, c, a, b);
                    }

                }
                if (titlesgajal != null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_GAJALS);
                    if(titlesgajal.size()==descrisgajal.size())
                    {
                        cgajal_number=titlesgajal.size();
                    }
                    else{
                        cgajal_number=descrisgajal.size();

                    }
                    for (int i = 0; i < cgajal_number; i++) {
                        Log.i("data got", trimme(descrisgajal.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesgajal.get(i).text();
                        String b = trimme(descrisgajal.get(i).text(), 100);
                        String c = linkgajal.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_GAJALS, c, a, b);
                    }

                }

                if (titlesother != null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_OTHERS);
                    if(titlesother.size()==descrisother.size())
                    {
                        cother_number=titlesother.size();
                    }
                    else{
                        cother_number=descrisother.size();

                    }
                    for (int i = 0; i < cother_number; i++) {
                        Log.i("data got", trimme(descrisother.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesother.get(i).text();
                        String b = trimme(descrisother.get(i).text(), 100);
                        String c = linkother.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_OTHERS, c, a, b);
                    }

                }


                Log.i("Tag", "Database is working");


            } catch (IOException e1) {
                e1.printStackTrace();
            }


            if (poem_number < cpoem_number) {
                createNotification("Check out some new poem",NID_POEM);
            } else {
                //createNotification("No new poem found",NID_POEM);
                Log.d("Notification","Data not updated");
            }
            if (story_number < cstory_number) {
                createNotification("New story has just been updated",NID_STORY);
            } else {
                //createNotification("No new story found",NID_STORY);
                Log.d("Notification","Data not updated");
            }
            if (essay_number < cessay_number) {
                createNotification("Some new essay has just been published",NID_ESSAY);
            } else {
                //createNotification("No new essay found",NID_ESSAY);
                Log.d("Notification","Data not updated");
            }
            if (balsansar_number < cbalsansar_number) {
                createNotification("BalSansar contents is updated",NID_BALSANSAR);
            } else {
                //createNotification("No new balsansar found",NID_BALSANSAR);
                Log.d("Notification","Data not updated");
            }
            if (gajal_number < cgajal_number) {
                createNotification("Check out recent uploaded gajals ",NID_GAJAL);
            } else {
                Log.d("Notification","Data not updated");
                //createNotification("No new gajal found",NID_GAJAL);
            }if (other_number< cother_number) {
                createNotification("New contents have been updated in Others category ",NID_GAJAL);
            } else {
                Log.d("Notification","Data not updated");
                //createNotification("No new gajal found",NID_GAJAL);
            }

           masterDbAdapter.close();
            // Do something here
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
        //masterDbAdapter.close();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //masterDbAdapter=new com.bct071.myapplication.MasterDbAdapter(getApplicationContext());
        masterDbAdapter.open();


        poem_number=masterDbAdapter.getRowCount(SQLITE_TABLE_POEMS);
        story_number=masterDbAdapter.getRowCount(SQLITE_TABLE_STORIES);
        balsansar_number=masterDbAdapter.getRowCount(SQLITE_TABLE_BALSANSARS);
        essay_number=masterDbAdapter.getRowCount(SQLITE_TABLE_ESSAYS);
        gajal_number=masterDbAdapter.getRowCount(SQLITE_TABLE_GAJALS);
        other_number=masterDbAdapter.getRowCount(SQLITE_TABLE_OTHERS);

        masterDbAdapter.close();

      // masterDbAdapter.close();


        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }

       // new SyncAll().execute();



        return START_STICKY;
    }

    public void createNotification(String toshow,int id_number){

        //for notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Write Nepal")
                        .setContentText(toshow);


// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mBuilder.getNotification().flags|=Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(id_number, mBuilder.build());
        //NOTIFY_ME_ID+=1;


        Log.e("sasd","notification created");
    }

/*
    private class SyncAll extends AsyncTask<Void, Void, Void> {


        Connection conpoem = null, constory = null, conessay = null, conbalsansaar = null, congajal = null;

        //com.bct071.myapplication.MasterDbAdapter masterDbAdapter=new com.bct071.myapplication.MasterDbAdapter(getApplicationContext());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //masterDbAdapter.open();
            masterDbAdapter.open();

            conpoem = Jsoup.connect(mainurl + forpoem)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);

            constory = Jsoup.connect(mainurl + forstory)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            conbalsansaar = Jsoup.connect(mainurl + forbalsansar)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            conessay = Jsoup.connect(mainurl + foressay)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            congajal = Jsoup.connect(mainurl + forgajal)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);
            Log.i("TAG", "CONNECTED!!!");


        }

        @Override
        protected Void doInBackground(Void... params) {
            // arraylist = new ArrayList<HashMap<String, String>>();
            // doc = null;

            try {
                // Connect to the Website URL

                Document docpoem = conpoem.get();
                Document docstory = constory.get();
                Document docessay = conessay.get();
                Document docgajal = congajal.get();
                Document docbalsansar = conbalsansaar.get();

                //for poem
                Elements titlespoem = docpoem.select("div.span8 h2");
                Elements descrispoem = docpoem.select(".span8 p");
                Elements linkpoem = docpoem.select(".entry.archive-box h2>a");
                //for essay
                Elements titlesessay= docessay.select("div.span8 h2");
                Elements descrisessay = docessay.select(".span8 p");
                Elements linkessay = docessay.select(".entry.archive-box h2>a");
                //for gajal
                Elements titlesgajal = docgajal.select("div.span8 h2");
                Elements descrisgajal = docgajal.select(".span8 p");
                Elements linkgajal = docgajal.select(".entry.archive-box h2>a");
                //for bal sansarr
                Elements titlesbalsansar = docbalsansar.select("div.span8 h2");
                Elements descrisbalsansar = docbalsansar.select(".span8 p");
                Elements linkbalsansar = docbalsansar.select(".entry.archive-box h2>a");
                //for storys
                Elements titlesstory = docstory.select("div.span8 h2");
                Elements descrisstory = docstory.select(".span8 p");
                Elements linkstory = docstory.select(".entry.archive-box h2>a");


                fpoem_number = titlespoem.size();
                fstory_number=titlesstory.size();
                fgajal_number=titlesgajal.size();
                fessay_number=titlesessay.size();
                fbalsansar_number=titlesbalsansar.size();

                //Clean all data
                //dbHelper.deleteAllCountries();

                if (titlespoem != null && descrispoem!=null) {
                   masterDbAdapter.deleteAllData(SQLITE_TABLE_POEMS);
                    if(titlespoem.size()==descrispoem.size())
                    {
                        cpoem_number=titlespoem.size();
                    }
                    else{
                        cpoem_number=descrispoem.size();

                    }

                    for (int i = 0; i < cpoem_number; i++) {
                        Log.i("data got", trimme(descrispoem.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlespoem.get(i).text();
                        String b = trimme(descrispoem.get(i).text(), 100);
                        String c = linkpoem.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_POEMS, c, a, b);
                    }
                }



                if (titlesstory != null && descrisstory!=null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_STORIES);
                    if(titlesstory.size()==descrisstory.size())
                    {
                        cstory_number=titlesstory.size();
                    }
                    else{
                        cstory_number=descrisstory.size();

                    }
                    for (int i = 0; i < cstory_number; i++) {
                        Log.i("data got", trimme(descrisstory.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesstory.get(i).text();
                        String b = trimme(descrisstory.get(i).text(), 100);
                        String c = linkstory.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_STORIES, c, a, b);
                    }

                }
                if (titlesessay != null && descrisessay!=null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_ESSAYS);
                    if(titlesessay.size()==descrisessay.size())
                    {
                        cessay_number=titlesessay.size();
                    }
                    else{
                        cessay_number=descrisessay.size();

                    }
                    for (int i = 0; i < cessay_number; i++) {
                        Log.i("data got", trimme(descrisessay.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesessay.get(i).text();
                        String b = trimme(descrisessay.get(i).text(), 100);
                        String c = linkessay.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_ESSAYS, c, a, b);
                    }


                }if (titlesbalsansar != null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_BALSANSARS);
                    if(titlesbalsansar.size()==descrisbalsansar.size())
                    {
                        cbalsansar_number=titlesbalsansar.size();
                    }
                    else{
                        cbalsansar_number=descrisbalsansar.size();

                    }
                    for (int i = 0; i < cbalsansar_number; i++) {
                        Log.i("data got", trimme(descrisbalsansar.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesbalsansar.get(i).text();
                        String b = trimme(descrisbalsansar.get(i).text(), 100);
                        String c = linkbalsansar.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_BALSANSARS, c, a, b);
                    }

                }if (titlesgajal != null) {
                    masterDbAdapter.deleteAllData(SQLITE_TABLE_GAJALS);
                    if(titlesgajal.size()==descrisgajal.size())
                    {
                        cgajal_number=titlesgajal.size();
                    }
                    else{
                        cgajal_number=descrisgajal.size();

                    }
                    for (int i = 0; i < cgajal_number; i++) {
                        Log.i("data got", trimme(descrisgajal.get(i).text(), 10));
                        //this should add fetched data in database
                        String a = titlesgajal.get(i).text();
                        String b = trimme(descrisgajal.get(i).text(), 100);
                        String c = linkgajal.get(i).attr("href");

                        masterDbAdapter.insertData(SQLITE_TABLE_GAJALS, c, a, b);
                    }

                }


                Log.i("Tag", "Database is working");


            } catch (IOException e1) {
                e1.printStackTrace();
            }



            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            if (poem_number < cpoem_number) {
                createNotification("Check out some new poem",NID_POEM);
            } else {
                //createNotification("No new poem found",NID_POEM);
                Log.d("Notification","Data not updated");
            }
            if (story_number < cstory_number) {
                createNotification("New story has just been updated",NID_STORY);
            } else {
                //createNotification("No new story found",NID_STORY);
                Log.d("Notification","Data not updated");
            }
            if (essay_number < cessay_number) {
                createNotification("Some new essay has just been published",NID_ESSAY);
            } else {
                //createNotification("No new essay found",NID_ESSAY);
                Log.d("Notification","Data not updated");
            }
            if (balsansar_number < cbalsansar_number) {
                createNotification("BalSansar contents is updated",NID_BALSANSAR);
            } else {
                //createNotification("No new balsansar found",NID_BALSANSAR);
                Log.d("Notification","Data not updated");
            }
            if (gajal_number < cgajal_number) {
                createNotification("Check out recent uploaded gajals ",NID_GAJAL);
            } else {
                Log.d("Notification","Data not updated");
                //createNotification("No new gajal found",NID_GAJAL);
            }

            masterDbAdapter.close();


        }
    }

*/
    public static String trimme(String s,int n){
        if(s.length()<n){

            return s;
        }
        else{
            return(s.subSequence(0, n)+"...");
        }

    }


}
