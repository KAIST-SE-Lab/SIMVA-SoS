package new_simvasos.model.Enums;

/**
 * Enumeration for action priorities (last updated: 18-07-19)
 *
 * MANDATORY: unconditionally selected and executed by the simulator
 * VERY_HIGH ~ VERY_LOW: level of priorities
 * EXCLUDED: unconditionally not-selected and excluded by the simulator
 */
public enum EnumActionPriority {
    MANDATORY,  //Mandatory action
    VERY_HIGH,  //Very high priority action
    HIGH,
    MIDDLE,
    LOW,
    VERY_LOW,
    EXCLUDED
}
