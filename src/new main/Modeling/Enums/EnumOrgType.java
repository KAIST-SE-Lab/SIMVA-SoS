package Enums;

/**
 * Enumeration for organization types (last updated: 18-07-19)
 *
 * VIRTUAL: Virtual type organization               -- no SoS-level goal, no management, no ownership
 * COLLABORATIVE: Collaborative type organization   -- no management, no ownership
 * ACKNOWLEDGED: Acknowledged type organization     -- no ownership
 * DIRECTED: Directed type organization             -- Explicit goal, Enforcement to subordinated CSs
 */
public enum EnumOrgType {
    VIRTUAL, 
    COLLABORATIVE,
    ACKNOWLEDGED,
    DIRECTED
}
