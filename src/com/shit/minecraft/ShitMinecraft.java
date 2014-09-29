package com.shit.minecraft;

import com.shit.minecraft.util.Player;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * Created by eamon_000(and more so REUBEN)`on 9/17/2014.
 * credit to Eamon, Adam, REUBEN, Zach
 *
 */
public class ShitMinecraft {
    public static boolean closeRequested = false;
    public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public static float FOV = 50f;
    public static float angle = 0f;
    public static Player p = new Player(new Vector3f(0f, 0f, 1f), new Vector3f(1f, 0f, 0f), new Vector3f(0f, 1f, 0f), new Vector3f(0f, 0f, 1f), 0f, 0f, new Vector3f(-5f, 10f, -10f), new Vector3f(-5f, 10f, 0f));
    public static boolean isPaused = false;
    public static void main(String[] args){
		System.out.println("Hello Woldr");
        System.out.println("Reuben can't spell...");
        System.out.println("Zach forgot a semicolon");
        
        run();
        cleanup();




	}

    public static void cleanup() {
        Display.destroy();
    }

    public static void waitabit() {
        try {
            Thread.sleep(10);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //initializes all openGL functions, sets display mode, and enables depth buffers etc
    public static void initGL(){
        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(FOV, (float) Display.getWidth() / Display.getHeight(), 0.1f, 200f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glShadeModel(GL_SMOOTH);
        glClearColor(0f, 0f, 0f, 0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glTranslatef(0f, 0f, -10f);


    }
    //initializes display, sets the screen size, and grabs the mouse
    public static void init(){
        try {
            Display.setDisplayMode(new DisplayMode(640, 400));
            Display.setTitle("ShittyMinecraft");

            Display.create();
            Mouse.setGrabbed(true);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.getDX();
            Mouse.getDY();

        } catch (LWJGLException e) {
            e.printStackTrace();
        }

    }
    //contains main loop as well as the logic processes and ensuring that the application doesn't take all of the cpu cycles when in the background
    public static void run(){

        init();
        initGL();
        while (!closeRequested) {
            //every game loop, update the display, and swap the buffers
            //essentially pushed rendered scene to front, and refreshes the screen for
            //future updates

            Display.update();
            if (Display.isActive()) {

                logic();

                if (!isPaused) render();
                else menuRender();


            } else {
                //if the display is not active, then sleep for 100 milliseconds to give the
                //cpu time off.
                //if the display isn't active, then the user has
                //alt tabbed etc, and nothing needs to be done.
                //may fix in the future, to process logic in the background, especially if
                //multiplayer implemented.
                if (!Display.isDirty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    logic();
                    if (!isPaused) render();
                    else menuRender();
                }
            }


        }

    }
    //calls the logical functions, calculating look position, change in movement
    //also calculates physics (eventually) and generally does all the heavy lifting
    //most of this will be calls to other functions
    public static void menuRender() {

    }

    public static void pausedInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            isPaused = !isPaused;
            waitabit();
        }
    }

    public static void menuLogic() {

    }

    public static void input() {
        if (Display.isCloseRequested()) {
            closeRequested = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            closeRequested = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            p.forward(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            p.forward(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            p.strafe(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            p.strafe(1);
        }
        if (Mouse.isButtonDown(0)) {
            //fire a projectile
            Projectile f = new Projectile(new Vector3f(p.getlook().x, p.getlook().y, p.getlook().z), .5f, 500, new Vector3f(p.getPos().x, p.getPos().y, p.getPos().z), new Vector3f(p.getXvec().x, p.getXvec().y, p.getXvec().z), new Vector3f(p.getYvec().x, p.getYvec().y, p.getYvec().z), p.getPitch(), p.getYaw());
            //f.update();
            projectiles.add(f);
            System.out.println("PROJECTILE FIRED");

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
            isPaused = !isPaused;
            waitabit();
        }
    }

    public static void logic() {
        Display.sync(60);
        if (!isPaused) {
            camera();
            input();
            projectileLogic();

        } else {
            pausedInput();
            menuLogic();

        }
        //not important code
        if (angle < 360) angle += 1f;
        else angle = 0;


    }

    //iterates through projectiles, updates position/passes control to projectiles
    public static void projectileLogic() {
        ArrayList<Projectile> tmp = new ArrayList<Projectile>();
        for (Projectile p : projectiles) {
            p.update();
            if (p.destroy) {
                tmp.add(p);
            }
        }
        for (Projectile p : tmp) {
            projectiles.remove(p);
        }


    }
    //camera control class
    public static void camera() {
        //needs to poll for mouse position changes, reset mouse position to middle each frame
        //update look position by rotating along appropriate axis by angle of delta mouse x and y
        //multiplied by the mouse sensitivity
        //calculate forward, backward, left right movement
        //multiply the speed in each direction by the unit vector for that direction (local axes)
        //add to the lookPos and the camPos

        //poll for mouse
        float mouseX = Mouse.getDX();
        float mouseY = Mouse.getDY();
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        p.cYaw((-mouseX * .05f));
        p.cPitch((mouseY * .05f));
        p.lookAt();


    }

    //self explanatory. update display, render out shapes etc.
    //will have two render modes, 2d and 3d, for the menus and the general world
    //also calls many other external functions to render out the scene

    public static void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (Projectile p : projectiles) {
            p.render();
        }
        //glLoadIdentity();

        //glTranslatef(0f, 0f, -10f);

        //glRotatef(angle, 0f, 1f, 0f);
        //glRotatef(angle, 1f, 0f, 0f);

        glPushMatrix();
        glTranslatef(0f, 0f, -20f);
        glRotatef(angle, 0f, 1f, 0f);
        glRotatef(angle, 1f, 0f, 0f);
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

        glBegin(GL_QUADS);
        glVertex3f(-10f, -10f, -10f);
        glVertex3f(10f, -10f, -10f);
        glVertex3f(10f, -10f, 10f);
        glVertex3f(-10f, -10f, 10f);
        glEnd();
        glPushMatrix();

        glBegin(GL_LINES);
        glColor3f(0f, 0f, 1f);
        glVertex3f(-10f, 0f, 0f);
        glVertex3f(10f, 0f, 0f);
        glColor3f(0f, 1f, 0f);
        glVertex3f(0f, 10f, 0f);
        glVertex3f(0f, -10f, 0f);
        glColor3f(1f, 0f, 0f);
        glVertex3f(0f, 0f, -10f);
        glVertex3f(0f, 0f, 10f);
        glEnd();
        glPopMatrix();
    }

}
