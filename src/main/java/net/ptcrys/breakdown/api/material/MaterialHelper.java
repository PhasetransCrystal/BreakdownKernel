package net.ptcrys.breakdown.api.material;

public class MaterialHelper {
    /**
     * <code>
       public static MaterialStack getMaterialStack(@Nullable Object object) {
           if (object instanceof MaterialStack materialStack) {
               return materialStack;
           } else if (object instanceof ItemStack itemStack) {
               return getMaterialStack(itemStack);
           } else if (object instanceof ItemLike item) {
               return getMaterialStack(item);
           } else if (object instanceof Ingredient ing) {
               for (var stack : ing.getValues()) {
                   var ms = getMaterialStack(stack);
                   if (!ms.isEmpty()) return ms;
               }
           }
           return MaterialStack.EMPTY;
       }
    
       public static MaterialStack getMaterialStack(ItemStack itemStack) {
           if (itemStack.isEmpty()) return MaterialStack.EMPTY;
           return getMaterialStack(itemStack.getItem());
       }
    
       public static MaterialStack getMaterialStack(@NotNull MaterialEntry entry) {
           Material entryMaterial = entry.material();
           if (!entryMaterial.isNull()) {
               return new MaterialStack(entryMaterial, entry.variant().getMaterialAmount(entryMaterial));
           }
           return MaterialStack.EMPTY;
       }
    
       public static MaterialStack getMaterialStack(ItemLike itemLike) {
           var entry = getMaterialEntry(itemLike);
           if (!entry.isEmpty()) {
               Material entryMaterial = entry.material();
               return new MaterialStack(entryMaterial, entry.variant().getMaterialAmount(entryMaterial));
           }
           ItemMaterialInfo info = ITEM_MATERIAL_INFO.get(itemLike.asItem());
           if (info == null) return MaterialStack.EMPTY;
           if (info.getMaterial().isEmpty()) {
               GTCEu.LOGGER.error("ItemMaterialInfo for {} is empty!", itemLike);
               return MaterialStack.EMPTY;
           }
           return info.getMaterial();
       }
    
       public static Material getMaterial(Fluid fluid) {
           if (FLUID_MATERIAL.isEmpty()) {
               Set<TagKey<Fluid>> allFluidTags = BuiltInRegistries.FLUID.listTagIds().collect(Collectors.toSet());
               for (final Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
                   if (material.hasAttribute(AttributeType.FLUID)) {
                       FluidAttribute property = material.getAttribute(AttributeType.FLUID);
                       FluidStorageKey.allKeys().stream()
                               .map(property::get)
                               .filter(Objects::nonNull)
                               .map(f -> Pair.of(f, TagUtil.createFluidTag(BuiltInRegistries.FLUID.getKey(f).getPath())))
                               .filter(pair -> allFluidTags.contains(pair.getSecond()))
                               .forEach(pair -> {
                                   allFluidTags.remove(pair.getSecond());
                                   FLUID_MATERIAL.put(pair.getFirst(), material);
                               });
                   }
               }
           }
           return FLUID_MATERIAL.getOrDefault(fluid, MarkerMaterial.NULL);
       }
    
       public static MaterialVariant getPrefix(ItemLike itemLike) {
           MaterialEntry entry = getMaterialEntry(itemLike);
           if (!entry.isEmpty()) return entry.variant();
           return MaterialVariant.NULL_PREFIX;
       }
    
       public static MaterialEntry getMaterialEntry(ItemLike itemLike) {
           // asItem is a bit slow, avoid calling it multiple times
           var itemKey = itemLike.asItem();
           var materialEntry = ITEM_MATERIAL_ENTRY_COLLECTED.get(itemKey);
    
           if (materialEntry == null) {
               // Resolve all the lazy suppliers once, rather than on each request. This avoids O(n) lookup performance
               // for unification entries.
               for (var entry : ITEM_MATERIAL_ENTRY) {
                   ITEM_MATERIAL_ENTRY_COLLECTED.put(entry.getFirst().get().asItem(), entry.getSecond());
               }
               ITEM_MATERIAL_ENTRY.clear();
    
               // guess an entry based on the item's tags if none are pre-registered.
               materialEntry = ITEM_MATERIAL_ENTRY_COLLECTED.computeIfAbsent(itemKey, item -> {
                   for (TagKey<Item> itemTag : item.asItem().builtInRegistryHolder().tags().toList()) {
                       MaterialEntry materialEntry1 = getMaterialEntry(itemTag);
                       // check that it's not the empty marker and that it's not a parent tag
                       if (!materialEntry1.isEmpty() &&
                               Arrays.stream(materialEntry1.variant().getItemParentTags()).noneMatch(itemTag::equals)) {
                           return materialEntry1;
                       }
                   }
                   return MaterialEntry.NULL_ENTRY;
               });
           }
           return materialEntry;
       }
    
       public static MaterialEntry getMaterialEntry(TagKey<Item> tag) {
           if (TAG_MATERIAL_ENTRY.isEmpty()) {
               // If the map is empty, resolve all possible tags to their values in an attempt to save time on later
               // lookups.
               Set<TagKey<Item>> allItemTags = BuiltInRegistries.ITEM.listTagIds().collect(Collectors.toSet());
               for (MaterialVariant prefix : MaterialVariant.values()) {
                   for (Material material : BreaApi.materialManager.getRegisteredMaterials()) {
                       Arrays.stream(prefix.getItemTags(material))
                               .filter(allItemTags::contains)
                               .forEach(tagKey -> {
                                   // remove the tag so that the next iteration is faster.
                                   allItemTags.remove(tagKey);
                                   TAG_MATERIAL_ENTRY.put(tagKey, new MaterialEntry(prefix, material));
                               });
                   }
               }
           }
           return TAG_MATERIAL_ENTRY.getOrDefault(tag, MaterialEntry.NULL_ENTRY);
       }
    
       public static List<ItemLike> getItems(MaterialEntry materialEntry) {
           if (materialEntry.material().isNull()) return new ArrayList<>();
           return MATERIAL_ENTRY_ITEM_MAP.computeIfAbsent(materialEntry, entry -> {
               var items = new ArrayList<Supplier<? extends Item>>();
               for (TagKey<Item> tag : getTags(entry.variant(), entry.material())) {
                   for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                       items.add(itemHolder::value);
                   }
               }
               MaterialVariant prefix = entry.variant();
               if (items.isEmpty() && prefix.hasItemTable() && prefix.doGenerateItem(entry.material())) {
                   return List.of(() -> prefix.getItemFromTable(entry.material()).get().asItem());
               }
               return items;
           }).stream().map(Supplier::get).collect(Collectors.toList());
       }
    
       public static ItemStack get(MaterialEntry materialEntry, int size) {
           var list = getItems(materialEntry);
           if (list.isEmpty()) return ItemStack.EMPTY;
           var stack = list.get(0).asItem().getDefaultInstance();
           stack.setCount(size);
           return stack;
       }
    
       public static ItemStack get(MaterialVariant orePrefix, Material material, int stackSize) {
           return get(new MaterialEntry(orePrefix, material), stackSize);
       }
    
       public static ItemStack get(MaterialVariant orePrefix, Material material) {
           return get(orePrefix, material, 1);
       }
    
       public static List<Block> getBlocks(MaterialEntry materialEntry) {
           if (materialEntry.isEmpty()) return Collections.emptyList();
           return MATERIAL_ENTRY_BLOCK_MAP.computeIfAbsent(materialEntry, entry -> {
    
               var blocks = new ArrayList<Supplier<? extends Block>>();
               for (var tag : getTags(materialEntry.variant(), entry.material())) {
                   var blockTag = TagKey.create(Registries.BLOCK, tag.location());
                   for (Holder<Block> itemHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(blockTag)) {
                       blocks.add(itemHolder::value);
                   }
               }
               return blocks;
           }).stream().map(Supplier::get).collect(Collectors.toList());
       }
    
       &#64;Nullable
       public static Block getBlock(MaterialEntry materialEntry) {
           var list = getBlocks(materialEntry);
           if (list.isEmpty()) return null;
           return list.get(0);
       }
    
       &#64;Nullable
       public static Block getBlock(MaterialVariant orePrefix, Material material) {
           return getBlock(new MaterialEntry(orePrefix, material));
       }
    
       &#64;Nullable
       public static TagKey<Block> getBlockTag(MaterialVariant orePrefix, @NotNull Material material) {
           var tags = orePrefix.getBlockTags(material);
           if (tags.length > 0) {
               return tags[0];
           }
           return null;
       }
    
       @Nullable
       public static TagKey<Item> getTag(MaterialVariant orePrefix, @NotNull Material material) {
           var tags = orePrefix.getItemTags(material);
           if (tags.length > 0) {
               return tags[0];
           }
           return null;
       }
    
       public static TagKey<Item>[] getTags(MaterialVariant orePrefix, @NotNull Material material) {
           return orePrefix.getItemTags(material);
       }
    
       public static List<Pair<ItemStack, ItemMaterialInfo>> getAllItemInfos() {
           List<Pair<ItemStack, ItemMaterialInfo>> f = new ArrayList<>();
           for (var entry : ITEM_MATERIAL_INFO.entrySet()) {
               f.add(Pair.of(new ItemStack(entry.getKey()), entry.getValue()));
           }
           return f;
       }
        * </code>
     */
}
