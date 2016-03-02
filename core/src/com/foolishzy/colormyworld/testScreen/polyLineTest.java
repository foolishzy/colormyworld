package com.foolishzy.colormyworld.testScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;

/**
 * Created by foolishzy on 2016/2/22.
 * <p/>
 * funcction:
 *       test polyline item's position
 * <p/>
 * others:
 */
public class polyLineTest extends MyScreen {
 private Texture grid;
 //path is map path
 public polyLineTest(ColorMyWorldGame game,String path) {
  super(game);
  this.map = new TmxMapLoader().load(path);
  grid = drawGrid();
  defineB2d();
 }

 private void defineB2d(){
  Array<PolylineMapObject> object = map.getLayers().get(0).getObjects().getByType(PolylineMapObject.class);
  for (PolylineMapObject polylineMapObject : object) {
   Polyline polyline = polylineMapObject.getPolyline();
   float[] vertice = polyline.getVertices();
   for (int i = 0; i < vertice.length; i++) {
    vertice[i] = vertice[i] / this.getPPM();
   }
   //bodyDef
   BodyDef bdf = new BodyDef();
   bdf.position.set(polyline.getX() / getPPM(),
           polyline.getY() / getPPM());
   bdf.type = BodyDef.BodyType.StaticBody;
   //fixture
   FixtureDef fdf = new FixtureDef();
   PolygonShape shape = new PolygonShape();
   shape.set(vertice);
   fdf.shape = shape;
   world.createBody(bdf).createFixture(fdf);

  }
  }

 @Override
 public void show() {
  super.show();
 }

 @Override
 public void render(float delta) {
  super.render(delta);
  update();
  //clear
  Gdx.gl.glClearColor(1, 1, 1, 1);
  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  //draw background
  batch.begin();
  batch.draw(grid, 0, 0);
  batch.end();
  //draw b2d item
  b2drender.render(world, gameCam.combined);
 }

 private void update(){
 //setup world
  world.step(60f, 6, 4);
 }

 private Texture drawGrid(){
  Pixmap pixmap = new Pixmap(800, 400, Pixmap.Format.RGBA8888);
  pixmap.setColor(0, 0, 1, 1);
  for (int x = 10; x < 800; x += 10){
   pixmap.drawLine(x, 0, x, 400);
  }
  for (int y = 10; y < 400; y += 10 ){
   pixmap.drawLine(0, y, 800, y);
  }
  return new Texture(pixmap);
 }

 @Override
 public void dispose() {
  super.dispose();
 }
}
