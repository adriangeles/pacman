/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adri�n �ngeles Ram�n
 * Universidad: Polit�cnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.persistence;


/**
 * Objeto que representa una puntuaci�n del pacman
 * Es un mapeo directo con la base de datos.
 */
public class ScoreTO {
	
	private String name;         // Iniciales grabadas al registrar la puntuaci�n
	private String module;       // M�dulo en el que se obtuvo la puntuaci�n
	private String level;        // Nivel en el que se consigui� dicha puntuaci�n
	private String time;         // Fecha en la que se consigui� el record
	private String score;        // Puntuaci�n
	
	
	public ScoreTO(String name, String module, String level, String time, String score) {
		this.name = name;
		this.module = module;
		this.level = level;
		this.time = time;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	public String getModule() {
		return module;
	}
	public String getLevel() {
		return level;
	}
	public String getTime() {
		return time;
	}
	public String getScore() {
		return score;
	}
}
