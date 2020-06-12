package com.sunny.CustomWebView;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import java.util.Date;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.widget.FrameLayout;
import android.webkit.*;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.SimpleDateFormat;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.HVArrangement;
import com.google.appinventor.components.runtime.PermissionResultHandler;
import com.google.appinventor.components.annotations.androidmanifest.*;
import com.google.appinventor.components.annotations.UsesActivities;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import android.view.MotionEvent;
@DesignerComponent(version = 5, description ="An extended form of Web Viewer <br> Developed by Sunny Gupta", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "https://res.cloudinary.com/andromedaviewflyvipul/image/upload/c_scale,h_20,w_20/v1571472765/ktvu4bapylsvnykoyhdm.png",helpUrl="https://github.com/vknow360/CustomWebView")
@UsesActivities(activities = {@ActivityElement(intentFilters = {@IntentFilterElement(actionElements = {@ActionElement(name = "android.intent.action.VIEW")}, categoryElements = {@CategoryElement(name = "android.intent.category.DEFAULT"), @CategoryElement(name = "android.intent.category.BROWSABLE")}, dataElements = {@DataElement(scheme = "http"), @DataElement(scheme = "https")}), @IntentFilterElement(actionElements = {@ActionElement(name = "android.intent.action.VIEW")}, categoryElements = {@CategoryElement(name = "android.intent.category.DEFAULT"), @CategoryElement(name = "android.intent.category.BROWSABLE")}, dataElements = {@DataElement(scheme = "http"), @DataElement(scheme = "https"), @DataElement(mimeType = "text/html"), @DataElement(mimeType = "text/plain"), @DataElement(mimeType = "application/xhtml+xml")})},name="com.sunny.CustomWebView.WebActivity")})
@SimpleObject(external=true)
@UsesPermissions(permissionNames="android.permission.WRITE_EXTERNAL_STORAGE,android.permission.ACCESS_DOWNLOAD_MANAGER,android.permission.ACCESS_FINE_LOCATION,android.permission.RECORD_AUDIO, android.permission.MODIFY_AUDIO_SETTINGS, android.permission.CAMERA,android.permission.VIBRATE")
public final class CustomWebView extends AndroidNonvisibleComponent{
    public boolean NO_VIEW = true;
    public Activity activity;
    public WebView webView;
    public Context context;
    public boolean Js = true;
    public boolean AutoplayMedia = false;
    public boolean AutoLoadImages = true;
    public boolean BlockNetworkLoads = false;
    public boolean ZoomDisplay = true;
    public boolean SupportZoom = true;
    public boolean scrollbar = true;
    public int ZoomPercent = 100;
    public int FontSize = 16;
    public boolean UsesLocation = false;
    public boolean LongClickable =true;
    public boolean followLinks = true;
    public boolean prompt = true;
    public String UserAgent = "";
    public boolean DesktopMode = false;
    public boolean ignoreSslErrors = false;
    public boolean LoadLocalFiles = false;
    public boolean SupportMultipleWindows = true;
    WebViewInterface wvInterface;
    public String WebViewString ;
    public boolean UseWideViewPort = true;
    public boolean LoadWithOverviewMode = true;
    public JsPromptResult jsPromptResult ;
    private String MOBILE_USER_AGENT = "";
    private ValueCallback<Uri[]> mFilePathCallback;
    public Message dontSend;
    public Message reSend;
    public boolean hasLocationAccess = false;
    public boolean hasWriteAccess = false;
    public PermissionRequest permissionRequest;
    public PrintJob printJob;
	  public CookieManager cookieManager;
    public JsResult jsResult;
    public JsResult jsAlert;
    public HttpAuthHandler httpAuthHandler;
	  public static String url = "";
	  public boolean deepLinks = false;
	String jobName = "";

