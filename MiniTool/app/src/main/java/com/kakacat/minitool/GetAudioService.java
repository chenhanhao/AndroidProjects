package com.kakacat.minitool;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.kakacat.minitool.util.SystemUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetAudioService extends IntentService {

    public GetAudioService() {
        super("GetAudioService2");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getParcelableExtra("uri");
        Context context = getBaseContext();
        String[] projections = {MediaStore.Video.Media.DATA};  //  列名
        Cursor cursor = context.getContentResolver().query(uri,projections, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(0);
        cursor.close();
        SystemUtil.separateAudioFromVideo(filePath);
        SystemUtil.vibrate(context,200);
    }

}
