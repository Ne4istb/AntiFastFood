package com.hackaton.ne4istb.antifastfood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageThreadLoader {

    private final HashMap<String, SoftReference<Bitmap>> Cache = new HashMap<String, SoftReference<Bitmap>>();
    private final ArrayList<QueueItem> Queue = new ArrayList<QueueItem>();
    private final Handler handler = new Handler();    // Assumes that this is started from the main (UI) thread
    private String diskurl;
    private CompressFormat compressedImageFormat = CompressFormat.PNG;
    private int cachedImageQuality = 75;
    private Context context;
    private Thread thread;
    private QueueRunner runner = new QueueRunner();
    /**
     * Creates a new instance of the ImageThreadLoader
     */
    public ImageThreadLoader(Context context) {
        this.context = context;
        this.diskurl = context.getApplicationContext().getCacheDir() + "/upcache/imagecache";
        new File(diskurl).mkdirs();
        thread = new Thread(runner);
    }
    ;

    public static Bitmap readBitmapFromNetwork(URL url) {
        InputStream is = null;
        BufferedInputStream bis = null;
        Bitmap bmp = null;
        Log.e("*********", "--4");
        try {
            URLConnection conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bmp = BitmapFactory.decodeStream(bis);
        } catch (MalformedURLException e) {
            Log.e("*************", "Bad ad URL", e);
        } catch (IOException e) {
            Log.e("******************", "Could not get remote ad image", e);
        } finally {
            try {
                if (is != null)
                    is.close();
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                Log.w("*************", "Error closing stream.");
            }
        }
        Log.e("*********", "--5");
        return bmp;
    }

    private File getImageFile(String imageUrl) {
        String fileName = Integer.toHexString(imageUrl.hashCode()) + "."
                + compressedImageFormat.name();
        return new File(this.diskurl + "/" + fileName);
    }

    private String getFullPath(String imageUrl) {
        return this.diskurl + "/" + Integer.toHexString(imageUrl.hashCode()) + "."
                + compressedImageFormat.name();
    }

    public Bitmap loadImage(final String uri, final ImageLoadedListener listener) throws MalformedURLException {
        // If it's in the cache, just get it and quit it
        Log.e("*********", "--1");
        if (Cache.containsKey(uri)) {

            SoftReference<Bitmap> ref = Cache.get(uri);
            if (ref != null) {
                return ref.get();
            }
        }
        Log.e("*********", "--2");
        File imageFile = getImageFile(uri);
        if (imageFile.exists()) {

            String path = getFullPath(uri);
            Drawable draw = Drawable.createFromPath(path);
            Bitmap bit = ((BitmapDrawable) draw).getBitmap();
            return bit;
        }
        Log.e("*********", "--3");
        QueueItem item = new QueueItem();
        item.url = new URL(uri);
        item.listener = listener;
        Queue.add(item);


        // start the thread if needed
        if (thread.getState() == State.NEW) {
            thread.start();
        } else if (thread.getState() == State.TERMINATED) {
            thread = new Thread(runner);
            thread.start();
        }

        return null;
    }

    public interface ImageLoadedListener {
        public void imageLoaded(Bitmap imageBitmap);
    }

    private final class QueueItem {
        public URL url;
        public ImageLoadedListener listener;
    }

    private class QueueRunner implements Runnable {
        public void run() {
            synchronized (this) {
                while (Queue.size() > 0) {
                    final QueueItem item = Queue.remove(0);

                    // If in the cache, return that copy and be done
                    if (Cache.containsKey(item.url.toString()) && Cache.get(item.url.toString()) != null) {
                        // Use a handler to get back onto the UI thread for the update

                        handler.post(new Runnable() {
                            public void run() {
                                if (item.listener != null) {
                                    // NB: There's a potential race condition here where the cache item could get
                                    //     garbage collected between when we post the runnable and it's executed.
                                    //     Ideally we would re-run the network load or something.
                                    SoftReference<Bitmap> ref = Cache.get(item.url.toString());
                                    if (ref != null) {
                                        item.listener.imageLoaded(ref.get());
                                    }
                                }
                            }
                        });
                    } else {

                        final Bitmap bmp = readBitmapFromNetwork(item.url);

                        if (bmp != null) {
                            Cache.put(item.url.toString(), new SoftReference<Bitmap>(bmp));
                            try {
                                File imageFile = getImageFile(item.url.toString());
                                imageFile.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(imageFile);
                                bmp.compress(compressedImageFormat, cachedImageQuality, ostream);
                                ostream.close();
                            } catch (Throwable e) {

                            }

                            // Use a handler to get back onto the UI thread for the update
                            handler.post(new Runnable() {
                                public void run() {
                                    if (item.listener != null) {
                                        item.listener.imageLoaded(bmp);
                                    }
                                }
                            });

                        }
                    }

                }
            }
        }
    }
}