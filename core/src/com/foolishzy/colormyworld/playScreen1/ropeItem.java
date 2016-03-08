package com.foolishzy.colormyworld.playScreen1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by foolishzy on 2016/3/7.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class ropeItem {
    /*quadraticFunction*/
    private class quadraticFunction {

        private Vector2 p1, p2;
        private float a, b, c;

        public quadraticFunction(Vector2 point1, Vector2 point2, float c) {
            //basic
            this.p1 = point1;
            this.p2 = point2;
            this.c = c;

            float x1 = p1.x;
            float x2 = p2.x;
            float y1 = p1.y;
            float y2 = p2.y;
            //a, b
            a = (x2 * y1 - x2 * c - x1 * y2 + x1 * c) /
                    (x1 * x1 * x2 - x2 * x2 * x1);

            b = (y1 - c - a * x1 * x1) / x1;

            //check
            float test_b = (y2 - c - a * x2 * x2) / x2;

            if (b == test_b) {
                Gdx.app.log("check result: ", " right");
                a = 0;
                b = 0;
            } else {
                Gdx.app.log("check result: ", "false, please check the method");
            }

        }
    }
    /******************/
}