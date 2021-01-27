package me.latestion.hoh.illegalbase;

import me.latestion.hoh.HideOrHunt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BaseHandler {

    public static List<Material> types = new ArrayList<>();

    public BaseHandler() {

    }

    public static final BlockFace[] faces = {
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.SOUTH,
            BlockFace.UP
    };

    public static void types() {
        types.add(Material.AIR);
        types.add(Material.CAVE_AIR);
        types.add(Material.LADDER);
        types.add(Material.WATER);
        types.add(Material.TORCH);
    }

    public static void isLegal(Block block, boolean bol, Base base, BlockFace ignore) {
        if (types.isEmpty()) types();
        // Block is just a reference for its location and relativity
        for (BlockFace face : faces) {
            Block b = block.getRelative(face); // 1st Block
            if (face == ignore) {
                continue;
            }
            if (types.contains(b.getType()) || b.getType().toString().toLowerCase().contains("door")) {
                // might take us to the entrance
                Block highestBlock = b.getLocation().getWorld().getHighestBlockAt(b.getLocation());
                // Somehow check if the block is going totally up and meets the highest block y
                int x = b.getX();
                int z = b.getZ();
                topLoop:
                for (int y = b.getY(); y < highestBlock.getY(); y++) {
                    if (!types.contains(b.getWorld().getBlockAt(x, y, z).getType())) {
                        break topLoop;
                    }
                    if (y + 1 == highestBlock.getY() || y == highestBlock.getY() - 1) {
                        base.isLegal = true;
                        throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
                    }
                }
                // Tree Check
                if (highestBlock.getType().toString().toLowerCase().contains("leaves")) {
                    highestBlock.getLocation().subtract(0, 5, 0);
                }
                if (b.getY() >= highestBlock.getY() || b.getY() >= highestBlock.getY() - 1) {
                    base.isLegal = true;
                    throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
                }
                else {
                    // Checks everything back for the block
                    isLegal(b, true, base, getIgnoreFace(face));
                }
            }
            else {
                // Some different block
                // This can be the entrance block
                if (bol) for (BlockFace f : faces) {
                    Block relative = b.getRelative(f);
                    Location relativeLocation = relative.getLocation();
                    if (relativeLocation.getY() >= relative.getWorld().getHighestBlockYAt(relativeLocation)) {
                        base.isLegal = true;
                        throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
                    }
                }
            }
        }
    }

    private static BlockFace getIgnoreFace(BlockFace face) {
        if (face == BlockFace.DOWN) return BlockFace.UP;
        if (face == BlockFace.NORTH) return BlockFace.SOUTH;
        if (face == BlockFace.EAST) return BlockFace.WEST;
        if (face == BlockFace.UP) return BlockFace.DOWN;
        if (face == BlockFace.SOUTH) return BlockFace.NORTH;
        if (face == BlockFace.WEST) return BlockFace.EAST;
        return null;
    }
}
