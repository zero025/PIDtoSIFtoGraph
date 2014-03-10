/**
 * @contributor Yamei Sun & Thomas Brunel
 */

package de.bioquant.cytoscape.pidfileconverter.Naming;

public final class CreatorIDWithModification extends AbstractNameCreatorWithModification {

	private static CreatorIDWithModification instance = null;

	private CreatorIDWithModification() {
	}

	public static CreatorIDWithModification getInstance() {
		if (null == instance)
			instance = new CreatorIDWithModification();
		return instance;
	}
}
