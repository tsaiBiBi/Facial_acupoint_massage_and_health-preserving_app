/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.acupuncture.facedetector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.example.acupuncture.camera.GraphicOverlay;
import com.example.acupuncture.camera.GraphicOverlay.Graphic;
import com.example.dataclass.Acup_pos;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;

import java.util.List;
/**
 * Graphic instance for rendering face position, contour, and landmarks within the associated
 * graphic overlay view.
 */
public class FaceGraphic extends Graphic {
    List<Acup_pos> pos;
    private static final String ACTIVITY_TAG="LogDemo";
    private static final float FACE_POSITION_RADIUS = 4.0f;
    private static final float ID_TEXT_SIZE = 30.0f;
    private static final float ID_Y_OFFSET = 40.0f;
    private static final float ID_X_OFFSET = -40.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private final Paint facePositionPaint;

    private final Paint acupPaint;
    private volatile Face face;
    private float length_rate; // 座標點 = cm * rate

    FaceGraphic(GraphicOverlay overlay, Face face, List<Acup_pos> pos) {
        super(overlay);

        this.face = face;
        final int selectedColor = Color.WHITE;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);

        acupPaint = new Paint();
        acupPaint.setColor(Color.BLACK);

        this.pos = pos;
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {

        Face face = this.face;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getBoundingBox().centerX());
        float y = translateY(face.getBoundingBox().centerY());
//        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, facePositionPaint);

        // Calculate positions.
        float left = x - scale(face.getBoundingBox().width() / 2.0f);
        float top = y - scale(face.getBoundingBox().height() / 2.0f);
        float right = x + scale(face.getBoundingBox().width() / 2.0f);
        float bottom = y + scale(face.getBoundingBox().height() / 2.0f);
        float lineHeight = ID_TEXT_SIZE + BOX_STROKE_WIDTH;

        get_length_rate();
        for(int i = 0; i < pos.size(); i++){
            float[] coordinate;
            Acup_pos point = pos.get(i);

            if(point.pos_type.equals("PT")){
                coordinate = mark_on_point(point);
            }
            else if(point.pos_type.equals("BT")){
                coordinate = mark_between_point(point);
            }
            else if(point.pos_type.equals("CS")){
                coordinate = mark_cross_point(point);
            }
            else
                continue;

            // 熊貓(耳朵)
            canvas.drawCircle((float)(coordinate[0]*0.98), (float)(coordinate[1] * 0.99), 5,acupPaint);
            canvas.drawCircle((float)(coordinate[0]*1.02), (float)(coordinate[1] * 0.99),5,acupPaint);
            // 熊貓(臉)
            canvas.drawCircle((float)coordinate[0], (float)(coordinate[1]), 15, facePositionPaint);
            // 熊貓(眼睛)
            canvas.drawCircle((float)(coordinate[0]*1.01), (float)(coordinate[1] * 1),4,acupPaint);
            canvas.drawCircle((float)(coordinate[0]*0.99), (float)(coordinate[1] * 1),4,acupPaint);
        }
    }
    // 取得 cm 與 座標 的比例 (座標點/cm)
    public void get_length_rate(){
        // 平均眼距估實際長度 (正常大人約 58~62 mm)
        PointF r_eye = face.getContour(FaceContour.RIGHT_EYE).getPoints().get(0);
        PointF l_eye = face.getContour(FaceContour.LEFT_EYE).getPoints().get(8);
        float eye_dist =  translateX(l_eye.x) - translateX(r_eye.x);
        this.length_rate = eye_dist / 6.0f;
    }
    public float[] mark_on_point(Acup_pos point){
        float[] coordinate = new float[2]; // 座標 x(0), y(1)
        PointF a_point = face.getContour(point.a_con).getPoints().get(point.a_idx);

        Log.e("(b8)IN", String.valueOf(this.length_rate));
        coordinate[0] = translateX(a_point.x) + ((float)point.bias_x*length_rate);
        coordinate[1] = translateY(a_point.y) + ((float)point.bias_y*length_rate);
        return coordinate;
    }

    public float[] mark_between_point(Acup_pos point){
        float[] coordinate = new float[2]; // 座標 x(0), y(1)
        PointF a_point = face.getContour(point.a_con).getPoints().get(point.a_idx);
        PointF b_point = face.getContour(point.b_con).getPoints().get(point.b_idx);

        coordinate[0] = (translateX(a_point.x) + translateX(b_point.x)) * (float)point.ratio + (float)point.bias_x;
        coordinate[1] = (translateY(a_point.y) + translateY(b_point.y)) * (float)point.ratio + (float)point.bias_y;
        return coordinate;
    }

    public float[] mark_cross_point(Acup_pos point){
        float[] coordinate = new float[2];
        float a_x = translateX(face.getContour(point.a_con).getPoints().get(point.a_idx).x);
        float a_y = translateY(face.getContour(point.a_con).getPoints().get(point.a_idx).y);

        float b_x = translateX(face.getContour(point.b_con).getPoints().get(point.b_idx).x);
        float b_y = translateY(face.getContour(point.b_con).getPoints().get(point.b_idx).y);

        float c_x = translateX(face.getContour(point.c_con).getPoints().get(point.c_idx).x);
        float c_y = translateY(face.getContour(point.c_con).getPoints().get(point.c_idx).y);

        float d_x = translateX(face.getContour(point.d_con).getPoints().get(point.d_idx).x);
        float d_y = translateY(face.getContour(point.d_con).getPoints().get(point.d_idx).y);

        // line a-b
        float line_ab_m = (a_y - b_y)/(a_x - b_x);
        float line_ab_b = a_y - (line_ab_m * a_x);

        // line c-d
        float line_cd_m = (c_y - d_y)/(c_x - d_x);
        float line_cd_b = c_y - (line_cd_m * c_x);

        // cross point
        float cross_p_x = (line_cd_b - line_ab_b) / (line_ab_m - line_cd_m);
        float cross_p_y = line_ab_m*cross_p_x + line_ab_b;

        coordinate[0] = cross_p_x;
        coordinate[1] = cross_p_y;
        return coordinate;
    }
}
