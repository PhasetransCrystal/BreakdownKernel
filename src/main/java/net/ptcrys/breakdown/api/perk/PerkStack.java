package net.ptcrys.breakdown.api.perk;

public record PerkStack(Perk perk, float level) {

    public PerkStack withLevel(float level) {
        return new PerkStack(this.perk, level);
    }
}
