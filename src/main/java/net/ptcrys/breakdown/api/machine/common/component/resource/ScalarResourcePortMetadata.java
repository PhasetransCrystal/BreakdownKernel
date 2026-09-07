package net.ptcrys.breakdown.api.machine.common.component.resource;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.recipe.BuiltinBdResourceIntegrations.BdResourceIntegration;
import net.ptcrys.breakdown.api.recipe.common.ScalarRecipeCapability;
import net.ptcrys.topo.api.machine.component.AttachmentType;
import net.ptcrys.topo.api.machine.resource.MachineResourceType;
import net.ptcrys.topo.api.machine.resource.PortAccess;
import net.ptcrys.topo.api.machine.resource.ResourcePortMetadata;

import net.neoforged.neoforge.transfer.resource.Resource;

import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

/** Declaration metadata for one scalar buffer port: which scalar, how much, IO policy, optional filter. */
public record ScalarResourcePortMetadata(
                                         BdResourceIntegration<ScalarResource, ScalarRecipeCapability> integration,
                                         int capacity,
                                         PortAccess policy,
                                         @Nullable Predicate<Resource> resourceFilter)
        implements ResourcePortMetadata {

    public static final AttachmentType<ScalarResourcePortMetadata> TYPE = BreakdownApiPlugin.INSTANCE.machine().attachmentType("scalar_resource_port", ScalarResourcePortMetadata.class);

    public ScalarResourcePortMetadata {
        Objects.requireNonNull(integration, "scalar resource integration");
        if (capacity <= 0) {
            throw new IllegalArgumentException("Scalar resource port capacity must be > 0 (was " + capacity + ")");
        }
        Objects.requireNonNull(policy, "resource port policy");
    }

    public ScalarResourcePortMetadata(
                                      BdResourceIntegration<ScalarResource, ScalarRecipeCapability> integration,
                                      int capacity,
                                      PortAccess policy) {
        this(integration, capacity, policy, null);
    }

    public ScalarResource resource() {
        return integration.recipeCapability().resource();
    }

    @Override
    public MachineResourceType<ScalarResource> resourceType() {
        return integration.resourceType();
    }

    @Override
    public boolean recipePoolIsolatable() {
        return false;
    }

    @Override
    public AttachmentType<ScalarResourcePortMetadata> type() {
        return TYPE;
    }

    /** Only activates class initialization ({@link #TYPE} registers in the field initializer). */
    public static void init() {}
}
