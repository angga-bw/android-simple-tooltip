/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 Douglas Nassif Roma Junior
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.douglasjunior.androidSimpleTooltip;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.util.Log;

/**
 * ArrowDrawable
 * Created by douglas on 09/05/16.
 */
public class ArrowDrawable extends ColorDrawable {

    public static final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3, AUTO = 4;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int mBackgroundColor;
    private final int mBorderColor;
    private Path mPath;
    private Path mPathBorder;
    private final int mDirection;

    private int borderSize = 4;

    ArrowDrawable(@ColorInt int foregroundColor, int direction) {
        this.mBackgroundColor = Color.TRANSPARENT;
        this.mBorderColor = Color.TRANSPARENT;
        this.mPaint.setColor(foregroundColor);
        this.mPaintBorder.setColor(mBorderColor);
        this.mDirection = direction;
        this.borderSize = 4;
    }

    ArrowDrawable(@ColorInt int foregroundColor, @ColorInt int borderColor, int direction) {
        this.mBackgroundColor = Color.TRANSPARENT;
        this.mBorderColor = borderColor;
        this.mPaint.setColor(foregroundColor);
        this.mPaintBorder.setColor(borderColor);
        this.mDirection = direction;
        this.borderSize = 4;
    }

    ArrowDrawable(@ColorInt int foregroundColor, @ColorInt int borderColor, int borderSize, int direction) {
        this.mBackgroundColor = Color.TRANSPARENT;
        this.mBorderColor = borderColor;
        this.mPaint.setColor(foregroundColor);
        this.mPaintBorder.setColor(borderColor);
        this.mDirection = direction;
        this.borderSize = borderSize;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updatePath(bounds);
    }

    private synchronized void updatePath(Rect bounds) {
        mPath = new Path();
        mPathBorder = new Path();

        switch (mDirection) {
            case LEFT:
                mPath.moveTo(bounds.width()+borderSize, bounds.height());
                mPath.lineTo(borderSize, ((bounds.height()) / 2));
                mPath.lineTo(bounds.width(), borderSize);
                mPath.lineTo(bounds.width(), bounds.height());
                mPathBorder.moveTo(bounds.width(), bounds.height());
                mPathBorder.lineTo(0, bounds.height() / 2);
                mPathBorder.lineTo(bounds.width(), 0);
                mPathBorder.lineTo(bounds.width(), bounds.height());
                break;
            case TOP:
                mPath.moveTo(borderSize, bounds.height());
                mPath.lineTo((bounds.width() / 2), borderSize);
                mPath.lineTo(bounds.width()-borderSize, bounds.height());
                mPath.lineTo(borderSize, bounds.height());
                mPathBorder.moveTo(0, bounds.height());
                mPathBorder.lineTo((bounds.width()) / 2, 0);
                mPathBorder.lineTo(bounds.width(), bounds.height());
                mPathBorder.lineTo(0, bounds.height());
                break;
            case RIGHT:
                mPath.moveTo(-borderSize, 0);
                mPath.lineTo(bounds.width()-borderSize, (bounds.height()) / 2);
                mPath.lineTo(-borderSize, bounds.height());
                mPath.lineTo(-borderSize, 0);
                mPathBorder.moveTo(0, 0);
                mPathBorder.lineTo(bounds.width(), (bounds.height()) / 2);
                mPathBorder.lineTo(0, bounds.height());
                mPathBorder.lineTo(0, 0);
                break;
            case BOTTOM:
                mPath.moveTo(0, -borderSize);
                mPath.lineTo((bounds.width()) / 2, bounds.height()-borderSize);
                mPath.lineTo(bounds.width(), -borderSize);
                mPath.lineTo(0, -borderSize);
                mPathBorder.moveTo(0, 0);
                mPathBorder.lineTo((bounds.width()) / 2, bounds.height());
                mPathBorder.lineTo(bounds.width(), 0);
                mPathBorder.lineTo(0, 0);
                break;
        }

        mPath.close();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        if (mPath == null)
            updatePath(getBounds());
        canvas.drawPath(mPathBorder, mPaintBorder);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    public void setColor(@ColorInt int color) {
        mPaint.setColor(color);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        if (mPaint.getColorFilter() != null) {
            return PixelFormat.TRANSLUCENT;
        }

        switch (mPaint.getColor() >>> 24) {
            case 255:
                return PixelFormat.OPAQUE;
            case 0:
                return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }
}
