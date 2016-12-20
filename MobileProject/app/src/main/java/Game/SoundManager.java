package Game;

import android.content.Context;
import android.media.MediaPlayer;

import com.nonamestudio.mobileproject.R;

/**
 * Created by melvi_000 on 06/12/2016.
 */

public class SoundManager {

    public final MediaPlayer hitSound;
    public final MediaPlayer dodgeSound;
    public final MediaPlayer preparePunchSound;
    public final MediaPlayer punchSound;
    public final MediaPlayer bellSound;

    public SoundManager(Context context)
    {
        hitSound = MediaPlayer.create(context, R.raw.hit);
        dodgeSound = MediaPlayer.create(context, R.raw.dodge);
        preparePunchSound = MediaPlayer.create(context, R.raw.prepare_punch);
        punchSound = MediaPlayer.create(context, R.raw.punch);
        bellSound = MediaPlayer.create(context, R.raw.bell);

    }
}
