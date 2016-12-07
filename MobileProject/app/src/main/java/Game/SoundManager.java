package Game;

import android.content.Context;
import android.media.MediaPlayer;

import com.nonamestudio.mobileproject.R;

/**
 * Created by melvi_000 on 06/12/2016.
 */

public class SoundManager {

    public final MediaPlayer punchSound;
    public final MediaPlayer dodgeSound;
    public final MediaPlayer preparePunchSound;

    public SoundManager(Context context)
    {
        punchSound = MediaPlayer.create(context, R.raw.punch);
        dodgeSound = MediaPlayer.create(context, R.raw.dodge);
        preparePunchSound = MediaPlayer.create(context, R.raw.prepare_punch);

    }
}
