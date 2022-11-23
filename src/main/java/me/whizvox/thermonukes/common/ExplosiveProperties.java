package me.whizvox.thermonukes.common;

public record ExplosiveProperties(
    int fuseLength,
    boolean litByPlayer,
    boolean litByExplosive,
    boolean litByRedstone,
    boolean litByProjectile) {

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private int fuseLength;
    private boolean litByPlayer;
    private boolean litByExplosive;
    private boolean litByRedstone;
    private boolean litByProjectile;

    private Builder() {
      fuseLength = 100;
      litByPlayer = true;
      litByExplosive = true;
      litByRedstone = true;
      litByProjectile = true;
    }

    public Builder fuseLength(int fuseLength) {
      this.fuseLength = fuseLength;
      return this;
    }

    public Builder litByPlayer(boolean litByPlayer) {
      this.litByPlayer = litByPlayer;
      return this;
    }

    public Builder litByExplosive(boolean litByExplosive) {
      this.litByExplosive = litByExplosive;
      return this;
    }

    public Builder litByRedstone(boolean litByRedstone) {
      this.litByRedstone = litByRedstone;
      return this;
    }

    public Builder litByProjectile(boolean litByProjectile) {
      this.litByProjectile = litByProjectile;
      return this;
    }

    public ExplosiveProperties build() {
      return new ExplosiveProperties(fuseLength, litByPlayer, litByExplosive, litByRedstone, litByProjectile);
    }
  }

}
