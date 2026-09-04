package net.ptcrys.breakdown.api.machine.common.component.resource;

@FunctionalInterface
interface ContentsChangedListener<S> {

    void onChanged(int index, S previousContents);
}
