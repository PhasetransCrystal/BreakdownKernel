package net.phasetranscrystal.breacore.api.material.registry;

import net.phasetranscrystal.breacore.api.material.Element;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.material.attributes.MaterialAttribute;
import net.phasetranscrystal.breacore.api.material.attributes.MaterialAttributeSet;
import net.phasetranscrystal.breacore.api.material.info.MaterialIconLayer;
import net.phasetranscrystal.breacore.api.material.info.MaterialIconSet;
import net.phasetranscrystal.breacore.api.material.stack.MaterialStack;
import net.phasetranscrystal.breacore.api.material.variants.MaterialVariant;
import net.phasetranscrystal.breacore.data.materials.BreaMaterials;

import net.minecraft.resources.Identifier;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class MaterialBuilder implements IMaterialBuilderExtension {

    private final Material.MaterialInfo materialInfo;
    private final MaterialAttributeSet attributeSet;
    private Set<MaterialVariant> ignoredVariants = null;

    private String formula = null;

    /*
     * The temporary list of components for this Material.
     */
    private List<MaterialStack> composition = new ArrayList<>();

    /*
     * Temporary value to use to determine how to calculate default RGB
     */
    private boolean averageRGB = false;

    public MaterialBuilder(Identifier Identifier) {
        String name = Identifier.getPath();
        if (name.charAt(name.length() - 1) == '_')
            throw new IllegalArgumentException("Material name cannot end with a '_'!");
        materialInfo = new Material.MaterialInfo(Identifier);
        attributeSet = new MaterialAttributeSet();
    }

    public <T extends MaterialAttribute> T getAttribute(AttributeType<T> key) {
        return attributeSet.getAttribute(key);
    }

    public <T extends MaterialAttribute> boolean hasAttribute(AttributeType<T> key) {
        return attributeSet.hasAttribute(key);
    }

    public <T extends MaterialAttribute> void setAttribute(AttributeType<T> key, T value) {
        attributeSet.setAttribute(key, value);
    }

    public <T extends MaterialAttribute> void addAttribute(AttributeType<T> key) {
        attributeSet.setAttribute(key, key.constructDefault().orElseThrow(() -> new IllegalArgumentException("attribute \"" + key + "\" do not have default constructor!")));
    }

    public MaterialBuilder color(int color) {
        materialInfo.getColors().put(MaterialIconLayer.BaseLayer, color);
        return this;
    }

    public MaterialBuilder secondaryColor(int color) {
        materialInfo.getColors().put(MaterialIconLayer.SecondaryLayer, color);
        return this;
    }

    public MaterialBuilder color(MaterialIconLayer layer, int color) {
        this.materialInfo.getColors().put(layer, color);
        return this;
    }

    public MaterialBuilder colorAverage() {
        this.averageRGB = true;
        return this;
    }

    public MaterialBuilder iconSet(MaterialIconSet iconSet) {
        materialInfo.setIconSet(iconSet);
        return this;
    }

    public MaterialBuilder components(Object... components) {
        Preconditions.checkArgument(
                components.length % 2 == 0,
                "Material Components list malformed!");

        for (int i = 0; i < components.length; i += 2) {
            if (components[i] == null) {
                throw new IllegalArgumentException(
                        "Material in Components List is null for Material " + this.materialInfo.getIdentifier());
            }
            composition.add(new MaterialStack(components[i] instanceof CharSequence chars ? BreaMaterials.get(chars.toString()) : (Material) components[i],
                    ((Number) components[i + 1]).intValue()));
        }
        return this;
    }

    public MaterialBuilder componentStacks(MaterialStack... components) {
        composition = Arrays.asList(components);
        return this;
    }

    public MaterialBuilder componentStacks(ImmutableList<MaterialStack> components) {
        composition = components;
        return this;
    }

    public MaterialBuilder ignoredVariants(MaterialVariant... variants) {
        if (this.ignoredVariants == null) {
            this.ignoredVariants = new HashSet<>();
        }
        this.ignoredVariants.addAll(Arrays.asList(variants));
        return this;
    }

    public MaterialBuilder element(Element element) {
        this.materialInfo.setElement(element);
        return this;
    }

    public MaterialBuilder formula(String formula) {
        this.formula = formula;
        return this;
    }

    public Material buildAndRegister() {
        materialInfo.setComponentList(ImmutableList.copyOf(composition));
        for (MaterialStack materialStack : materialInfo.getComponentList()) {
            Material material = materialStack.getMaterial();
        }

        var mat = new Material(materialInfo, attributeSet);
        if (formula != null) {
            mat.setFormula(formula);
        }
        materialInfo.verifyInfo(attributeSet, averageRGB);
        mat.registerMaterial();
        if (ignoredVariants != null) {
            ignoredVariants.forEach(p -> p.setIgnored(mat));
        }
        mat.verifyMaterial();
        return mat;
    }
}
