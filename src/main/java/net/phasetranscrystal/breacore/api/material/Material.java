package net.phasetranscrystal.breacore.api.material;

import net.phasetranscrystal.brealib.util.BreaMath;
import net.phasetranscrystal.brealib.util.FormattingUtil;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.material.attributes.MaterialAttribute;
import net.phasetranscrystal.breacore.api.material.attributes.MaterialAttributeSet;
import net.phasetranscrystal.breacore.api.material.info.MaterialIconLayer;
import net.phasetranscrystal.breacore.api.material.info.MaterialIconSet;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;
import net.phasetranscrystal.breacore.api.material.registry.IMaterialBuilderExtension;
import net.phasetranscrystal.breacore.api.material.stack.MaterialResource;
import net.phasetranscrystal.breacore.api.material.stack.MaterialStack;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.breacore.common.data.BreaMaterials;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Function;

public class Material implements Comparable<Material>, IMaterialExtension {

    @Getter
    @NotNull
    private final MaterialInfo materialInfo;
    @Getter
    @NotNull
    private final MaterialAttributeSet attributeSet;
    @Getter
    private String chemicalFormula;

    private Material(@NotNull MaterialInfo materialInfo, @NotNull MaterialAttributeSet attributeSet) {
        this.materialInfo = materialInfo;
        this.attributeSet = attributeSet;
        verifyMaterial();
    }

    protected Material(Identifier identifier) {
        this.materialInfo = new MaterialInfo(identifier);
        this.attributeSet = new MaterialAttributeSet();
        this.attributeSet.setMaterial(this);
    }

    private String calculateChemicalFormula() {
        if (chemicalFormula != null) return this.chemicalFormula;
        if (materialInfo.element != null) {
            String[] split = materialInfo.element.symbol().split("-");
            String result;
            if (split.length > 1) {
                split[1] = FormattingUtil.toSmallUpNumbers(split[1]);
                result = split[0] + split[1];
            } else result = materialInfo.element.symbol();
            return result;
        }
        if (!materialInfo.componentList.isEmpty()) {
            StringBuilder components = new StringBuilder();
            for (var component : materialInfo.componentList)
                components.append(component.toString());
            return components.toString();
        }
        return "";
    }

    public Material setFormula(String formula) {
        return setFormula(formula, true);
    }

    public Material setFormula(String formula, boolean withFormatting) {
        this.chemicalFormula = withFormatting ? FormattingUtil.toSmallDownNumbers(formula) : formula;
        return this;
    }

    public ImmutableList<MaterialStack> getMaterialComponents() {
        return this.materialInfo.componentList;
    }

    public Material setComponents(MaterialStack... components) {
        this.materialInfo.setComponents(components);
        return this;
    }

    public void registerMaterial() {
        BreaApi.materialManager.register(this);
    }

    public String getName() {
        return materialInfo.identifier.getPath();
    }

    public String getModId() {
        return materialInfo.identifier.getNamespace();
    }

    public boolean isElement() {
        return materialInfo.element != null;
    }

    public Optional<Element> getElement() {
        return Optional.ofNullable(materialInfo.element);
    }

    protected void calculateDecompositionType() {}

    public int getLayerARBG(MaterialIconLayer layer) {
        if (!materialInfo.colors.containsKey(layer)) return -1;
        var layerColor = getMaterialARBG(layer);
        if (layerColor != -1 || layer == MaterialIconLayer.BaseLayer) return layerColor;
        else return getMaterialARBG(layer);
    }

    public int getMaterialARBG() {
        return materialInfo.colors.get(MaterialIconLayer.BaseLayer) | 0xFF000000;
    }

    public int getMaterialARBG(MaterialIconLayer layer) {
        return materialInfo.colors.get(layer) | 0xFF000000;
    }

    public int getMaterialRBG() {
        return materialInfo.colors.get(MaterialIconLayer.BaseLayer);
    }

    public int getMaterialRBG(MaterialIconLayer layer) {
        return materialInfo.colors.get(layer);
    }

    public void setMaterialARBG(int materialARBG) {
        materialInfo.colors.put(MaterialIconLayer.BaseLayer, materialARBG);
    }

    public void setMaterialARBG(MaterialIconLayer layer, int materialARBG) {
        materialInfo.colors.put(layer, materialARBG);
    }

    public boolean hasFluidColor() {
        return materialInfo.colors.containsKey(MaterialIconLayer.FluidLayer);
    }

    public MaterialIconSet getMaterialIconSet() {
        return this.materialInfo.iconSet;
    }

    public void setMaterialIconSet(MaterialIconSet materialIconSet) {
        this.materialInfo.iconSet = materialIconSet;
    }

    public Identifier getIdentifier() {
        return materialInfo.identifier;
    }

    public String getUnlocalizedName() {
        return materialInfo.identifier.toLanguageKey("material");
    }

    public MutableComponent getLocalizedName() {
        return Component.translatable(getUnlocalizedName());
    }

    @Override
    public int compareTo(@NonNull Material material) {
        return toString().compareTo(material.toString());
    }

