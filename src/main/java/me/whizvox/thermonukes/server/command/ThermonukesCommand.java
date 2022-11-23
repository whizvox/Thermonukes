package me.whizvox.thermonukes.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import me.whizvox.thermonukes.common.capability.EntityRadiation;
import me.whizvox.thermonukes.common.capability.WorldRadiation;
import me.whizvox.thermonukes.common.lib.ThermonukesCapabilities;
import me.whizvox.thermonukes.common.lib.ThermonukesLang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.Collection;
import java.util.List;

public class ThermonukesCommand {

  private static final SimpleCommandExceptionType
      MUST_BE_ENTITY = new SimpleCommandExceptionType(Component.translatable(ThermonukesLang.Keys.COMMAND_GENERIC_MUST_BE_ENTITY)),
      NOT_LOOKING_AT_ENTITY = new SimpleCommandExceptionType(Component.translatable(ThermonukesLang.Keys.COMMAND_GENERIC_NOT_LOOKING_AT_ENTITY));

  private static boolean isEntity(CommandSourceStack src) {
    return src.getEntity() != null;
  }

  private static boolean playerHasPermission(CommandSourceStack src, int minPermissionLevel) {
    return src.isPlayer() && src.hasPermission(minPermissionLevel);
  }

  private static boolean playerIsOp(CommandSourceStack src) {
    return playerHasPermission(src, 2);
  }

  private static boolean hasPermission(CommandSourceStack src, int minPermissionLevel) {
    return src.hasPermission(minPermissionLevel);
  }

  private static boolean isOp(CommandSourceStack src) {
    return hasPermission(src, 2);
  }

  private static BlockPos getBlockPos(CommandContext<CommandSourceStack> ctx) {
    return new BlockPos(ctx.getSource().getPosition());
  }

  private static ChunkPos getChunkPos(CommandContext<CommandSourceStack> ctx) {
    return new ChunkPos(getBlockPos(ctx));
  }

  private static int clearCaches(CommandContext<CommandSourceStack> ctx) {
    //GeigerCounterHelper.clearCache();
    return 1;
  }

  private static int clearEntityRadiation(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities) {
    entities.forEach(entity -> {
      if (entity instanceof LivingEntity living) {
        living.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(EntityRadiation::clearRadiation);
      }
    });
    ctx.getSource().sendSystemMessage(ThermonukesLang.commandRadiationClearEntity(entities));
    return 1;
  }

  private static int clearChunkRadiation(CommandContext<CommandSourceStack> ctx, Level level, ChunkPos pos) {
    level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(radiation -> radiation.clearRadiation(pos));
    ctx.getSource().sendSystemMessage(ThermonukesLang.commandRadiationClearChunk(pos, level.dimension()));
    return 1;
  }

  private static int clearLevelRadiation(CommandContext<CommandSourceStack> ctx, ServerLevel level) {
    level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(WorldRadiation::clearAllRadiation);
    ctx.getSource().sendSystemMessage(ThermonukesLang.commandRadiationClearLevel(level.dimension()));
    return 1;
  }

  private static int clearAllRadiation(CommandContext<CommandSourceStack> ctx) {
    MinecraftServer server = ctx.getSource().getServer();
    server.getAllLevels().forEach(level ->
        level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(WorldRadiation::clearAllRadiation)
    );
    server.getAllLevels().forEach(level -> level.getAllEntities().forEach(entity -> {
      if (entity instanceof LivingEntity living) {
        living.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(EntityRadiation::clearRadiation);
      }
    }));
    ctx.getSource().sendSystemMessage(ThermonukesLang.COMMAND_RADIATION_CLEAR_ALL);
    return 1;
  }

  private static int setChunkRadiation(CommandContext<CommandSourceStack> ctx, ServerLevel level, ChunkPos pos, int dosage) {
    level.getCapability(ThermonukesCapabilities.WORLD_RADIATION).ifPresent(radiation -> {
      radiation.setRadiationDosage(pos, dosage);
    });
    ctx.getSource().sendSystemMessage(ThermonukesLang.commandRadiationSetChunk(dosage, pos, level.dimension()));
    return 1;
  }

