package me.latestion.hoh.drops;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.latestion.hoh.HideOrHunt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class SchemLoader {

    private HideOrHunt plugin;

    public SchemLoader(HideOrHunt plugin) {
        this.plugin = plugin;
    }

    public void grab(int X, int Y, int Z) throws IOException {

        File schem = new File(plugin.getDataFolder().getAbsolutePath() + "/schems/" + randomSchem() + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(schem);
        ClipboardReader reader = format.getReader(new FileInputStream(schem));
        Clipboard clipboard = reader.read();

        try {

            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(plugin.game.getWorld());
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1);

            // Saves our operation and builds the paste - ready to be completed.
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(X, Y, Z)).ignoreAirBlocks(true).build();

            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) { // If world edit generated an exception it will go here
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> schemNames() {
        return plugin.getConfig().getStringList("Schem-Names");
    }

    public String randomSchem() {
        Random rand = new Random();
        return schemNames().get(rand.nextInt(schemNames().size()));
    }

}
