package com.tongji.lisa1225.calendartest.view;

import android.app.Application;
import com.bugtags.library.Bugtags;

public class BugTagApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //在这里初始化
        Bugtags.start("3e5eb38b63145d44d772e71380a58c57", this, Bugtags.BTGInvocationEventBubble);
    }
}
