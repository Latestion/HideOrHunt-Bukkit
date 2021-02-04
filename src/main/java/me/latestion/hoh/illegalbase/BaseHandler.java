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

    public List<Material> matTypes = new ArrayList<>();
    public List<String> stringTypes = new ArrayList<>();
    public Base base;

    private int ran;
    private boolean isGoneUp = false;
    
    public List<Block> checked = new ArrayList<>();

    public BaseHandler(Base base) {
        this.base = base;
        types();
    }

    public final BlockFace[] faces = {
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.SOUTH,
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.SELF
    };

    public void types() {
        matTypes.add(Material.AIR);
        matTypes.add(Material.CAVE_AIR);
        matTypes.add(Material.LADDER);
        matTypes.add(Material.WATER);
        matTypes.add(Material.TORCH);
        matTypes.add(Material.LAVA);
        matTypes.add(Material.BEACON);
        stringTypes = HideOrHunt.getInstance().getConfig().getStringList("Whitelisted-Blocks");
    }

    // Reference Block
    // Runs Once For Check Joining Block
    // Ignoring Face
    // boolean if its beacon
    public void isLegal(Block block, boolean bol, BlockFace ignore, boolean beacon) {

        /*
        Illegal Checker
        Stops the code from sending StackOverflowError
         */
        ran++;
        if (ran == 50) {
            base.isLegal = false;
            ran = 0;
            throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
        }

        // Block is just a reference for its location and relativity
        for (BlockFace face : faces) {
            Block b = block.getRelative(face); // 1st Block
            if (checked.contains(b) || face == ignore) {
                continue;
            }
            checked.add(b);
            if (isType(b.getType())) {

                Block highestBlock = b.getLocation().getWorld().getHighestBlockAt(b.getLocation());

                // Somehow check if the block is going totally up and meets the highest block y
                // Tree Check
                if (highestBlock.getType().toString().toLowerCase().contains("leaves")) {
                    highestBlock.getLocation().subtract(0, 5, 0);
                }
                int x = b.getX();
                int z = b.getZ();
                topLoop:
                for (int y = b.getY(); y < highestBlock.getY(); y++) {
                    if (!isType(b.getWorld().getBlockAt(x, y, z).getType())) {
                        // SOME HOW CHECK HERE IF IT HAS A WAY
                        if (isType(b.getWorld().getBlockAt(x, y - 1, z).getType()))
                            isLegal(b, true, BlockFace.DOWN, false);
                        break topLoop;
                    }
                    if (y + 1 == highestBlock.getY() || y == highestBlock.getY() - 1) {
                        // IT REACHED THE TOP
                        base.isLegal = true;
                        ran = 0;
                        throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
                    }
                }
                if (b.getY() >= highestBlock.getY() || b.getY() >= highestBlock.getY() - 1) {
                    base.isLegal = true;
                    ran = 0;
                    System.out.println("Legal2");
                    throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
                }
                else {
                    // Checks everything back for the block
                    isLegal(b, true, getIgnoreFace(face), false);
                }
            }
            else {
                // Some different block
                // This can be the entrance block
                if (bol) for (BlockFace f : faces) {
                    if (f == ignore) continue;
                    Block relative = b.getRelative(f);
                    Location relativeLocation = relative.getLocation();
                    double y = relativeLocation.getY();
                    double highestY = relative.getWorld().getHighestBlockYAt(relativeLocation);
                    if (y >= highestY || y >= highestY - 1) {
                        base.isLegal = true;
                        ran = 0;
                        throw new RuntimeException("Help! Somebody debug me! I'm crashing!");
                    }
                }
            }
        }
        if (beacon) {
            base.isLegal = false;
            ran = 0;
            throw new RuntimeException("Help!  Somebody debug me!  I'm crashing!");
        }
    }

    private BlockFace getIgnoreFace(BlockFace face) {
        if (face == BlockFace.DOWN) return BlockFace.UP;
        if (face == BlockFace.NORTH) return BlockFace.SOUTH;
        if (face == BlockFace.EAST) return BlockFace.WEST;
        if (face == BlockFace.UP) return BlockFace.DOWN;
        if (face == BlockFace.SOUTH) return BlockFace.NORTH;
        if (face == BlockFace.WEST) return BlockFace.EAST;
        if (face == BlockFace.SELF) return BlockFace.SELF;
        return null;
    }

    public boolean isType(Material mat) {
        String matType = mat.toString().toLowerCase(); // String of material
        if (matTypes.contains(mat)) {
            return true;
        }
        for (String s : stringTypes) if (matTypes.contains(s)) return true;
        return false;
    }

}