   public CustomWebView(ComponentContainer container) {
    	  super(container.$form());
    	  activity = container.$context();
    	  context = (Context) activity;
    	  webView = new WebView(context);
    	  wvInterface = new WebViewInterface();
		    cookieManager = CookieManager.getInstance();
		    hasWriteAccess = context.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Process.myPid(),Process.myUid()) == 0;
		    hasLocationAccess = context.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,Process.myPid(),Process.myUid()) == 0;
    	  webView.addJavascriptInterface(wvInterface, "AppInventor");
          webView.addJavascriptInterface(wvInterface, "Makeroid");
          webView.addJavascriptInterface(wvInterface, "Kodular");
          MOBILE_USER_AGENT = webView.getSettings().getUserAgentString();
          webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
          webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                OnDownloadNeeded(s,s2,s3,l);
            }
        });
        webView.setFindListener(new WebView.FindListener() {
            @Override
            public void onFindResultReceived(int i, int i1, boolean b) {
                FindResultReceived(i,i1,b);
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                String item = hitTestResult.getExtra();
                int type = hitTestResult.getType();
                if (item == null){
                    item = "";
                }
                String str = "";
                if (type == 8) {
                  Message message = new Handler().obtainMessage();
                  CustomWebView.this.webView.requestFocusNodeHref(message);
                  str = (String)message.getData().get("url");
                }
                if (str == null) {
                  str = "";
                }
                LongClicked(item,str,type);
                return true;
            }
        });
		webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                OnScrollChanged(i,i1,i2,i3,webView.canScrollHorizontally(-1),webView.canScrollHorizontally(1));
            }
        });
        webView.setWebViewClient(new WebClient());
        webView.setWebChromeClient(new ChromeClient());
        webView.getSettings().setJavaScriptEnabled(Js);
        webView.getSettings().setAllowFileAccess(LoadLocalFiles);
        webView.getSettings().setSupportZoom(SupportZoom);
        webView.getSettings().setDisplayZoomControls(ZoomDisplay);
        webView.setLongClickable(LongClickable);
        webView.getSettings().setBuiltInZoomControls(SupportZoom);
        webView.setInitialScale(ZoomPercent);
		cookieManager.setAcceptThirdPartyCookies(webView,true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(SupportZoom);
        webView.setVerticalScrollBarEnabled(scrollbar);
        webView.setHorizontalScrollBarEnabled(scrollbar);
        webView.getSettings().setDefaultFontSize(FontSize);
        webView.getSettings().setBlockNetworkImage(!AutoLoadImages);
        webView.getSettings().setLoadWithOverviewMode(LoadWithOverviewMode);
        webView.getSettings().setUseWideViewPort(UseWideViewPort);
        webView.getSettings().setLoadsImagesAutomatically(AutoLoadImages);
        webView.getSettings().setBlockNetworkLoads(BlockNetworkLoads);
        webView.getSettings().setAllowFileAccessFromFileURLs(LoadLocalFiles);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(LoadLocalFiles);
        webView.getSettings().setAllowContentAccess(LoadLocalFiles);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(AutoplayMedia);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(SupportMultipleWindows);
        webView.getSettings().setSupportMultipleWindows(SupportMultipleWindows);
        webView.getSettings().setTextZoom(ZoomPercent);
        webView.getSettings().setGeolocationEnabled(UsesLocation);
        UserAgent = MOBILE_USER_AGENT;
        webView.getSettings().setUserAgentString(UserAgent);
  }

  @DesignerProperty(editorType = "component:com.google.appinventor.components.runtime.VerticalArrangement")
  @SimpleProperty(userVisible = false)
  public void WebviewVArrangement(HVArrangement container){
        if(NO_VIEW){
		View v = container.getView();
		FrameLayout frameLayout = (FrameLayout) v;
        frameLayout.addView(webView,new FrameLayout.LayoutParams(-1,-1));
        NO_VIEW = false;
      }
  }
  @DesignerProperty(editorType = "component:com.google.appinventor.components.runtime.HorizontalArrangement")
  @SimpleProperty(userVisible = false)
  public void WebviewHArrangement(HVArrangement container){
        if(NO_VIEW){
        View v = container.getView();
		FrameLayout frameLayout = (FrameLayout) v;
        frameLayout.addView(webView,new FrameLayout.LayoutParams(-1,-1));
        NO_VIEW = false;
      }
	}
  @SimpleFunction(description="Arrangement to create webview")
  public void CreateWebView(AndroidViewComponent container){
		if(NO_VIEW){
        View v = container.getView();
        FrameLayout frameLayout = (FrameLayout) v;
        frameLayout.addView(webView,new FrameLayout.LayoutParams(-1,-1));
        NO_VIEW = false;
      }
  }
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description="Set webview string")
  public void WebViewString(String newString) {
    wvInterface.setWebViewStringFromBlocks(newString);
  }
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets the visibility of webview")
  public boolean Visible(){
        return webView.getVisibility() == View.VISIBLE;
    }
	@SimpleProperty(description="Returns the visibility of webview")
  public void Visible(boolean visibility){
        webView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description="Get webview string")
  public String WebViewString() {
    return wvInterface.webViewString;
  }

  @SimpleProperty(description = "Get webview user agent",category = PropertyCategory.BEHAVIOR)
  public String UserAgent() {
    return UserAgent;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty(description="Sets the WebView's user-agent string. If the string is null or empty, the system default value will be used. ")
  public void UserAgent(String userAgent) {
	  if(!userAgent.isEmpty()){
	  UserAgent = userAgent;
	  }else{
		  UserAgent = MOBILE_USER_AGENT;
	  }
	webView.getSettings().setUserAgentString(UserAgent);
	Reload();
  }

  @SimpleProperty(
      description = "URL of the page currently viewed",
      category = PropertyCategory.BEHAVIOR)
  public String CurrentUrl() {
    return (webView.getUrl() == null) ? "" : webView.getUrl();
  }
  @SimpleProperty(
      description = "Title of the page currently viewed",
      category = PropertyCategory.BEHAVIOR)
  public String CurrentPageTitle() {
    return (webView.getTitle() == null) ? "" : webView.getTitle();
  }
  @SimpleProperty(
      description = "Determines whether to follow links when they are tapped in the WebViewer.  " +
          "If you follow links, you can use GoBack and GoForward to navigate the browser history. ",
      category = PropertyCategory.BEHAVIOR)
  public boolean FollowLinks() {
    return followLinks;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(description="Sets whether to enable deep links or not")
  public void DeepLinks(boolean follow) {
    deepLinks = follow;
  }
  @SimpleProperty(description="Returns whether deep links are enabled or not")
  public boolean DeepLinks() {
	return deepLinks;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether to follow links or not")
  public void FollowLinks(boolean follow) {
    followLinks = follow;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(description="Sets whether the WebView requires a user gesture to play media")
  public void AutoplayMedia(boolean follow) {
    AutoplayMedia = follow;
    webView.getSettings().setMediaPlaybackRequiresUserGesture(AutoplayMedia);
  }
  @SimpleProperty(description="Returns whether the WebView requires a user gesture to play media")
  public boolean AutoplayMedia() {
    return AutoplayMedia;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether the WebView should support zooming using its on-screen zoom controls and gestures")
  public void ZoomEnabled(boolean follow) {
    SupportZoom = follow;
    webView.getSettings().setBuiltInZoomControls(SupportZoom);
    webView.getSettings().setSupportZoom(SupportZoom);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether the WebView should load image resources")
  public void AutoLoadImages(boolean follow) {
    AutoLoadImages = follow;
    webView.getSettings().setBlockNetworkImage(!AutoLoadImages);
    webView.getSettings().setLoadsImagesAutomatically(AutoLoadImages);
  }
  @SimpleProperty(description="Returnss whether the WebView should load image resources")
  public boolean AutoLoadImages() {
    return AutoLoadImages;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether the WebView should display on-screen zoom controls")
  public void DisplayZoom(boolean follow) {
    ZoomDisplay = follow;
    webView.getSettings().setDisplayZoomControls(ZoomDisplay);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,
      defaultValue = "100")
  @SimpleProperty(description="Sets the zoom of the page in percent. The default is 100")
  public void ZoomPercent(int follow) {
    ZoomPercent = follow;
    webView.getSettings().setTextZoom(ZoomPercent);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,
      defaultValue = "16")
  @SimpleProperty(description="Sets the default font size of text. The default is 16.")
  public void FontSize(int follow) {
    FontSize = follow;
    webView.getSettings().setDefaultFontSize(FontSize);
  }
  @SimpleProperty(description="Returns the font size of text")
  public int FontSize() {
    return FontSize;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(description="Sets whether to load content in desktop mode")
  public void DesktopMode(boolean mode) {
    DesktopMode = mode;
    if(mode){
        UserAgent = UserAgent.replace("Android","diordnA").replace("Mobile","eliboM");
    }else{
        UserAgent = UserAgent.replace("diordnA","Android").replace("eliboM","Mobile");
    }
    webView.getSettings().setUserAgentString(UserAgent);
	Reload();
  }
  @SimpleProperty(description="Returns whether to load content in desktop mode")
  public boolean DesktopMode() {
    return DesktopMode;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether to enable text selection and context menu")
  public void LongClickable(boolean follow) {
    LongClickable = follow;
    webView.setLongClickable(LongClickable);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(description="Sets whether webview can access local files.Use this to enable file uploading and loading files using HTML")
  public void FileAccess(boolean follow) {

        if (!hasWriteAccess){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
            form.askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionResultHandler() {
                        @Override
                        public void HandlePermissionResponse(String permission, boolean granted) {
                            hasWriteAccess = granted;
                        }
                    });
                }
            });
		}
		LoadLocalFiles = follow;
            if (hasWriteAccess){
                webView.getSettings().setAllowFileAccess(LoadLocalFiles);
                webView.getSettings().setAllowFileAccessFromFileURLs(LoadLocalFiles);
                webView.getSettings().setAllowUniversalAccessFromFileURLs(LoadLocalFiles);
                webView.getSettings().setAllowContentAccess(LoadLocalFiles);
            }else{
                webView.getSettings().setAllowFileAccess(false);
                webView.getSettings().setAllowFileAccessFromFileURLs(false);
                webView.getSettings().setAllowUniversalAccessFromFileURLs(false);
                webView.getSettings().setAllowContentAccess(false);
            }
  }
  @SimpleProperty(description="Returns whether webview can access local files")
  public boolean FileAccess() {
    return LoadLocalFiles;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether the WebView supports multiple windows")
  public void SupportMultipleWindows(boolean follow) {
    SupportMultipleWindows = follow;
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(SupportMultipleWindows);
    webView.getSettings().setSupportMultipleWindows(SupportMultipleWindows);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(description="Sets whether the WebView should not load resources from the network.Use this to save data.")
  public void BlockNetworkLoads(boolean follow) {
    BlockNetworkLoads = follow;
    webView.getSettings().setBlockNetworkLoads(BlockNetworkLoads);
  }
  @SimpleProperty(description="Returns whether the WebView should not load resources from the network")
  public boolean BlockNetworkLoads() {
    return BlockNetworkLoads;
  }
  @SimpleProperty(description="Returns whether the WebView supports multiple windows")
  public boolean SupportMultipleWindows() {
    return SupportMultipleWindows;
  }
  @SimpleProperty(description="Gets wether text selection and context menu are enabled")
  public boolean LongClickable() {
    return LongClickable;
  }
  @SimpleProperty(
      description = "Returns whether webview ignores SSL errors",
      category = PropertyCategory.BEHAVIOR)
  public boolean IgnoreSslErrors() {
    return ignoreSslErrors;
  }

  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(description="Determine whether or not to ignore SSL errors. Set to true to ignore " +
  "errors. Use this to accept self signed certificates from websites")
  public void IgnoreSslErrors(boolean ignoreSslErrors) {
    ignoreSslErrors = ignoreSslErrors;
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether the WebView loads pages in overview mode, that is, zooms out the content to fit on screen by width. This setting is taken into account when the content width is greater than the width of the WebView control.")
  public void LoadWithOverviewMode(boolean ignoreSslErrors) {
    LoadWithOverviewMode = ignoreSslErrors;
    webView.getSettings().setLoadWithOverviewMode(LoadWithOverviewMode);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(description="Sets whether the WebView should enable support for the 'viewport' HTML meta tag or should use a wide viewport.")
  public void UseWideViewPort(boolean ignoreSslErrors) {
    UseWideViewPort = ignoreSslErrors;
    webView.getSettings().setUseWideViewPort(UseWideViewPort);
  }
  @SimpleProperty(description="Returns whether the WebView loads pages in overview mode")
  public boolean LoadWithOverviewMode() {
    return LoadWithOverviewMode;
  }
  @SimpleProperty(description="Returns whether the WebView should enable support for the 'viewport' HTML meta tag or should use a wide viewport.")
  public boolean UseWideViewPort() {
    return UseWideViewPort;
  }
  @DesignerProperty(editorType= PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,defaultValue="True")
  @SimpleProperty(description="Tells the WebView to enable JavaScript execution.")
  public void EnableJS(boolean js) {
    Js = js;
    webView.getSettings().setJavaScriptEnabled(Js);
  }
  @SimpleProperty(description="Returns whether webview supports JavaScript execution")
  public boolean EnableJS() {
    return Js;
  }
   @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty(userVisible = false,
      description = "Whether or not to give the application permission to use the Javascript geolocation API")
  public void UsesLocation(boolean uses) {
    UsesLocation = uses;
    if (!hasLocationAccess){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
        form.askPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionResultHandler() {
                    @Override
                    public void HandlePermissionResponse(String permission, boolean granted) {
                        hasLocationAccess = granted;
                    }
                });
            }
        });
        if (hasLocationAccess){
            webView.getSettings().setGeolocationDatabasePath(activity.getFilesDir().getAbsolutePath());
            webView.getSettings().setDatabaseEnabled(true);
        }else{
            webView.getSettings().setDatabaseEnabled(false);
        }
        webView.getSettings().setGeolocationEnabled(UsesLocation);
    }
  }
  @SimpleProperty(description = "Returns whether webview will prompt for permission and raise 'OnPermissionRequest' event or not")
  public boolean PromptForPermission() {
    return prompt;
  }

  @DesignerProperty(defaultValue = "True", editorType = "boolean")
  @SimpleProperty(description = "Whether to display horizonatal and vertical scrollbars or not")
  public void Scrollbar(boolean paramBoolean) {
    scrollbar = paramBoolean;
    webView.setVerticalScrollBarEnabled(scrollbar);
    webView.setHorizontalScrollBarEnabled(scrollbar);
  }
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "True")
  @SimpleProperty(userVisible = true,description="Sets whether webview will prompt for permission and raise 'OnPermissionRequest' event or not else assume permission is granted.")
  public void PromptForPermission(boolean prompt) {
    prompt = prompt;
  }
  @SimpleProperty(userVisible = true,description="Sets background color of webview")
  public void BackgroundColor(int bgColor) {
    webView.setBackgroundColor(bgColor);
  }
@SimpleEvent(description = "When the JavaScript calls AppInventor.setWebViewString this event is run.")
  public void WebViewStringChange(String value) {
    EventDispatcher.dispatchEvent(this, "WebViewStringChange",value);
  }
  @SimpleFunction(description="Stops the current load.")
  public void StopLoading(){
        webView.stopLoading();
    }
  @SimpleFunction(description="Reloads the current URL.")
   public void Reload(){
        CancelJsRequests();
        webView.reload();
    }
    @SimpleFunction(description="Loads the given data into this WebView using a 'data' scheme URL.")
    public void LoadHtml(String html){
        CancelJsRequests();
        webView.loadData(html,"text/html", "UTF-8");
    }
  @SimpleFunction(description="Gets whether this WebView has a back history item.")
  public boolean CanGoBack(){
        return webView.canGoBack();
    }
  @SimpleFunction(description="Gets whether this WebView has a forward history item.")
  public boolean CanGoForward(){
        return webView.canGoForward();
    }
  @SimpleFunction(description="Removes all cookies and raises 'CookiesRemoved' event")
  public void ClearCookies(){
    if(Build.VERSION.SDK_INT >= 21){
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                CookiesRemoved(aBoolean);
            }
        });
        cookieManager.flush();
        return;
    }
}

   @SimpleEvent(description="Event raised after 'ClearCokies' method with result")
   public void CookiesRemoved(boolean successful){
    EventDispatcher.dispatchEvent(this,"CookiesRemoved",successful);
    }
  @SimpleFunction(description="Clears the resource cache.")
  public void ClearCache(){
        webView.clearCache(true);
    }
  @SimpleFunction(description="Tells this WebView to clear its internal back/forward list.")
  public void ClearInternalHistory(){
        webView.clearHistory();
    }
  @SimpleFunction(description="Goes back in the history of this WebView.")
  public void GoBack(){
	  if(CanGoBack()){
        webView.goBack();
	  }
    }
  @SimpleFunction(description="Goes forward in the history of this WebView.")
  public void GoForward(){
	  if(CanGoForward()){
        webView.goForward();
	  }
    }
	@SimpleFunction(description="Returns the url which launched this activity")
  public static String GetUrlToOpen() {
    return url;
  }
	@SimpleFunction(description="Destroys the webview and removes it completely from view system")
	public void DestroyWebView(){
        webView.destroy();
    }
  @SimpleFunction(description="Gets whether the page can go back or forward the given number of steps.")
  public boolean CanGoBackOrForward(int steps){
        return webView.canGoBackOrForward(steps);
    }
  @SimpleFunction(description="Goes to the history item that is the number of steps away from the current item. Steps is negative if backward and positive if forward.")
  public void GoBackOrForward(int steps){
	  if(CanGoBackOrForward(steps)){
        webView.goBackOrForward(steps);
	  }
    }

	@SimpleFunction(description="Loads the given URL.")
    public void GoToUrl(String url){
            CancelJsRequests();
            webView.loadUrl(url);
    }
	@SimpleEvent(description="Event raised when page loading has finished.")
    public void PageLoaded(){
	EventDispatcher.dispatchEvent(this, "PageLoaded");
    }
	@SimpleEvent(description="Event raised when downloading is needed.")
    public void OnDownloadNeeded(String url,String contentDisposition,String mimeType,long size){
	EventDispatcher.dispatchEvent(this, "OnDownloadNeeded",url,contentDisposition,mimeType,size);
    }
	@SimpleEvent(description="Event raised when page loading progress has changed.")
    public void OnProgressChanged(int progress){
EventDispatcher.dispatchEvent(this, "OnProgressChanged",progress);
    }
	@SimpleEvent(description="Event raised after getting console message.")
    public void OnConsoleMessage(String message, int lineNumber, int sourceID, String level){
EventDispatcher.dispatchEvent(this, "OnConsoleMessage",message,lineNumber,sourceID,level);
    }

	@SimpleFunction(description="Asynchronously evaluates JavaScript in the context of the currently displayed page.")
    public void EvaluateJavaScript(String script){
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    AfterJavaScriptEvaluated(s);
                }
            });
    }
	@SimpleEvent(description="Event raised after evaluating Js with result.")
    public void AfterJavaScriptEvaluated(String result){
      EventDispatcher.dispatchEvent(this, "AfterJavaScriptEvaluated",result);
    }
	@SimpleEvent(description="Event raised when webview gets scrolled")
	public void OnScrollChanged(int scrollX,int scrollY,int oldScrollX,int oldScrollY,boolean canGoLeft,boolean canGoRight){
		EventDispatcher.dispatchEvent(this,"OnScrollChanged",scrollX,scrollY,oldScrollX,oldScrollY,canGoLeft,canGoRight);
    }

	@SimpleFunction(description="Clears the highlighting surrounding text matches.")
	public void ClearMatches(){
        webView.clearMatches();
    }
	@SimpleEvent(description="Event raised when something is long clicked in webview with item(image,string,empty,etc) and type(item type like 0,1,8,etc)")
	public void LongClicked(String item,String secondaryUrl,int type){
    EventDispatcher.dispatchEvent(this, "LongClicked",item,secondaryUrl,type);
  }
    @SimpleFunction(description="Scrolls the webview to given position")
    public void ScrollTo(final int x,final int y){
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.scrollTo(x,y);
            }
        },300);
    }
    @SimpleFunction(description="Return the scrolled left position of the webview")
    public int GetScrollX(){
        return webView.getScrollX();
    }
    @SimpleFunction(description="Return the scrolled top position of the webview")
    public int GetScrollY(){
        return webView.getScrollY();
    }
	@SimpleEvent(description="Event raised when any error is received during loading url and returns message,error code and failing url")
	public void OnErrorReceived(String message,int errorCode,String url){
EventDispatcher.dispatchEvent(this, "OnErrorReceived",message,errorCode,url);
    }


    public class WebClient extends WebViewClient{
       @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url1) {
			boolean result = false;
            if (url1.startsWith("http")){
                result = !followLinks;
            }else if (deepLinks){
                DeepLinkParser(url1);
				result = true;
            }
			return result;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			boolean result = false;
            String url1 = request.getUrl().toString();
            if (url1.startsWith("http")){
                result = !followLinks;
            }else if (deepLinks){
                DeepLinkParser(url1);
			   result = true;
            }
			return result;
        }

       @Override
       public void onPageFinished(WebView view, String url) {
           PageLoaded();
           if(DesktopMode){
             webView.zoomOut();
           }
       }

       @Override
       public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
           if (ignoreSslErrors){
               handler.proceed();
           }else {
               handler.cancel();
           }
       }

       @Override
       public void onFormResubmission(WebView view, Message dontResend, Message resend) {
           dontSend = dontResend;
           reSend = resend;
           OnFormResubmission();
       }

       @Override
       public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
           return null;
       }

       @Override
       public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
           OnErrorReceived(errorResponse.getReasonPhrase(),errorResponse.getStatusCode(),request.getUrl().toString());
       }

       @Override
       public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
           OnErrorReceived(error.getDescription().toString(),error.getErrorCode(),request.getUrl().toString());
       }

       @Override
       public void onPageStarted(WebView view, String url, Bitmap favicon) {
           PageStarted(url);
       }

       @Override
       public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
           httpAuthHandler = handler;
           OnReceivedHttpAuthRequest(host,realm);
       }
   }
   @SimpleEvent(description="Event raised when file uploading is needed")
  public void FileUploadNeeded(){
	  EventDispatcher.dispatchEvent(this,"FileUploadNeeded");
  }
  @SimpleFunction(description="Uploads the given file from content uri.Use empty string to cancel the upload request.")
  public void UploadFile(String contentUri){
	  if(mFilePathCallback != null){
		if(contentUri.isEmpty()){
			mFilePathCallback.onReceiveValue(null);
			mFilePathCallback = null;
		}else{
			mFilePathCallback.onReceiveValue(new Uri[]{Uri.parse(contentUri)});
			mFilePathCallback = null;
		}
	  }
  }
   public class ChromeClient extends WebChromeClient {
       @Override
       public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
           final GeolocationPermissions.Callback theCallback = callback;
           final String theOrigin = origin;
           if (prompt) {
               AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
               alertDialog.setTitle("Permission Request");
               if (origin.equals("file://"))
                   origin = "This Application";
               alertDialog.setMessage(origin + " would like to access your location.");
               alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Allow",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               theCallback.invoke(theOrigin, true, true);
                           }
                       });
               alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Refuse",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               theCallback.invoke(theOrigin, false, true);
                           }
                       });
               alertDialog.show();
               return;
           }
           callback.invoke(origin, true, true);
       }

       @Override
       public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
				mFilePathCallback = null;
            }
			mFilePathCallback = filePathCallback;
			FileUploadNeeded();
           return LoadLocalFiles;
       }

       @Override
       public boolean onCreateWindow(WebView view,final boolean isDialog,final boolean isUserGesture, Message resultMsg) {
           if (SupportMultipleWindows){
              final WebView mWebView = new WebView(context);
               mWebView.setWebViewClient(new WebViewClient(){
                   @Override
                   public void onPageStarted(WebView view, String url, Bitmap favicon) {
                       OnNewWindowRequest(url,isDialog,!isUserGesture);
                       mWebView.stopLoading();
                       mWebView.destroy();
                   }
               });
               WebView.WebViewTransport transport = (WebView.WebViewTransport)resultMsg.obj;
               transport.setWebView(mWebView);
               resultMsg.sendToTarget();
           }
           return SupportMultipleWindows;
       }


       @Override
       public void onProgressChanged(WebView view, int newProgress) {
           OnProgressChanged(newProgress);
       }

       @Override
       public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
           OnConsoleMessage(consoleMessage.message(),consoleMessage.lineNumber(),consoleMessage.lineNumber(),consoleMessage.messageLevel().toString());
           return true;
       }

       @Override
       public void onPermissionRequest(PermissionRequest request) {
           if (!prompt){
               request.grant(request.getResources());
           }else{
               permissionRequest = request;
               String[] strings = request.getResources();
               List<String> permissions = Arrays.asList(strings);
               OnPermissionRequest(permissions);
           }
       }

       @Override
       public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
           jsPromptResult = result;
           OnJsPrompt(url,message,defaultValue);
           return Js;
       }

       @Override
       public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
           OnJsAlert(url,message);
           jsAlert = result;
           return Js;
       }

       @Override
       public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
           jsResult = result;
           OnJsConfirm(url,message);
           return Js;
       }
   }
    @SimpleFunction(description="Grants permissions to webview.It accepts a list of permissions.")
    public void GrantPermission(final List<String> permissions){
      if (permissionRequest != null){
          activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  if(permissions.isEmpty()){
                      permissionRequest.deny();
                  }else {
                      permissionRequest.grant(permissionRequest.getResources());
                  }
                  permissionRequest = null;
              }
          });
      }
    }
    @SimpleEvent(description="Event raised when resubmission of form is needed")
    public void OnFormResubmission(){
        EventDispatcher.dispatchEvent(this,"OnFormResubmission");
    }
    @SimpleFunction(description="Whether to resubmit form or not.")
    public void ResubmitForm(boolean reSubmit){
      if (reSend != null && dontSend != null) {
        if (reSubmit){
            reSend.sendToTarget();
        }else {
            dontSend.sendToTarget();
        }
        reSend = null;
        dontSend = null;
      }
    }
	@SimpleEvent(description="Event raised when new window is requested by webview with target url ,boolean 'isDialog' and 'isPopup'")
	public void OnNewWindowRequest(String url,boolean isDialog,boolean isPopup){
		EventDispatcher.dispatchEvent(this, "OnNewWindowRequest",url,isDialog,isPopup);
    }

	@SimpleFunction(description="Gets height of HTML content")
	public int ContentHeight(){
        return webView.getContentHeight();
    }
	@SimpleEvent(description="Event raised after getting SSL certificate of current displayed url/website with boolean 'isSecure' and Strings 'issuedBy','issuedTo' and 'validTill'.If 'isSecure' is false and other values are empty then assume that website is not secure")
	 public void GotCertificate(boolean isSecure,String issuedBy,String issuedTo,String validTill){
        EventDispatcher.dispatchEvent(this, "GotCertificate",isSecure,issuedBy,issuedTo,validTill);
    }
	@SimpleFunction(description="Gets the SSL certificate for the main top-level page and raises 'GotCertificate' event")
	public void GetSslCertificate(){
        SslCertificate certificate = webView.getCertificate();
        if (certificate != null) {
            GotCertificate(true,certificate.getIssuedBy().getDName(),certificate.getIssuedTo().getDName(),certificate.getValidNotAfterDate().toString());
        }else{
            GotCertificate(false,"","","");
	      }
    }
  @SimpleEvent(description="Event raised when Js have to show an alert to user")
  public void OnJsAlert(String url,String message){
      EventDispatcher.dispatchEvent(this,"OnJsAlert",url,message);
    }
  @SimpleEvent(description="Tells to display a confirm dialog to the user.")
  public void OnJsConfirm(String url,String message){
      EventDispatcher.dispatchEvent(this,"OnJsConfirm",url,message);
    }
  @SimpleEvent(description="Event raised when JavaScript needs input from user")
  public void OnJsPrompt(String url, String message, String defaultValue){
      EventDispatcher.dispatchEvent(this,"OnJsPrompt",url,message,defaultValue);
    }
  @SimpleFunction(description="Dismiss previously requested Js alert")
  public void DismissJsAlert(){
    if (jsAlert != null) {
      jsAlert.cancel();
      jsAlert = null;
    }
    }
  @SimpleFunction(description="Inputs a confirmation response to Js")
  public void ContinueJs(String input){
    if (jsPromptResult != null) {
      jsPromptResult.confirm(input);
      jsPromptResult = null;
    }
    }
  @SimpleFunction(description="Whether to proceed JavaScript originated request")
  public void ConfirmJs(boolean confirm){
    if (jsResult != null) {
      if (confirm){
          jsResult.confirm();
      }else{
          jsResult.cancel();
      }
      jsResult = null;
    }
    }
  @SimpleEvent(description="Notifies that the WebView received an HTTP authentication request.")
  public void OnReceivedHttpAuthRequest(String host,String realm){
      EventDispatcher.dispatchEvent(this,"OnReceivedHttpAuthRequest",host,realm);
    }
  @SimpleEvent(description="Event indicating that page loading has started in web view.")
  public void PageStarted(String url){
      EventDispatcher.dispatchEvent(this,"PageStarted",url);
    }
  @SimpleFunction(description="Instructs the WebView to proceed with the authentication with the given credentials.If both parameters are empty then it will cancel the request.")
  public void ProceedHttpAuthRequest(String username,String password){
    if(httpAuthHandler != null){
        if (username.isEmpty() && password.isEmpty()){
            httpAuthHandler.cancel();
        }else {
            httpAuthHandler.proceed(username,password);
        }
        httpAuthHandler = null;
      }
    }
	@SimpleEvent(description="Event raised after 'Find' method with int 'activeMatchOrdinal','numberOfMatches' and 'isDoneCounting'")
	public void FindResultReceived(int activeMatchOrdinal,int numberOfMatches,boolean isDoneCounting){
        EventDispatcher.dispatchEvent(this, "FindResultReceived",activeMatchOrdinal,numberOfMatches,isDoneCounting);
    }
    @SimpleFunction(description="Clear all location preferences.")
    public void ClearLocation(){
        GeolocationPermissions.getInstance().clearAll();
    }
	@SimpleFunction(description="Finds all instances of find on the page and highlights them, asynchronously. Successive calls to this will cancel any pending searches.")
	public void Find(String string){
        webView.findAllAsync(string);
    }
	@SimpleFunction(description="Get cookies for specific url")
	public String GetCookies(String url){
        return CookieManager.getInstance().getCookie(url);
    }
	@SimpleFunction(description="Highlights and scrolls to the next match if 'forward' is true else scrolls to previous match.")
	public void FindNext(boolean forward){
        webView.findNext(forward);
    }
	public class WebViewInterface {
        String webViewString;
        WebViewInterface() {
            webViewString = "";
        }
        @JavascriptInterface
        public String getWebViewString() {
            return webViewString;
        }
        @JavascriptInterface
        public void setWebViewString(final String newString) {
            webViewString = newString;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                  WebViewStringChange(newString);
                }
              });
        }
        public void setWebViewStringFromBlocks(final String newString) {
            webViewString = newString;
        }
    }
    @SimpleEvent(description="Event raised when a website asks for specific permission(s) in list format.")
    public void OnPermissionRequest(List<String> permissionsList){
        EventDispatcher.dispatchEvent(this,"OnPermissionRequest",permissionsList);
    }
    @SimpleEvent(description="Event raised after getting previus print's result.")
    public void GotPrintResult(String id,boolean isCompleted,boolean isFailed,boolean isBlocked){
        EventDispatcher.dispatchEvent(this,"GotPrintResult",id,isCompleted,isFailed,isBlocked);
    }
    @SimpleFunction(description="Prints the content of webview with color mode(Use 2 for color scheme , 1 for monochrome scheme and 0 for default scheme. )")
   public void PrintWebContent(){
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        jobName = context.getApplicationInfo().name + " Document";
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);
        if(printManager != null){
            printJob = printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());
        }
        GotPrintResult(jobName,printJob.isCompleted(),printJob.isFailed(),printJob.isBlocked());
    }
    @SimpleFunction(description="Restarts current/previous print job. You can request restart of a failed print job.")
    public void RestartPrinting(){
       printJob.restart();
        GotPrintResult(jobName,printJob.isCompleted(),printJob.isFailed(),printJob.isBlocked());
    }
    @SimpleFunction(description="Cancels current print job. You can request cancellation of a queued, started, blocked, or failed print job.")
    public void CancelPrinting(){
        printJob.cancel();
        GotPrintResult(jobName,printJob.isCompleted(),printJob.isFailed(),printJob.isBlocked());
    }
	public void CancelJsRequests(){
        if(jsAlert != null){
            jsAlert.cancel();
			jsAlert = null;
        }else if (jsResult != null){
            jsResult.cancel();
			jsResult = null;
        }else if (jsPromptResult != null){
            jsPromptResult.cancel();
			jsPromptResult = null;
        }else if(mFilePathCallback != null){
			mFilePathCallback.onReceiveValue(null);
			mFilePathCallback = null;
		}
    }
	@SimpleFunction(description= "Downloads the file from url")
	public void Download(String url,String mimeType,String contentDisposition,String fileName,String downloadDir){
		if (!hasWriteAccess){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
            form.askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionResultHandler() {
                        @Override
                        public void HandlePermissionResponse(String permission, boolean granted) {
                            hasWriteAccess = granted;
                        }
                    });
                }
            });
		}
		if(hasWriteAccess){
        String name = fileName;
        String dir = downloadDir;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType(mimeType);
        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", UserAgent);
        request.setDescription("Downloading file...");
        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (downloadDir.isEmpty()){
            dir = Environment.DIRECTORY_DOWNLOADS;
        }else if (fileName.isEmpty()){
            name = URLUtil.guessFileName(url, contentDisposition, mimeType);
        }
        request.setDestinationInExternalPublicDir(dir, name);
       DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);
		}
    }
	public void DeepLinkParser(String url){
        PackageManager packageManager = context.getPackageManager();
        Intent intent ;
        if (url.startsWith("tel:")){
            intent = new Intent(Intent.ACTION_DIAL,Uri.parse(url));
            activity.startActivity(intent);
        }else if (url.startsWith("whatsapp:")){
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,Uri.parse(url));
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            activity.startActivity(intent);
        }else if (url.startsWith("geo:")){
            intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(packageManager) != null){
                activity.startActivity(intent);
            }else{
                Toast.makeText(context,"No corresponding activity or app found",Toast.LENGTH_SHORT);
            }
        }else if (url.startsWith("intent:")){
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent.resolveActivity(packageManager) != null){
                    activity.startActivity(intent);
                }
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                if (fallbackUrl != null) {
                    webView.loadUrl(fallbackUrl);
                }
                intent = new Intent(Intent.ACTION_VIEW).setData(
                        Uri.parse("market://details?id=" + intent.getPackage()));
                if (intent.resolveActivity(packageManager) != null) {
                    activity.startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
				Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT);
            }
        }
    }
}
