package com.tongji.lisa1225.calendartest.wxapi;


public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI wxapi;

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent); setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);

        wxapi = WXAPIFactory.createWXAPI(this, "wx61684d30a8561d7c");
        wxapi.handleIntent(getIntent(), this);
    }
    public void onReq(BaseReq baseReq) {
    }

    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            // 正确返回
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    // ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX是微信分享，api自带
                    case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                        // 只是做了简单的finish操作
                        finish();
                        break;
                        default:
                            break;
                }
                break;
                default:
                    switch (baseResp.getType()) {
                        // 微信分享
                        case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                            Log.i("WXEntryActivity" , ">>>errCode = " + baseResp.errCode);
                            finish();
                            break;
                            default:
                                break;
                    } break;
        }
    }
}