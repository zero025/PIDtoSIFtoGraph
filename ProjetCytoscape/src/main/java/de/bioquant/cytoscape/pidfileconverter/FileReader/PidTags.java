package de.bioquant.cytoscape.pidfileconverter.FileReader;

public enum PidTags {
	
	INTERACTIONLIST("InteractionList"),
	INTERACTION("Interaction"),
	INTERACTIONCOMPONENTLIST("InteractionComponentList"),
	INTERACTIONCOMPONENT("InteractionComponent"),
	ABSTRACTION("Abstraction"),
	POSITIVECONDITION("PositiveCondition"),
	LABELTYPE("LabelType"),
	LABELVALUELIST("LabelValueList"),
	LABELVALUE("LabelValue"),
	MOLECULELIST("MoleculeList"),
	MOLECULE("Molecule"),
	NAME("Name"),
	LABEL("Label"),
	PTMEXPRESSION("PTMExpression"),
	PTMTERM("PTMTerm"),
	FAMILYMEMBERLIST("FamilyMemberList"),
	MEMBER("Member"),
	PART("Part"),
	COMPLEXCOMPONENTLIST("ComplexComponentList"),
	COMPLEXCOMPONENT("ComplexComponent"),
	UNKNOWN("");
	
	private String name;
	private boolean isIn;
	
	private PidTags(String name){
		this.name=name;
		this.isIn=false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	public static PidTags getValue(String role)
	{
		try {
            return valueOf(role.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
	}

	/**
	 * @return the isIn
	 */
	public final boolean isIn() {
		return isIn;
	}

	/**
	 * @param isIn the isIn to set
	 */
	public final void setIn(boolean isIn) {
		this.isIn = isIn;
	}

}
