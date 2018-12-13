package new_simvasos.model.Enums;

/**
 * Enumeration for task criticality definition (last updated: 18-07-20)
 *
 * CATEGORY_A: Catastrophic consequences if failed
 *      - Loss of life, Severe detrimental environmental effects
 * CATEGORY_B: Critical consequences if failed
 *      - Temporarily disabling but not life-threatening injury, Major deterimental environmental effects
 * CATEGORY_C: Major consequences if failed
 *      - Degradation
 * CATEOGRY_D: Minor or negligible consequences if failed
 *      - Minor degradation
 *
 * Reference: D. Escorial Rico, "Software Criticality Classification and Reduction,"
 * Workshop of Critical Software (WOCS)
 */
public enum EnumTaskCriticality {
    CATEGORY_A, //Catastrophic consequences if failed
    CATEGORY_B,
    CATEGORY_C,
    CATEGORY_D
}
