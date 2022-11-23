package me.whizvox.thermonukes.common.capability.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCapabilityProvider<CAP extends INBTSerializable<TAG>, TAG extends Tag> implements ICapabilitySerializable<TAG> {

  private final Capability<CAP> capability;
  private final CAP obj;
  private final LazyOptional<CAP> lazyOp;

  public SimpleCapabilityProvider(Capability<CAP> capability, CAP obj) {
    this.capability = capability;
    this.obj = obj;
    lazyOp = LazyOptional.of(() -> obj);
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return cap == capability ? lazyOp.cast() : LazyOptional.empty();
  }

  @Override
  public TAG serializeNBT() {
    return obj.serializeNBT();
  }

  @Override
  public void deserializeNBT(TAG tag) {
    obj.deserializeNBT(tag);
  }

}
