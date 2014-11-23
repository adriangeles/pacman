package es.upm.fi.f980092.tfc.gameengine.ia.utils.pathfinder;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
	public int x;
	public int y;
	
	public List<Vertex> adjacent;
	
	public Vertex(int x, int y , List<Vertex> adjacent) {
		this.x = x;
		this.y = y;
		this.adjacent = ( adjacent == null) ? new ArrayList<Vertex>() : adjacent ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
