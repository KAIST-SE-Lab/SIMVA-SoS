package new_simvasos.model.Enums;

/**
 * Enumeration for CS type definition (last updated: 18-07-19)
 * CS type definition depends on the type of an organization (EnumOrgType: directed ~ virtual)
 *
 * ADMIN: administrator of an organization, very high authority
 * MANAGER: manager, high authority
 * MIDDLE_MANAGER: middle manager, middle authority
 * MEDIATOR: mediator between systems, meddle-low authority
 * ORDINARY: ordinary system, low authority
 * PASSIVE: passively operating system, very-low authority
 */
public enum EnumCSType {
    ADMIN,
    MANAGER,
    MIDDLE_MANAGER,
    MEDIATOR,
    ORDINARY,
    PASSIVE
}
