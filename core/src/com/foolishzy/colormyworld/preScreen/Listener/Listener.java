package com.foolishzy.colormyworld.preScreen.Listener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.Sprite.Box;

/**
 * Created by foolishzy on 2016/2/1.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class Listener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(fixA.getFilterData().categoryBits == ColorMyWorldGame.DESTINATION_BIT ||
                fixB.getFilterData().categoryBits == ColorMyWorldGame.DESTINATION_BIT){
            //get box
            Fixture box = fixA.getFilterData().categoryBits == ColorMyWorldGame.DESTINATION_BIT ?
                    fixB : fixA;
            ((Box) box.getUserData()).getDestination();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
