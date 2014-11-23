package es.upm.fi.f980092.tfc.gameengine.ia;

import es.upm.fi.f980092.tfc.gameengine.scene.A_SceneElement;

public interface I_Behaviour {

	void move(A_SceneElement aSceneElement, int step);
	
	void restart();

}
