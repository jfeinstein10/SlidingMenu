package com.jeremyfeinstein.slidingmenu.lib;

import android.graphics.Canvas;
import android.view.animation.Interpolator;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class CanvasTransformerBuilder {

    private static Interpolator lin = new Interpolator() {
        public float getInterpolation(float t) {
            return t;
        }
    };

    public CanvasTransformer zoom(final int openedX, final int closedX,
                                  final int openedY, final int closedY,
                                  final float px, final float py) {
        return zoom(openedX, closedX, openedY, closedY, px, py, lin);
    }

    public CanvasTransformer zoom(final int openedX, final int closedX,
                                  final int openedY, final int closedY,
                                  final float px, final float py, final Interpolator interp) {
        return new CanvasTransformer() {
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float f = interp.getInterpolation(percentOpen);
                canvas.scale((openedX - closedX) * f + closedX,
                        (openedY - closedY) * f + closedY, px, py);
            }
        };
    }

    public CanvasTransformer rotate(final int openedDeg, final int closedDeg,
                                    final float px, final float py) {
        return rotate(openedDeg, closedDeg, px, py, lin);
    }

    public CanvasTransformer rotate(final int openedDeg, final int closedDeg,
                                    final float px, final float py, final Interpolator interp) {
        return new CanvasTransformer() {
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float f = interp.getInterpolation(percentOpen);
                canvas.rotate((openedDeg - closedDeg) * f + closedDeg,
                        px, py);
            }
        };
    }

    public CanvasTransformer translate(final int openedX, final int closedX,
                                       final int openedY, final int closedY) {
        return translate(openedX, closedX, openedY, closedY, lin);
    }

    public CanvasTransformer translate(final int openedX, final int closedX,
                                       final int openedY, final int closedY, final Interpolator interp) {
        return new CanvasTransformer() {
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float f = interp.getInterpolation(percentOpen);
                canvas.translate((openedX - closedX) * f + closedX,
                        (openedY - closedY) * f + closedY);
            }
        };
    }

    public CanvasTransformer concatTransformer(final CanvasTransformer... args) {
        return new CanvasTransformer() {
            public void transformCanvas(Canvas canvas, float percentOpen) {
                for (final CanvasTransformer arg : args) {
                    arg.transformCanvas(canvas, percentOpen);
                }
            }
        };
    }

}
