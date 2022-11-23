package me.whizvox.thermonukes.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class ChunkPosArgument implements ArgumentType<Coordinates> {

  private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0");
  public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.pos2d.incomplete"));

  public static ColumnPosArgument chunkPos() {
    return new ColumnPosArgument();
  }

  public static ChunkPos getChunkPos(CommandContext<CommandSourceStack> pContext, String pName) {
    BlockPos blockpos = pContext.getArgument(pName, Coordinates.class).getBlockPos(pContext.getSource());
    return new ChunkPos(blockpos);
  }

  public Coordinates parse(StringReader p_118991_) throws CommandSyntaxException {
    int i = p_118991_.getCursor();
    if (!p_118991_.canRead()) {
      throw ERROR_NOT_COMPLETE.createWithContext(p_118991_);
    } else {
      WorldCoordinate worldcoordinate = WorldCoordinate.parseInt(p_118991_);
      if (p_118991_.canRead() && p_118991_.peek() == ' ') {
        p_118991_.skip();
        WorldCoordinate worldcoordinate1 = WorldCoordinate.parseInt(p_118991_);
        return new WorldCoordinates(worldcoordinate, new WorldCoordinate(true, 0.0D), worldcoordinate1);
      } else {
        p_118991_.setCursor(i);
        throw ERROR_NOT_COMPLETE.createWithContext(p_118991_);
      }
    }
  }

  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> pContext, SuggestionsBuilder pBuilder) {
    if (!(pContext.getSource() instanceof SharedSuggestionProvider)) {
      return Suggestions.empty();
    } else {
      String s = pBuilder.getRemaining();
      Collection<SharedSuggestionProvider.TextCoordinates> collection;
      if (!s.isEmpty() && s.charAt(0) == '^') {
        collection = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
      } else {
        collection = ((SharedSuggestionProvider)pContext.getSource()).getRelevantCoordinates();
      }

      return SharedSuggestionProvider.suggest2DCoordinates(s, collection, pBuilder, Commands.createValidator(this::parse));
    }
  }

  public Collection<String> getExamples() {
    return EXAMPLES;
  }

}