    @Override
    public String toString() {
        return materialInfo.identifier.toString();
    }

    public MaterialStack multiply(int amount) {
        return new MaterialStack(this, amount);
    }

    public <T extends MaterialAttribute> boolean hasAttribute(AttributeType<T> type) {
        return attributeSet.hasAttribute(type);
    }

    public <T extends MaterialAttribute> T getAttribute(AttributeType<T> type) {
        return attributeSet.getAttribute(type);
    }

    public <T extends MaterialAttribute> void setAttribute(AttributeType<T> type, T value) {
        attributeSet.setAttribute(type, value);
    }

    public void verifyMaterial() {
        this.chemicalFormula = calculateChemicalFormula();
        calculateDecompositionType();
    }

    public boolean isNull() {
        return this == MarkerMaterial.NULL;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Material material))
            return false;

        return Objects.equals(this.getIdentifier(), material.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getIdentifier());
    }

    public Holder<Material> builtInRegistryHolder() {
        return BreaRegistries.MATERIALS.get(getIdentifier()).get();
    }

    public boolean isSame(Material other) {
        return other == this;
    }

    private MaterialResource defaultResource;

    public MaterialResource computeDefaultResource(Function<Material, MaterialResource> resourceConstructor) {
        if (this.defaultResource == null) {
            this.defaultResource = (MaterialResource) resourceConstructor.apply(this);
        }

        return this.defaultResource;
    }

    @Accessors(chain = true)
    public static class MaterialInfo {

        /**
         * 材料的ID
         */
        @Getter
        private final Identifier identifier;
        /**
         * 材料的贴图层级颜色
         */
        @Getter
        @Setter
        private Map<MaterialIconLayer, Integer> colors = new HashMap<>();
        @Getter
        @Setter
        private ImmutableList<MaterialStack> componentList;
        @Getter
        @Setter
        private MaterialIconSet iconSet;
        @Getter
        @Setter
        private Element element;

        public MaterialInfo(@NotNull Identifier identifier) {
            this.identifier = identifier;
            colors.put(MaterialIconLayer.BaseLayer, -1);
        }

        public void verifyInfo(MaterialAttributeSet attributeSet, boolean averageRGB) {
            if (iconSet == null)
                iconSet = attributeSet.hasAttribute(AttributeType.FLUID) ? MaterialIconSet.FLUID : MaterialIconSet.DEFAULT;
            if (colors.get(MaterialIconLayer.BaseLayer) == -1) {
                if (!averageRGB || componentList.isEmpty())
                    colors.put(MaterialIconLayer.BaseLayer, 0xFFFFFF);
                else {
                    long colorTemp = 0;
                    long divisor = 0;
                    for (var stack : componentList) {
                        colorTemp += stack.getMaterial().getMaterialARBG() * stack.amount();
                        divisor += stack.amount();
                    }
                    colors.put(MaterialIconLayer.BaseLayer, BreaMath.saturatedCast(colorTemp / divisor));
                }
            }
        }

        public MaterialInfo setComponents(MaterialStack... components) {
            this.componentList = ImmutableList.copyOf(Arrays.stream(components).toList());
            return this;
        }
    }

    public static class Builder implements IMaterialBuilderExtension {

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

        public Builder(Identifier Identifier) {
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

        public <T extends MaterialAttribute> Builder setAttribute(AttributeType<T> key, T value) {
            attributeSet.setAttribute(key, value);
            return this;
        }

        public <T extends MaterialAttribute> Builder addAttribute(AttributeType<T> key) {
            attributeSet.setAttribute(key, key.constructDefault().orElseThrow(() -> new IllegalArgumentException("attribute \"" + key + "\" do not have default constructor!")));
            return this;
        }

        public Builder color(int color) {
            materialInfo.getColors().put(MaterialIconLayer.BaseLayer, color);
            return this;
        }

        public Builder secondaryColor(int color) {
            materialInfo.getColors().put(MaterialIconLayer.SecondaryLayer, color);
            return this;
        }

        public Builder color(MaterialIconLayer layer, int color) {
            this.materialInfo.getColors().put(layer, color);
            return this;
        }

        public Builder colorAverage() {
            this.averageRGB = true;
            return this;
        }

        public Builder iconSet(MaterialIconSet iconSet) {
            materialInfo.setIconSet(iconSet);
            return this;
        }

        public Builder components(Object... components) {
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

        public Builder componentStacks(MaterialStack... components) {
            composition = Arrays.asList(components);
            return this;
        }

        public Builder componentStacks(ImmutableList<MaterialStack> components) {
            composition = components;
            return this;
        }

        public Builder ignoredVariants(MaterialVariant... variants) {
            if (this.ignoredVariants == null) {
                this.ignoredVariants = new HashSet<>();
            }
            this.ignoredVariants.addAll(Arrays.asList(variants));
            return this;
        }

        public Builder element(Element element) {
            this.materialInfo.setElement(element);
            return this;
        }

        public Builder formula(String formula) {
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
                // ignoredVariants.forEach(p -> p.setIgnored(mat));
            }
            mat.verifyMaterial();
            return mat;
        }
    }
}
