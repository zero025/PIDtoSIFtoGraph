package de.bioquant.cytoscape.pidfileconverter.Model;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidInteractionIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidPathwayIdException;

/**
 * @author Florian Dittmann
 *
 */
public class IdClearer {

	public static String clearSource(final String pid) {
		if (pid.matches("http:.*#.*")) {
			String[] help = pid.split("#");
			if (help.length == 2) {
				return help[1];
			}
		}
		return pid;

	}

	public static String getSource(final String pid) {
		if (pid.matches("http:.*#.*")) {
			String[] help = pid.split("#");
			if (help.length == 2) {
				return help[0];
			}
		}
		return null;
	}

	public static String clearUpPidI(final String pidi)
			throws InvalidInteractionIdException {
		String pidNoSource = clearSource(pidi);
		if (!pidNoSource.startsWith("pid_i_"))
			throw new InvalidInteractionIdException("Invalid Interaction-ID: "+pidNoSource);

		return pidNoSource.replaceFirst("pid_i_", "");
	}

	public static String clearUpPidP(final String pidp)
			throws InvalidPathwayIdException {
		String pidNoSource = clearSource(pidp);
		if (!pidNoSource.startsWith("pid_p_"))
			throw new InvalidPathwayIdException();

		return pidNoSource.replaceFirst("pid_p_", "");
	}

	public static String clearUpPidM(final String pidm)
			throws InvalidIdException {
		String pidNoSource = clearSource(pidm);
		if (!pidNoSource.startsWith("pid_m_"))
			throw new InvalidInteractionIdException();

		return pidNoSource.replaceFirst("pid_m_", "");
	}

	public static boolean isValidPidI(final String pidi) {
		final String pidNoSource = clearSource(pidi);
		if (pidNoSource.startsWith("pid_i_"))
			return true;
		else
			return false;
	}

	public static boolean isValidPidP(final String pidp) {
		final String pidNoSource = clearSource(pidp);
		if (pidNoSource.startsWith("pid_p_"))
			return true;
		else
			return false;
	}

	public static boolean isValidPidX(final String pidx) {
		final String pidNoSource = clearSource(pidx);
		if (pidNoSource.startsWith("pid_x_"))
			return true;
		else
			return false;
	}

	public static boolean isValidPidM(final String pidm) {
		final String pidNoSource = clearSource(pidm);
		if (pidNoSource.startsWith("pid_m_"))
			return true;
		else
			return false;
	}

}
