package es.upm.fi.f980092.tfc.gameengine.physics;

public class PhysicsProperties {

	private int[] position = null; // Posicion
	private int[] velocity = null; // Vector de velocidad
	private int[] aceleration = null; // Vector de acelareacion
	private int[] acelerationTime = null; // Tiempo de aceleracion
	private boolean jump = false;

	// Getter && Setter

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public int[] getVelocity() {
		return velocity;
	}

	public void setVelocity(int[] velocity) {
		this.velocity = velocity;
	}

	public int[] getAceleration() {
		return aceleration;
	}

	public void setAceleration(int[] aceleration) {
		this.aceleration = aceleration;
	}

	public int[] getAcelerationTime() {
		return acelerationTime;
	}

	public void setAcelerationTime(int[] acelerationTime) {
		this.acelerationTime = acelerationTime;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
}
