package com.eco.bio7.spatial;

import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.awt.GLCanvas;

public class Fullscreen extends Frame {

	private static final long serialVersionUID = 1L;
	private boolean fullscreen = false;
	private boolean displayChanged = false;
	private GraphicsEnvironment graphicEnvironment = null;
	private GraphicsDevice graphicDevice = null;
	private DisplayMode currentMode, oldMode;
	private static int refreshRate = 60;
	private static Fullscreen screen;
	private static int bit = 32;
	private static int monitorHeight = 768;
	private static int monitorWidth = 1024;

	public Fullscreen(final GLCanvas canvas) {
		String osname = System.getProperty("os.name");
		boolean isWin = osname.startsWith("Windows");
		
		boolean isVista = true;//isWin && (osname.indexOf("Vista") != -1 || osname.indexOf(" 7") != -1);

		screen = this;
		graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicDevice = graphicEnvironment.getDefaultScreenDevice();
		oldMode = graphicDevice.getDisplayMode();
		currentMode = oldMode;
		if (isVista == false) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					add(canvas);
					init();
					
					
					 
				}
			});
			
		} else {
			add(canvas);
			setUndecorated(true);

			setExtendedState(MAXIMIZED_BOTH);
			setVisible(true);
			requestFocus();
		}

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				exit();

			}
		});
		
      
		
	}

	public boolean init() {
		setUndecorated(true);

		if (graphicDevice.isFullScreenSupported()) {

			try {
				graphicDevice.setFullScreenWindow(this);
				fullscreen = true;
			} catch (Exception e) {
				graphicDevice.setFullScreenWindow(null);
				fullscreen = false;
			}
			/* Change the settings if it is supported! */

			switchDisplayMode();

		}

		return fullscreen;
	}

	/* Switch to a different display mode! */
	private void switchDisplayMode() {
		if (fullscreen && graphicDevice.isDisplayChangeSupported()) { // Change

			try {

				graphicDevice.setDisplayMode(new DisplayMode(monitorWidth, monitorHeight, bit, refreshRate));
				displayChanged = true;
			} catch (RuntimeException e) {
				graphicDevice.setDisplayMode(oldMode);
				displayChanged = false;

				e.printStackTrace();
			}

		}
	}

	public void exit() {

		if (fullscreen) {

			GraphicsEnvironment graphicEnvironment = null;
			GraphicsDevice graphicsDevice = null;
			graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicsDevice = graphicEnvironment.getDefaultScreenDevice();
			if (graphicsDevice.isFullScreenSupported()) {
				if (displayChanged) {
					graphicDevice.setDisplayMode(oldMode);
				}

				fullscreen = false;

				graphicsDevice.setFullScreenWindow(null);

			}
		}

		dispose();
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public void setMonitorHeight(int monitorHeight) {
		this.monitorHeight = monitorHeight;
	}

	public void setMonitorWidth(int monitorWidth) {
		this.monitorWidth = monitorWidth;
	}

	public static void setFullscreenOptions(int x, int y, int bitDepth, int refresh) {
		bit = bitDepth;
		monitorHeight = y;
		monitorWidth = x;
		refreshRate = refresh;
	}

}
