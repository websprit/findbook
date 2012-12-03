package org.lukang.demo.component;

import org.lukang.demo.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class MyDialog extends Dialog {  
    private Window window = null;  
  
    /*** 
     *  
     * @param context 
     * @param layoutResID 
     *            配置文件 
     * @param x 
     *            显示的x坐标 
     * @param y 
     *            显示的y坐标 
     * @param title 
     *            集合 
     */  
    public MyDialog(final Context context, int layoutResID, int x, int y,  
            final String[] title) {  
        super(context, R.style.Transparent);  
  
        window = this.getWindow();  
        window.requestFeature(Window.FEATURE_NO_TITLE);  
        setContentView(layoutResID);  
        int width = this.getWindow().getWindowManager().getDefaultDisplay()  
                .getWidth();  
        windowDeploy(width / 2, 300, x, y);  
        show();  
  
    }  
  
    /*** 
     * 设置窗口显示 
     *  
     * @param x 
     * @param y 
     * @param dialog_x 
     * @param dialog_y 
     */  
    public void windowDeploy(int dialog_width, int dialog_height, int dialog_x,  
            int dialog_y) {  
  
        window.setBackgroundDrawableResource(android.R.color.transparent); // 设置对话框背景为透明  
        WindowManager.LayoutParams wl = window.getAttributes();  
        wl.width = dialog_width;  
        wl.height = dialog_height;  
        // wl.alpha = 0.8f;  
        wl.gravity = Gravity.LEFT | Gravity.TOP; // 不设置的话默认是居中  
        wl.x = dialog_x - dialog_width / 2; // 要显示的位置x坐标  
        wl.y = dialog_y;  
        window.setAttributes(wl);  
        window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画  
        setCanceledOnTouchOutside(true);  
    }  
  
}  
