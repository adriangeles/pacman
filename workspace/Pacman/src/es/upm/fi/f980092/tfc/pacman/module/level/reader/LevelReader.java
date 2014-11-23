/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.pacman.module.level.reader;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import es.upm.fi.f980092.tfc.pacman.activities.Boot;
import es.upm.fi.f980092.tfc.pacman.module.level.Level;

/**
 * Esta clase es la responsable de leer escenarios guardos en formato
 * XML en una ruta fisica. Se pueden elegir entre dos tipos de procesamiento.
 * Uno corto que procesa solo la información basica escenario que esta compuesta
 * por el nombre y la descripción del mismo. La información completa contiene
 * geometrias, render ... y es utilizado para mostrar el escenario en OpenGL
 */
public class LevelReader {
	
	public static Level read(String path, Boot log) throws ParserConfigurationException, IOException, SAXException {
		
		if (log != null) {
			String[] dirs = path.split("/");						
			log.updateLog("   Nivel: " + dirs[dirs.length-1] + "\n", false);
		}
		
		SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
		SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
		XMLReader myXMLReader = mySAXParser.getXMLReader();
		LevelHandler handler = new FullLevelHandler(path,log); 
		myXMLReader.setContentHandler(handler);		
		FileReader fr = new FileReader(path + "/level.xml");		
		myXMLReader.parse( new InputSource(fr));
		
		return handler.getWorld();
	}
}
