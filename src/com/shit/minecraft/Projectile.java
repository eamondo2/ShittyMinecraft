package com.shit.minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by eamondo2 on 9/25/14.
 */
public class Projectile {
    //just fuckin around
    public Vector3f pos;
    public int CUTOFF;
    public int counter = 0;
    public float speed;
    public boolean destroy = false;
    public float pitch;
    public float yaw;
    public Vector3f Xvec;
    public Vector3f Yvec;
    public Vector3f dir = new Vector3f(0f, 0f, 0f);

    public Projectile(Vector3f dir, float speed, int CUTOFF, Vector3f p, Vector3f Xvec, Vector3f Yvec, float pitch, float yaw) {
        this.dir = dir;
        this.Xvec = Xvec;
        this.Yvec = Yvec;
        this.CUTOFF = CUTOFF;
        this.speed = speed;
        this.pos = p;
        this.pos.y -= 10;

    }

    public void destroy() {
        destroy = true;
    }

    public void render() {
        //render proj
        glPushMatrix();
        glTranslatef(pos.x, pos.y, pos.z);
        glRotatef(pitch, Xvec.x, Xvec.y, Xvec.z);
        glRotatef(yaw, Yvec.x, Yvec.y, Yvec.z);
        GL11.glBegin(GL11.GL_QUADS);

        // Front Face
        glColor3f(0f, 1f, 0f);
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);   // Bottom Left Of The Texture and Quad

        GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Right Of The Texture and Quad

        GL11.glVertex3f(1.0f, 1.0f, 1.0f);   // Top Right Of The Texture and Quad

        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);   // Top Left Of The Texture and Quad

        // Back Face
        glColor3f(1f, 0f, 0f);
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad

        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Right Of The Texture and Quad

        GL11.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Left Of The Texture and Quad

        GL11.glVertex3f(1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad

        // Top Face
        glColor3f(0f, 0f, 1f);
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left Of The Texture and Quad

        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);   // Bottom Left Of The Texture and Quad

        GL11.glVertex3f(1.0f, 1.0f, 1.0f);   // Bottom Right Of The Texture and Quad

        GL11.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Texture and Quad

        // Bottom Face
        glColor3f(1f, 0f, 0f);
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Right Of The Texture and Quad

        GL11.glVertex3f(1.0f, -1.0f, -1.0f);   // Top Left Of The Texture and Quad

        GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Left Of The Texture and Quad

        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);   // Bottom Right Of The Texture and Quad

        // Right face

        GL11.glVertex3f(1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad

        GL11.glVertex3f(1.0f, 1.0f, -1.0f);   // Top Right Of The Texture and Quad

        GL11.glVertex3f(1.0f, 1.0f, 1.0f);   // Top Left Of The Texture and Quad

        GL11.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom Left Of The Texture and Quad

        // Left Face

        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad

        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);   // Bottom Right Of The Texture and Quad

        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);   // Top Right Of The Texture and Quad

        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);   // Top Left Of The Texture and Quad
        GL11.glEnd();
        glPopMatrix();
    }

    public void update() {


        //System.out.println("LOOKPOS" + pos.x + " " + pos.y + " " + pos.z);
        //dir = dir.normalise(dir);
        pos.x -= (dir.x * speed * ShitMinecraft.delta);
        pos.y -= (dir.y * speed * ShitMinecraft.delta);
        pos.z -= (dir.z * speed * ShitMinecraft.delta);
        counter++;


        if (counter >= CUTOFF) {
            this.destroy();
        }
    }


}
