package mods.gregtechmod.objects.blocks.teblocks.base;

import java.util.List;

public abstract class TileEntityRedstoneDisplayBase extends TileEntityMultiMode {
    private int lastRedstone;
    
    @Override
    protected int getTextureIndex() {
        return this.lastRedstone;
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (isAllowedToWork()) {
            int strength = this.world.getRedstonePowerFromNeighbors(this.pos);
            if (this.lastRedstone != strength) {
                this.lastRedstone = strength;
                updateClientField("lastRedstone");
            }
        }
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("lastRedstone");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("lastRedstone")) rerender();
    }
}
