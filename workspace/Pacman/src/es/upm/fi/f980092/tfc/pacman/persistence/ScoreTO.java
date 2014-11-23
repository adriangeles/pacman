/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.persistence;


/**
 * Objeto que representa una puntuación del pacman
 * Es un mapeo directo con la base de datos.
 */
public class ScoreTO {
	
	private String name;         // Iniciales grabadas al registrar la puntuación
	private String module;       // Módulo en el que se obtuvo la puntuación
	private String level;        // Nivel en el que se consiguió dicha puntuación
	private String time;         // Fecha en la que se consiguió el record
	private String score;        // Puntuación
	
	
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