  private static int setEntityRadiation(CommandContext<CommandSourceStack> ctx, float dosage) throws CommandSyntaxException {
    Entity source = ctx.getSource().getEntity();
    if (source == null) {
      throw MUST_BE_ENTITY.create();
    }
    Level level = source.level;
    Vec3 sPos = source.getEyePosition(); // start
    Vec3 ePos = sPos.add(source.getLookAngle().multiply(10, 10, 10)); // end
    BlockHitResult blockHit = level.clip(new ClipContext(sPos, ePos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, source));
    EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, source, sPos, ePos, new AABB(sPos, ePos), entity -> entity instanceof LivingEntity);
    if (entityHit != null && entityHit.getType() == HitResult.Type.ENTITY && entityHit.distanceTo(source) < blockHit.distanceTo(source)) {
      setEntityRadiation(ctx, List.of(entityHit.getEntity()), dosage);
    } else {
      throw NOT_LOOKING_AT_ENTITY.create();
    }
    return 1;
  }

  private static int setEntityRadiation(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> entities, float dosage) {
    entities.forEach(entity -> {
      if (entity instanceof LivingEntity) {
        entity.getCapability(ThermonukesCapabilities.ENTITY_RADIATION).ifPresent(radiation -> radiation.setDosage(dosage));
      }
    });
    ctx.getSource().sendSystemMessage(ThermonukesLang.commandRadiationSetEntities(dosage, entities));
    return 1;
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("thermonukes")
        .then(Commands.literal("clearcaches")
            .requires(src -> hasPermission(src, 1))
            .executes(ThermonukesCommand::clearCaches)
        )
        .then(Commands.literal("radiation")
            .then(Commands.literal("clear")
                .requires(src -> playerIsOp(src))
                .then(Commands.literal("all").executes(ctx -> clearAllRadiation(ctx)))
                .then(Commands.literal("entity")
                    .then(Commands.argument("target", EntityArgument.entities())
                        .executes(ctx -> clearEntityRadiation(ctx, EntityArgument.getEntities(ctx, "target")))
                    )
                )
                .then(Commands.literal("chunk")
                    .executes(ctx -> clearChunkRadiation(
                        ctx,
                        ctx.getSource().getLevel(),
                        getChunkPos(ctx)
                    ))
                    .then(Commands.argument("chunkPos", ChunkPosArgument.chunkPos())
                        .executes(ctx -> clearChunkRadiation(
                            ctx,
                            ctx.getSource().getLevel(),
                            ChunkPosArgument.getChunkPos(ctx, "chunkPos")
                        ))
                        .then(Commands.argument("dimension", DimensionArgument.dimension())
                            .executes(ctx -> clearChunkRadiation(
                                ctx,
                                DimensionArgument.getDimension(ctx, "dimension"),
                                ChunkPosArgument.getChunkPos(ctx, "chunkPos")
                            ))
                        )
                    )
                )
                .then(Commands.literal("level")
                    .executes(ctx -> clearLevelRadiation(ctx, ctx.getSource().getLevel()))
                    .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .executes(ctx -> clearLevelRadiation(ctx, DimensionArgument.getDimension(ctx, "dimension")))
                    )
                )
            )
            .then(Commands.literal("set")
                .requires(src -> playerIsOp(src))
                .then(Commands.literal("chunk")
                    .then(Commands.argument("dosage", IntegerArgumentType.integer(0, 100_000_000))
                        .executes(ctx -> setChunkRadiation(
                            ctx,
                            ctx.getSource().getLevel(),
                            getChunkPos(ctx),
                            IntegerArgumentType.getInteger(ctx, "dosage")
                        ))
                        .then(Commands.argument("chunkPos", ChunkPosArgument.chunkPos())
                            .executes(ctx -> setChunkRadiation(
                                ctx,
                                DimensionArgument.getDimension(ctx, "dimension"),
                                ChunkPosArgument.getChunkPos(ctx, "chunkPos"),
                                IntegerArgumentType.getInteger(ctx, "dosage")
                            ))
                            .then(Commands.argument("dimension", DimensionArgument.dimension())
                                .executes(ctx -> setChunkRadiation(
                                    ctx,
                                    DimensionArgument.getDimension(ctx, "dimension"),
                                    getChunkPos(ctx),
                                    IntegerArgumentType.getInteger(ctx, "dosage")
                                ))
                            )
                        )
                    )
                )
                .then(Commands.literal("entity")
                    .then(Commands.argument("dosage", IntegerArgumentType.integer(0, 100_000_000))
                        .executes(ctx -> setEntityRadiation(
                            ctx,
                            IntegerArgumentType.getInteger(ctx, "dosage")
                        ))
                        .then(Commands.argument("target", EntityArgument.entities())
                            .executes(ctx -> setEntityRadiation(
                                ctx,
                                EntityArgument.getEntities(ctx, "target"),
                                IntegerArgumentType.getInteger(ctx, "dosage")
                            ))
                        )
                    )
                )
            )
        )
    );
  }

}
